(ns trend-analyser.html
  "This name space is primarily for testing purposes.
   The HTML returned is used to inspect the data and check that
   the values are as expected."
  (:require [trend-analyser.tweets :as tweets]
            [hiccup.core :as hiccup]
            [hiccup.util :as util]))

(defn trend-html 
  "Returns the trends which link to a page where the tweets related to that trend are dumped."
  []
  (hiccup/html [:h1 "Trends:"]
               (for [x (tweets/trends-response)]
                 [:p 
                  [:a {:href (util/url "http://localhost:3000/tweetshtml/" (util/url-encode x))} x]])))

(defn counter-generator 
  "Generates a closure over a number that increments by one each time the function is called." 
  []
  (let [count (atom 0)] 
    #(swap! count inc)))

(defn tweet-html 
  "Loops through are the returned tweets. The closure is used to give each tweet a numbered heading.
  The actual tweet data is printed below the heading."
  [search-term]
  (def counter (counter-generator))
  (hiccup/html 
    [:h1 "Tweets"]
    (for [tweet-map (tweets/raw-tweets-from-db)]
      [:div
       [:h2 (str "Tweet " (counter))]
       [:p (str tweet-map)]])))


(defn tweet-list-html 
  "Simply prints out all the tweets with no separation. Useful for seeing what data structure is being used to contain
  a list of tweets."
  [search-term]
  (def counter (counter-generator))
  (hiccup/html 
    [:h1 "Tweets"]
      [:div
       [:h2 (str "Tweet " (counter))]
       [:p (tweets/get-tweet-info (tweets/initial-tweets-request search-term))]]))




;;(tweets/raw-tweets-from-db)