(ns boards-pevensey-scrape.model.reader
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [clojure.data.zip.xml :as zf]
            [appengine-magic.services.url-fetch :as fetch]))

(def patterns [#"swjk2: (Definition of [^:]+):(.+)" #"swjk2: (.+) - (.+)"  #"swjk2: (.+) (.+)"])

(defn word-to-expln [descriptions]
  (map
   #(if-let [found (some (fn [pattern] (re-matches pattern %))  patterns)]
     [ (second found) (second (rest found))] [nil nil]
            )
    descriptions) )

(defn get-descriptions [zipper]
  (zf/xml-> zipper :channel :item :description zf/text))

(defn as-parsed-xml [stream]
  (zip/xml-zip  (xml/parse stream)))

(defn swjk-twitter-stream []
  (if-let [offline? (Boolean/valueOf (or  (System/getProperty "offline") "false")) ]
    (-> (slurp "resources/user_timeline.rss")
        .getBytes
        java.io.ByteArrayInputStream.)
    (fetch/fetch "http://api.twitter.com/1/statuses/user_timeline.rss?screen_name=swjk2&count=10000")))
;(System/setProperty "offline", "true")

