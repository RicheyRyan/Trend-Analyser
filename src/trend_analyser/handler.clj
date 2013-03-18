(ns trend-analyser.handler
  "Handles the incoming requests to server."
  (:use [compojure.core]
        [trend-analyser.tweets]
        [trend-analyser.html])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]))


(defroutes app-routes
  "Defines all the requests and paths. Index.html is returned at root.
  The documention is returned at /uberdoc.html.
  Trends and tweets can be queried by GET requests.
  If a route is not found then the simple text not found is displayed."
  (route/files "/")
  (route/files "/uberdoc.html")
  (GET "/trendshtml" [] (trend-html))
  (GET "/trends" [] (trends-json))
  (GET "/tweets/:trend" [trend] (tweet-json trend))
  (GET "/tweetshtml/:trend" [trend] (tweet-html trend))
  (GET "/tweetlisthtml/:trend" [trend] (tweet-list-html trend))
  (GET "/hashtags/:trend" [trend] (hashtag-json trend))
  (route/not-found "Not Found"))


(def app
  "Defines the app variable which compojure will use when running the app."
  (handler/site app-routes))


