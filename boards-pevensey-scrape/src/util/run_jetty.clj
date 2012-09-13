(ns util.run-jetty
 (:require [boards-pevensey-scrape.core :as bps]
           [appengine-magic.core :as ae]) 
 )

(comment
  (do
            (require 'util.run-jetty)
            (in-ns 'util.run-jetty)))

(defn run-server []
  "convenience utility for running the server locally"
(ae/serve bps/boards-pevensey-scrape-app))