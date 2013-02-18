(ns tweet-test.html
  (:require [tweet-test.tweets :as tweets]
            [hiccup.core :as hiccup]
            [hiccup.util :as util]))

(defn trend-html []
  (hiccup/html [:h1 "Trends:"]
               (for [x (tweets/trends-response)]
                 [:p 
                  [:a {:href (util/url "http://localhost:3000/tweets/" (util/url-encode x))} x]])))

(defn counter-generator []
  "Closure over a number that increments by one" 
  (let [count (atom 0)] 
    #(swap! count inc)))

(defn tweet-html [search-term]
  (def counter (counter-generator))
  (hiccup/html 
    [:h1 "Tweets"]
    (for [tweet-map (tweets/get-tweet-info (tweets/initial-tweets-request search-term))]
      [:div
       [:h2 (str "Tweet " (counter))]
       [:p (str tweet-map)]])))




