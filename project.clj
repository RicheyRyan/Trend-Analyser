(defproject Trend-Analyser "0.1.1-SNAPSHOT"
  :description "A compojure based web app that analyses trend data from Twitter.
  Uses MongoDB for storage and an assortment of libraries to process the tweets."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [cheshire "5.0.1"]
                 [clj-http "0.6.3"]
                 [hiccup "1.0.2"]
                 [com.novemberain/monger "1.4.2"]
                 [overtone/at-at "1.1.1"]]
  :plugins [[lein-ring "0.8.0"]
            [lein-marginalia "0.7.1"]]
  :ring {:handler trend-analyser.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
