(ns boards-pevensey-scrape.model.reader
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.data.zip.xml :as zf]
            [appengine-magic.services.url-fetch :as fetch]))


(defn get-descriptions [zipper]
  (zf/xml-> zipper :channel :item :description zf/text))


(defn swjk-twitter-stream []
  (if-let [offline? (System/getProperty "offline")]
    (-> (slurp "resources/user_timeline.rss")
        .getBytes
        java.io.ByteArrayInputStream.)
    (fetch/fetch "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=swjk2&count=10000")))
