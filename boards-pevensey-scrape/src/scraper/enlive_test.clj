(ns scraper.enlive-test
  (:require [net.cgrand.enlive-html :as html])
  (:require [net.cgrand.xml :as xml])
  ;(:use clj-web-crawler)
  (:require [clojure.contrib.http.connection :as conn])
  )

;(java.net.CookieHandler/setDefault (guestbook.CookieManager. nil guestbook.CookiePolicy/ACCEPT_ALL))
;http://www.java2s.com/Open-Source/Java-Document/6.0-JDK-Core/net/java/net/CookieManager.java.htm

(def thread-root-name "uk");-windsurfing-weather")
(def host "http://boards.mpora.com")
(def local-beaches #"[Pp]evensey|[Cc]ooden|[Pp]osh")
(def looking-ahead-forum "/forums/looking-ahead-f6.html")
;(in-ns 'enlive-test)

;(-> "http://www.penny-arcade.com/comic/" URL. html-resource 
;  (select [:.body :img]) first :attrs :src) 
(defn get-forum-stream [s]
 ; (crawl-response host "/forums/looking-ahead-f6.html")
 ;(.getContent(conn/http-connection (str host s)))
 (.openStream (java.net.URL. s))
 )

(defn enliven-string [s]
  (html/html-resource s)) ;(StringBufferInputStream.
   
  (defn forum-thread-starting [enlivened-list]
  (if-let [[title-thread & threads] (html/select enlivened-list [[:a (html/attr-starts :href thread-root-name)]])]
    (if-let [last-page (first(filter #(= "Last Page" (html/text %)) threads))]
      {:title (html/text title-thread) :root-link ((title-thread :attrs) :href) :last-page ((last-page :attrs) :href) }
                ) 
  ))
  (defn match-page [topic-regex root-link page-number]
    (let [page-url (str "/forums/" root-link "p" page-number ".html") 
          [match] (re-seq topic-regex  (apply str (line-seq (clojure.java.io/reader(get-forum-stream (str host page-url))))))]
      ;(do (prn host page-url match)
      (if match [(str host page-url) match])
    ))
  
  (defn get-pages-containing 
    ([topic-regex root-link last-page] 
      (if-let [[max-pages-match] (re-seq #"p(\d+).html" last-page)] 
        (get-pages-containing 
          topic-regex root-link last-page [] (map inc (range (Integer/parseInt (last max-pages-match)))))))
    ([topic-regex root-link last-page pages-containing pages-numbers]
    (let [topic-pages (map (partial match-page topic-regex root-link) pages-numbers)]
      (filter (fn [x](not (nil? x))) topic-pages) 
      )))

  (defn find-topics-on []
    (if-let [{:keys [title root-link last-page]} (forum-thread-starting (enliven-string(get-forum-stream (str host looking-ahead-forum))))]
       (if-let [topic-pages (get-pages-containing local-beaches (subs root-link 0 (- (count root-link) 5)) last-page)]
          {:title title :contents topic-pages}
          {:title "no threads found" }
         )
      ))
  
  ;(find-topics-on)