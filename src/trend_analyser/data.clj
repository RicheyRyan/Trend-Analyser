(ns trend-analyser.data
  "This namespace handles interaction with MongoDB. It ensures that data is in an appropriate format for insertion and
  that it is returned in an appropriate format."
  (:require [monger.core :as mg]
            [monger.collection :as coll]
            [monger.json :as json]
            [monger.result :as result]
            [monger.util :as mutil])
	(:import [com.mongodb MongoOptions ServerAddress]
           [org.bson.types ObjectId]))

;;This handles connecting to MongoDB via port 27017 on localhost.
(mg/connect!)

;;Once connected this function instructs MongoDB to use the database "Trend-Analyser".
(mg/use-db! "trend-analyser")

;;A function to check whether any trends are currently stored in the trends collection.
(defn db-has-trends? []
  (coll/any? "trends"))


(defn db-has-raw-tweets? 
  "A function to check whether any raw tweets are currently stored in the raw tweets collection.
  Raw tweets refer to tweets that have been parsed to contain the max amount of data that we want. They will
  be refined further."
  []
   (coll/any? "raw-tweets"))

(defn insert-trends 
  "This function removes any existing trend data from the database and inserts new trend data. It formats the trends in
   hashmap with an automatically generated id."
   [insert]
    (coll/insert "trends" (into {} (merge {:_id (mutil/object-id) :date (str(java.util.Date.))} insert)))
    nil)

(comment (defn insert-trends [insert]
     (if (result/ok? (coll/remove "trends"))
       (if (result/ok? (coll/insert "trends" (into {} (merge {:_id (mutil/object-id) :date (str(java.util.Date.))} insert))))
         true
         (throw (Exception. "Failed to add trends into db.")))
       (throw (Exception. "Failed to remove trends from db.")))))

(defn retrieve-trends 
  "Retrieves the trends from the database. Once they are retrieved they are removed from the hashmap.
   This function returns a seq with the names of the trends in it."
   []
    (first
      (coll/find-maps "trends")))

(defn format-raw-tweets 
  "A helper function to ensure tweets are correctly formatted to go in the database.
   Places the tweets in the database in a hashmap, with an automatically generated id
   and a reference to the trend it refers to."
   [tweets, trend]
        (for [tweet tweets]
          (into {} (merge {:_id (ObjectId.)} {:trend trend} tweet))))

(defn insert-raw-tweets 
  "Inserts the raw tweets into the rawtweets collection."
  [insert, trend]
  (coll/insert "rawtweets" (format-raw-tweets insert trend)))

(defn retrieve-raw-tweets 
  "Rettrieves the raw tweets from the rawtweets collection."
  []
  (first
    (coll/find-maps "rawtweets")))
