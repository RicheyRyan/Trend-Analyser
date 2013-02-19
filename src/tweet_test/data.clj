(ns tweet-test.data
  (:require [monger.core :as mg]
            [monger.collection :as coll]
            [monger.json :as json])
	(:import [com.mongodb MongoOptions ServerAddress]))

(comment (mg/connect!)
(mg/use-db! "trend-analyser")

(defn db-is-not-empty? [] (str (coll/any? "trend-analyser")))

(defn insert-trends [trends]
  (coll/update "trends" trends :upsert true :multi true)))