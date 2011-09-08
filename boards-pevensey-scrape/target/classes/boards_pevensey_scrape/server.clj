;not used anymore. just keeping it around in case
(ns boards-pevensey-scrape.server
  (:require [noir.server :as server]))

(server/load-views "src/boards_pevensey_scrape/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'boards-pevensey-scrape})))
