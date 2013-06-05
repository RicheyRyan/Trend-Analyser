# Trend Analyser

An application to process tweet data data from Twitter using Clojure.

## Goal

To create an application that retrieves the latest trends for Ireland from Twitter. It retrieves the tweets related to each of the trends. The tweets are ordered in terms of popularity and displayed to the user.

![Structure of App](https://dl.dropboxusercontent.com/u/13548247/trend-analyser/structure.png)

The application was used as means to learn about Clojure, functional programming and all the tools necessary to create a web application. These tools include the Clojure libraries Compojure, Cheshire, Monger and clj-http.

## Structure

The handler namespace handles all the requests to the application and defines the routes for the application. It uses functions from the other namespaces to provide appropriate functionality.

The tweets namespace handles all Twitter interaction. It sends GET requests to the Twitter API using clj-http. The response received is parsed using Cheshire to convert the JSON into a Clojure map. The tweet information is parsed, extracting the information that is relevant such as the tweet id. The tweet or trend information can be serialised to JSON, inserted to the database or extracted from the database. There is also a mechanism to make timed queries to Twitter.

The data namespace uses the Monger library to interact with a MongoDB database. The tweet maps are given an ID and a data and are inserted into the database. They are retrieved using the find-maps function. 

The scheduler namespace handles timed requests to Twitter. It uses the Java class ScheduledThreadPoolExecutor from the concurrency package. It was a good opportunity to use Java interoperation. Using it to make timed updates to the database proved difficult and as a result the timed queries do not work fully.

The html namespace is used to print out HTML representations of data retrieved from Twitter using the Hiccup library. This namespace was mainly used for debugging purposes. 

## Documentation

The documented code for the project can be found at the link below

http://www.richeyryan.com/trend-analyser/

## License

Copyright 2013 Richey Ryan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. Copyright © 2013 Richey Ryan
