(ns tweet-test.handler
  (:use [compojure.core]
        [tweet-test.tweets]
        [tweet-test.html])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :as resp]))

(defroutes app-routes
  (route/files "/" {:root (str (System/getProperty "user.dir") "/public")})
  (GET "/trends" [] (trend-html))
  (GET "/tweets/:trend" [trend] (tweet-html [trend]))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
