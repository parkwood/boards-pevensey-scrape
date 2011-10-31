(ns util.run-jetty
 (:require [boards-pevensey-scrape.core :as bps]
           [appengine-magic.core :as ae]) 
  )

(defn run-server []
  "convenience utility for running the server locally"
(ae/serve bps/boards-pevensey-scrape-app))