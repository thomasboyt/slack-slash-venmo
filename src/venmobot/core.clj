(ns venmobot.core
  (:use compojure.core
        venmobot.venmo
        ring.adapter.jetty
        ring.middleware.params) 

  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello World")

  (POST "/venmo" [] (wrap-params do-venmo-transaction))
  (GET "/authed" [] (wrap-params oauth-callback))

  (route/resources "/")
  (route/not-found "Not Found"))

(defn start [port]
  (run-jetty #'app-routes {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt
               (or (System/getenv "PORT") "8080"))]
  (start port)))
