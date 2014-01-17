(ns venmobot.venmo
  (:require [venmobot.models.user :as user]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(defn render-oauth-link
  [username token]
  {:cookies {"slack_username" {:value username} "slack_token" {:value token}}
   :body (format "Hey, you need to authenticate with Venmo before you can send money! Click here: https://api.venmo.com/v1/oauth/authorize?client_id=%s&scope=make_payments&response_type=code" (System/getenv "VENMO_CLIENT_ID"))})
 
(defn do-venmo-transaction
  [{form-params :form-params}]
  ; first, look up current user in DB
  (if-let [paying-user (user/get-user (form-params "user_name") (form-params "token"))]
    ; Return link to oauth view
    {:body "alright, now make a payment"}
    (render-oauth-link (form-params "user_name") (form-params "token"))))

(defn oauth-callback
  [{params :params cookies :cookies}]
  (let [code (params "code")
        resp (client/post "https://api.venmo.com/v1/oauth/access_token"
             {:form-params {:client_id (System/getenv "VENMO_CLIENT_ID")
                            :client_secret (System/getenv "VENMO_CLIENT_SECRET")
                            :code code}})
        data (json/read-str (resp :body))
        user {:username ((cookies "slack_username") :value)
              :slacktoken ((cookies "slack_token") :value)
              :accesstoken (data "access_token")
              :refreshtoken (data "refresh_token")
              :venmoname ((data "user") "username")
              :venmoid ((data "user") "id")}]
    (user/add-user! user)
    {:body "Authenticated!"}))
