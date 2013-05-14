(ns trend-analyser.tweets
  "This namespace handles all the major tweet processing. It makes requests to Twitter,
  interacts with the database and supplies functions to be called from the other namespaces."
  (:require [cheshire.core :as cheshire]
            [clj-http.client :as http]
            [clojure.string :as string]
            [hiccup.util :as util]
            [trend-analyser.data :as data]
            [trend-analyser.scheduler :as scheduler]))

;The URL for receiving a list of trends from Twitter.
(def trend-url 
  "https://api.twitter.com/1/trends/560743.json")

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

(defn trends-response 
  "Queries Twitter for the trends, pulls out the request body, parses the JSON,
  extracts the trend data and parses it."
  []
    (parse-trends 
     (:trends 
       (peek 
         (cheshire/parse-string 
                                (:body (http/get trend-url {:accept :json})) true)))))

(defn trends-json 
  "Retrieves the trends from the database and creates a JSON string."
  []
  (cheshire/generate-string (data/retrieve-trends)))

(defn min-to-millis 
  "A helper function which takes a number of minutes and returns the equivilent number of milliseconds."
  [minutes]
  (* 1000 (* 60 minutes)))

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
               :lang "en"
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

(defn parse-tweet-for-hashtag
  "A function to parse all the text of trend names out of tweet data.a"
  [tweet]
  (for [x (tweet :hashtags)]
    (x :text)))


(defn hashtag-json 
  "A function to take tweets and generate a JSON string containing all the hashtags associated with those tweets."
  [search-term]
      (let [tweets (get-tweet-info 
                     (initial-tweets-request search-term))]
        (cheshire/generate-string
          (set 
            (flatten
              (for [tweet tweets] 
                (parse-tweet-for-hashtag tweet)))))))

(defn add-tweets-to-db 
  "A function to facilitate requesting tweets from Twitter and feed them into the database."
  []
  (let [trends (data/retrieve-trends)]
    (for [trend trends]
      (raw-tweet-to-db trend))))

(defn add-trends-to-db 
  "Takes the parsed trend data and inserts it into the database."
  []
  (data/insert-trends (trends-response))
  (add-tweets-to-db)
  (println "inserting"))
  
;A value to be used in the start function to indicate the task should start immediately.
(def immediately 0)

;A value to be used in the start function which represents a period of a minute.
(def every-minute (* 60 1000))


(defn start 
  "A function that executes the supplied function for the frequency specified by the period."
  [fn period]
  (scheduler/periodically fn immediately period))

(defn stop
  "A function to stopped the timed execution that start initiated."
  []
  (scheduler/shutdown))

;Trends are requested and added to Twitter every minute.
;(start #(add-trends-to-db) every-minute)

;The bones of code to create a bayesian rating system for tweets. The number of retweets would
;be rated by how old the tweets were in order to created value which would provide an accurate rank for
;that tweet.
(comment 
(def avg-age 360)

(def avg-rt 50)

(def br1 (+ (* avg-age avg-rt) (* 120 50)))

(def br2 (+ (* avg-age avg-rt) (* 600 50)))

(double (/ br1 (* avg-age 120)))

(quot br1 (* avg-age 120))

(double (/ br2 (* avg-age 600)))

(> (/ br1 (* avg-age 120)) (/ br2 (* avg-age 600))))


