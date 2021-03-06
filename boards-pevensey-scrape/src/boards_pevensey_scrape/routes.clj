(ns boards-pevensey-scrape.routes
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        [boards-pevensey-scrape.model.scrape :as scrape]
        [boards-pevensey-scrape.model.reader :as swjk2]
        )
  (:require
   [noir.response :as response :only json]
   [appengine-magic.services.mail :as mail]))


(def local-beaches-regex #"[Pp]evensey|[Cc]ooden|[Pp]osh|[Cc]amber")

(defpage "/local-beaches" []  
  (let [{:keys [title contents]} (scrape/find-topics-on local-beaches-regex)] 
  (str (xml-declaration "utf-8")
    "<?xml-stylesheet title=\"XSL_formatting\" type=\"text/xsl\" href=\"/shared/bsp/xsl/rss/nolsol.xsl\"?>"   
  (html
    [:rss {:xmlns:atom "http://www.w3.org/2005/Atom" :xmlns:media "http://search.yahoo.com/mrss/" :version "2.0"}
     [:channel
      [:title title]
      [:link "http://localhost:8080"]
      [:atom:link {:href "http://localhost:8080"} ]
      (for [[link txt] contents] [:item [:title txt ] [:description txt][:link link]])
     ]])))  
  )

(defpage "/words" []
;  (html (swjk2/swjk-twitter-stream))
  (let [stream (swjk2/swjk-twitter-stream)
        word-list (-> stream  swjk2/as-parsed-xml  swjk2/get-descriptions swjk2/word-to-expln )]
    (response/json word-list))) 

(defpage "mail-words" []
  (let [ msg (mail/make-message :from "one@example.com"
                               :to "henrywidd@yahoo.com"                               
                               :subject "twitwords"
                               :text-body "Sent from appengine-magic.")]
    (mail/send msg)
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "sent"}))