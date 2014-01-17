(ns venmobot.core
  (:use compojure.core
        venmobot.venmo
        ring.adapter.jetty
        ring.middleware.params
        ring.middleware.cookies) 

  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (POST "/venmo" [] (wrap-cookies (wrap-params do-venmo-transaction)))
  (GET "/authed" [] (wrap-cookies (wrap-params oauth-callback)))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn -main []
  (run-jetty #'app-routes {:port 8080 :join? false}))
