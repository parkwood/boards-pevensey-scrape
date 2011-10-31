(ns boards-pevensey-scrape.routes
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        [boards-pevensey-scrape.model.scrape :as scrape]))


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
