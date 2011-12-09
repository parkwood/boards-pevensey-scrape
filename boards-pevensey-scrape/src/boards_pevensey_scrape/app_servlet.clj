(ns boards-pevensey-scrape.app_servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use boards-pevensey-scrape.core)
  (:use [appengine-magic.servlet :only [make-servlet-service-method]]))


(defn -service [this request response]
  ((make-servlet-service-method boards-pevensey-scrape-app) this request response))

;(require '[appengine-magic.core :as ae])
;(ae/serve boards-pevensey-scrape-app)
; (ae/stop)
