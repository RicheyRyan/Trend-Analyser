(ns tweet-test.tweets
  (:require [cheshire.core :as cheshire]
            [clj-http.client :as http]
            [clojure.string :as string]
            [hiccup.util :as util]))

(def trend-url "https://api.twitter.com/1/trends/560743.json")

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
    (parse-trends)))

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
               :lang "en"
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
  (util/url "http://search.twitter.com/search.json" (response :next_page)))

(defn get-tweet-info [response]
  (let [results (response :results)]
  (for [x results]
    (into {}
          (merge (select-keys x [:created_at :id :text])
                 (select-keys (x :entities)[:hashtags :urls :user_mentions])
                 (select-keys (x :metadata)[:recent_retweets]))))))

(defn get-all-tweets [term]
  (let [tweets (list)
        first-response (initial-tweets-request term)]
    (conj tweets (get-tweet-info first-response))
    (loop [next-page (get-next-page first-response)]
      (let [next-response (string-tweets-request next-page)]
        (conj tweets (get-tweet-info next-response))
        (recur (get-next-page next-response))))))


(comment 
(def avg-age 360)

(def avg-rt 50)

(def br1 (+ (* avg-age avg-rt) (* 120 50)))

(def br2 (+ (* avg-age avg-rt) (* 600 50)))

(double (/ br1 (* avg-age 120)))

(quot br1 (* avg-age 120))

(double (/ br2 (* avg-age 600)))

(> (/ br1 (* avg-age 120)) (/ br2 (* avg-age 600))))


