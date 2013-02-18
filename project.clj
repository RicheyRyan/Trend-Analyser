(defproject tweet-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [cheshire "5.0.1"]
                 [clj-http "0.6.3"]
                 [hiccup "1.0.2"]]
  :plugins [[lein-ring "0.8.0"]]
  :ring {:handler tweet-test.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
