(ns boards-pevensey-scrape.test.model.scrape
  (:use [boards-pevensey-scrape.model.scrape])
  (:use [clojure.test]))

(deftest test-sensible-results-returned-when-forum-page-not-available
  (is (= {:title "Boards Forum Unavailable" :contents {}}
          (binding [get-stream-from-url (fn [s] "")]
            (find-topics-on #"beach-name"))
          )
      )
  )

(deftest test-sensible-results-returned-when-one-of-the-thread-pages-errors
  (is (= {:title "thread title" :contents {(str host looking-ahead-forum) "No discussion of local beaches found"} }
        (binding [get-first-and-last-pages-of-uk-weather-threads (fn [x] {:title "thread title" :root-link "http://root1.html" :last-page "http://root11.html" })]
          (find-topics-on #"beach-name")
         )
       )
   )
)




