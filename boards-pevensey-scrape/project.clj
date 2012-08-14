(defproject boards-pevensey-scrape "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [enlive "1.0.0"]
                 [com.clojars.jmeeks/clj-web-crawler "0.1.0-SNAPSHOT"]
                 [com.google.api.client/google-api-client "1.4.0-alpha"]
                 [noir "1.1.1-SNAPSHOT"]
                 [org.clojure/data.zip "0.1.1"]
                 ]
  :dev-dependencies [
    [appengine-magic "0.4.6"]
    [lein-ring "0.4.3"]]
  ;:appengine-app-versions {"test-appengine-site-dev" "2"
  ;                         "test-appengine-site-stage" "2"
  ;                         "test-appengine-site-production" "2"}
  :appengine-sdk {"me" "c:/Users/h/Downloads/eclipse-java-helios-SR2-win32-x86_64/eclipse/plugins/com.google.appengine.eclipse.sdkbundle_1.5.2.r36v201107211953/appengine-java-sdk-1.5.2"
                  })

 