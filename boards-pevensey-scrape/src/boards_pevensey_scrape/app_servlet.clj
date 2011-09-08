(ns boards-pevensey-scrape.app_servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use boards-pevensey-scrape.core)
  (:use [appengine-magic.servlet :only [make-servlet-service-method]]))


(defn -service [this request response]
  ((make-servlet-service-method boards-pevensey-scrape.core/boards-pevensey-scrape-app) this request response))

