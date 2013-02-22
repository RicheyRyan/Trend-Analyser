(ns trend-analyser.tweets
  "This namespace handles all the major tweet processing. It makes requests to Twitter,
  interacts with the database and supplies functions to be called from the other namespaces."
  (:require [cheshire.core :as cheshire]
            [clj-http.client :as http]
            [clojure.string :as string]
            [hiccup.util :as util]
            [trend-analyser.data :as data]
            [overtone.at-at :as at]
            [trend-analyser.scheduler :as scheduler]))

;The URL for receiving a list of trends from Twitter.
(def trend-url 
  "https://api.twitter.com/1/trends/560743.json")

;A thread pool to contain timed jobs.
(def my-pool 
   (at/mk-pool))

(defn parse-trends 
  "Loops through a response from Twitter containing trends and returns a list with the trend names."
  [list]
  {:trends
   (into []
         (for [x list]
           (x :name)))})

(defn trends-response 
  "Queries Twitter for the trends, pulls out the request body, parses the JSON,
  extracts the trend data and parses it."
  []
  (-> 
    (http/get trend-url {:accept :json}) 
    (:body) 
    (cheshire/parse-string true) 
    peek 
    (:trends)
    (parse-trends)))

(defn trends-json 
  "Retrieves the trends from the database and creates a JSON string."
  []
  (cheshire/generate-string (data/retrieve-trends)))

(defn min-to-millis 
  "A helper function which takes a number of minutes and returns the equivilent number of milliseconds."
  [minutes]
  (* 1000 (* 60 minutes)))

(defn timed-query 
  "A helper function that takes another function and executes it at the specified."
  [query interval]
  (at/every interval (query) my-pool :fixed-delay true))

(defn add-trends-to-db 
  "Takes the parsed trend data and inserts it into the database."
  []
  (data/insert-trends (trends-response)))

(defn parse-tweets-response 
  "A function to parse the response send by Twitter.
   It pulls the body out of the response and parses the json string."
   [response]
  (->
    (response :body)
    (cheshire/parse-string true)))

(defn generate-tweet-query 
  "A helper function to take a number of parameters such as a query and the desired number of results
  and assemble a valid URL to query."
  [query]
  (str
    (util/url "http://search.twitter.com/search.json" 
              {:q query 
               :include_entities true
               :result_type "mixed"
               :rpp 100})))

(defn initial-tweets-request 
  "A function to query Twitter for tweets and parse the response if it was successful."
  [search-term]
    (let [response
            (http/get (generate-tweet-query search-term) {:accept :json})]
      (if (= (response :status) 200)
        (parse-tweets-response response)
        (throw 
         (Exception. 
          (str 
           "Request for tweet JSON failed. Response status: " 
           (response :status) "."))))))

(defn get-tweet-info 
  "A function to parse the data that will be used for processing the tweets out of each tweet.Each tweet
  is returned in it's own map with the relevant data."
  [response]
  (let [results (response :results)]
  (for [x results]
          (merge (select-keys x [:created_at :id :text])
                 (select-keys (x :entities)[:hashtags :urls :user_mentions])
                 (select-keys (x :metadata)[:recent_retweets])))))

(defn raw-tweet-to-db 
  "A function to insert the parsed tweets into the database.
  The tweets have their trend associated with them."
  [trend]
  (let [tweets (get-tweet-info 
                 (initial-tweets-request trend))]
    (data/insert-raw-tweets tweets trend)))

(defn raw-tweets-from-db 
  "A function to pull the tweets from the database in with all the information being stored."
  []
      (data/retrieve-raw-tweets))

(defn tweet-json 
  "A function to take tweets and generate a JSON string containing the tweets."
  [search-term]
      (let [tweets (get-tweet-info 
                     (initial-tweets-request search-term))]
        (cheshire/generate-string 
         (take 10
               (for [tweet tweets]
                 (merge (get tweet :id)))))))


(def immediately 0)

(def every-minute (* 60 1000))
  
(defn start [fn]
  (scheduler/periodically fn immediately every-minute))

(defn stop []
  (scheduler/shutdown))




  ;;(timed-query #(trends-response) (min-to-millis 30))

(comment 
(def avg-age 360)

(def avg-rt 50)

(def br1 (+ (* avg-age avg-rt) (* 120 50)))

(def br2 (+ (* avg-age avg-rt) (* 600 50)))

(double (/ br1 (* avg-age 120)))

(quot br1 (* avg-age 120))

(double (/ br2 (* avg-age 600)))

(> (/ br1 (* avg-age 120)) (/ br2 (* avg-age 600))))


