(ns scraper.test-scrape
 (:use clj-web-crawler) 
)

; The crawl macro also accepts a client, a method and a body. 
; In the body of crawl, you can examine various things like 
; the status code, the response, etc.
(let [clj-ws (client "http://www.clojure.org")
      home  (method "/")] 
  (crawl clj-ws home
    (println (.getStatusCode home))  
    (println (response-str home))))  

; If you need to login to a website you can do that too and
; verify any cookies are set to validate the login
(let [site (client "http://boards.mpora.com/")
      forum (method "/forums/looking-ahead-f6.html" :get )] 
  (crawl site forum 
    (pprint (cookies site))))

(defn find-pevensey-posts []
  (let [looking-ahead (crawl-response "http://boards.mpora.com/forums/looking-ahead-f6.html")
        threads (re-seq #{"UK windsurfing weather"}) ]
    
    ))

;http://boards.mpora.com/forums/looking-ahead-f6.html
;http://mpora.com/usercp/login/?login=Log%20In&password=viper1&return_url=http%3A%2F%2Fmpora.com%2F&username=swjk
;http://mpora.com

(let [site (client "http://mpora.com/")
      forum (method "/usercp/login/?do=login&password=viper1&return_url=http%3A%2F%2Fboards.mpora.com%2Fforums%2F&s=&securitytoken=guest&username=swjk&vb_login_md5password=&vb_login_md5password_utf=" :post )] 
  (crawl site forum 
     (pprint (cookies site))    ))
; the crawl-response function is for quick and dirty things and just returns the 
; response as a string from the server
(println (crawl-response "http://www.clojure.org"))

; prints the HTML of the clojure.org/api page and always defaults to a GET request
(println (crawl-response "http://www.clojure.org" "/api"))

; you can also pass in a real method object 
(println (crawl-response "http://www.example.com" (method "/do_login" :post {:login "me"}))
