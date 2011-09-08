(ns boards-pevensey-scrape.core
  (:require [appengine-magic.core :as gae]
	    [noir.util.gae :as noir]))

;; load all of the routes (defpage's) so they get def'd
(require 'boards-pevensey-scrape.routes)

;; def the appengine app
(gae/def-appengine-app boards-pevensey-scrape-app (noir/gae-handler nil))