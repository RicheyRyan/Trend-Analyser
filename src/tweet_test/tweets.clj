(ns tweet-test.tweets
  (:require [cheshire.core :as cheshire]
            [clj-http.client :as http]
            [clojure.string :as string]
            [hiccup.util :as util]
            [tweet-test.data :as data]
            [overtone.at-at :as at]))

(def trend-url "https://api.twitter.com/1/trends/560743.json")

(def my-pool (at/mk-pool))

;;Utilities for requesting and parsing trend data
(defn parse-trends [list]
               (for [x list]
                 (x :name)))

(defn trends-response []
  (-> 
    (http/get trend-url {:accept :json}) 
    (:body) 
    (cheshire/parse-string true) 
    peek 
    (:trends)
    (parse-trends)
    (cheshire/generate-string)))

(defn trends-json []
  (cheshire/generate-string (trends-response)))

(comment (defn add-trends-to-db []
  (at/every 5000 #(data/insert-trends (trends-response)(println "inserting trends")) my-pool :fixed-delay true)))

;;Utilities for requesting and parsing tweet data
(defn parse-tweets-response [response]
  (->
    (response :body)
    (cheshire/parse-string true)))

(defn generate-tweet-query [query]
  (str
    (util/url "http://search.twitter.com/search.json" 
              {:q query 
               :include_entities true
               :result_type "mixed"
               :rpp 100})))

(defn initial-tweets-request [search-term]
    (let [response
            (http/get (generate-tweet-query search-term) {:accept :json})]
      (if (= (response :status) 200)
        (parse-tweets-response response)
        (throw 
         (Exception. 
          (str 
           "Request for tweet JSON failed. Response status: " 
           (response :status) "."))))))

(defn string-tweets-request [next-page]
  (let [response
  (http/get next-page {:accept :json})]
    (if (= (response :status) 200)
        (parse-tweets-response response)
        (throw 
         (Exception. 
          (str 
           "Request for tweet JSON failed. Response status: " 
           (response :status) "."))))))

(defn get-next-page [response]
  (str (util/url "http://search.twitter.com/search.json?" (response :next_page))))

(defn get-tweet-info [response]
  (let [results (response :results)]
  (for [x results]
    (into {}
          (merge (select-keys x [:created_at :id :text])
                 (select-keys (x :entities)[:hashtags :urls :user_mentions])
                 (select-keys (x :metadata)[:recent_retweets]))))))

(defn send-to-db [tweets]
            (println tweets))

;(add-trends-to-db)

(comment 
(def avg-age 360)

(def avg-rt 50)

(def br1 (+ (* avg-age avg-rt) (* 120 50)))

(def br2 (+ (* avg-age avg-rt) (* 600 50)))

(double (/ br1 (* avg-age 120)))

(quot br1 (* avg-age 120))

(double (/ br2 (* avg-age 600)))

(> (/ br1 (* avg-age 120)) (/ br2 (* avg-age 600))))


