(ns boards-pevensey-scrape.model.scrape
  (:require [net.cgrand.enlive-html :as html])
  (:require [net.cgrand.xml :as xml]))

(def host "http://boards.mpora.com")
(def looking-ahead-forum "/forums/looking-ahead-f6.html")
 
; required to avoid endless redirects
(java.net.CookieHandler/setDefault (java.net.CookieManager. nil java.net.CookiePolicy/ACCEPT_ALL))

(defn get-stream-from-url "return empty string in case of connection problem" [s]
  (try
 (.openStream (java.net.URL. s))
 (catch Exception e ""))
 )

(defn get-all-threads-starting [thread-root-name enlivened-list]
  (html/select enlivened-list [[:a (html/attr-starts :href thread-root-name)]]))
   
(defn get-first-and-last-pages-of-uk-weather-threads [enlivened-list]
  (if-let [[title-thread & threads] (get-all-threads-starting "uk" enlivened-list)]
    (if-let [last-page (first(filter #(= "Last Page" (html/text %)) threads))]
      {:title (html/text title-thread) 
       :root-link ((title-thread :attrs) :href) 
       :last-page ((last-page :attrs) :href) }               ) 
  ))
  
(defn match-page [topic-regex root-link page-number]
    (let [page-url (str "/forums/" root-link "p" page-number ".html") 
          [match] (re-seq topic-regex  (apply str (line-seq (clojure.java.io/reader(get-stream-from-url (str host page-url))))))]
      (if match [(str host page-url) match])
    ))
  
(defn apply-matching-to-each-page-of-thread
    [topic-regex root-link last-page pages-containing pages-numbers]
    (let [topic-pages (pmap (partial match-page topic-regex root-link) pages-numbers)]
      (filter (fn [x](not (nil? x))) topic-pages)))

(defn get-pages-containing
    [topic-regex root-link last-page] 
      (if-let [[max-pages-match] (re-seq #"p(\d+).html" last-page)] 
        (apply-matching-to-each-page-of-thread 
          topic-regex root-link last-page [] (map inc (range (Integer/parseInt (last max-pages-match)))))))
    
(defn get-thread-id-minus-page-number [root-link]
    "strips off the .html part of a link, to give the common root"
    (subs root-link 0 (- (count root-link) 5)))
  
(defn enliven-string [s]
  (html/html-resource s))
  
(defn find-topics-on [local-beaches]
    (if-let [{:keys [title root-link last-page]} (get-first-and-last-pages-of-uk-weather-threads 
                                                   (enliven-string(get-stream-from-url (str host looking-ahead-forum))))]
       (if-let [topic-pages (get-pages-containing local-beaches 
                                                  (get-thread-id-minus-page-number root-link) 
                                                  last-page)]
          {:title title :contents topic-pages}
          {:title title :contents {(str host looking-ahead-forum) "No discussion of local beaches found"} }         )
       {:title "Boards Forum Unavailable" :contents {}}
      ))
  