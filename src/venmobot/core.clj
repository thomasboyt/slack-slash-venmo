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
  (GET "/authed" [code] (oauth-callback code))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn -main []
  (run-jetty #'app-routes {:port 8080 :join? false}))
