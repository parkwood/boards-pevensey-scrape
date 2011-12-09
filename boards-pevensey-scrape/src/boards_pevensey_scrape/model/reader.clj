(ns boards-pevensey-scrape.model.reader
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.contrib.zip-filter.xml :as zf]))


(defn get-descriptions [zipper]
  (zf/xml-> zipper :channel :item :description zf/text))

(defn get-ids []
  (zip/xml-zip (xml/parse "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=swjk2&count=10000")))
