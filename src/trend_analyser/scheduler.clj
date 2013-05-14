(ns trend-analyser.scheduler
  "This namespace provides utilities for creating timers to run a task periodically."
  (:import (java.util.concurrent ScheduledThreadPoolExecutor TimeUnit)))

(def ^:private num-threads 1)
(def ^:private pool (atom nil))

(defn- thread-pool 
  "Creates a pool of threads to execute jobs on."
  []
  (swap! pool (fn [p] (or p (ScheduledThreadPoolExecutor. num-threads)))))

(defn periodically
 	  "Schedules function f to run every 'delay' milliseconds after a
 	  delay of 'initial-delay'."
 	  [f initial-delay delay]
 	  (.scheduleWithFixedDelay (thread-pool)
 	                           f
 	                           initial-delay delay TimeUnit/MILLISECONDS))

(defn shutdown
  "Terminates all periodic tasks."
  []
  (swap! pool (fn [p] (when p (.shutdown p)))))

