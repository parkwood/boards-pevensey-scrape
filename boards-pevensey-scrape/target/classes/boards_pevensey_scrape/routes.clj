(ns boards-pevensey-scrape.routes
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        scraper.enlive-test))

(defpartial layout [& content]
  (html5
    [:head
      [:title "my site"]]
    [:body
      [:div#wrapper
        content]]))

(defpage "/local-beaches" []  
  ;"<rss xmlns:atom=\"http://www.w3.org/2005/Atom\"><channel><title>my site</title><atom:link href=\"http://localhost:8080\" /><item><title>my site</title><link>www.yahoo.com</link></item></channel></rss>"
  (let [{:keys [title contents]} (scraper.enlive-test/find-topics-on)] ;{:title "UK windsurfing weather from August 30th", :contents '(["http://boards.mpora.com/forums/uk-windsurfing-weather-t59447p1.html" "Pevensey"])}];
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

       ;  [:guid {:isPermaLink "false"}  "http://localhost:8080"]
     ;  [:pubDate "Sat, 03 Sep 2011 09:51:55 GMT"] 
  ;(layout
  ;  [:h1 "Welcome to my site!"]
  ;  [:p "Hope you like it."]))