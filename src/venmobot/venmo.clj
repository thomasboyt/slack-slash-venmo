(ns venmobot.venmo
  (:require [venmobot.models.user :as user]))

(defn render-oauth-link
  []
  (format "Hey, you need to authenticate with Venmo before you can send money! Click here: https://api.venmo.com/v1/oauth/authorize?client_id=%s&scope=make_payments" (System/getenv "VENMO_CLIENT_ID")))
 
(defn do-venmo-transaction
  [{form-params :form-params}]
  ; first, look up current user in DB
  (if-let [existing-user (user/get-user-by-username (form-params :username))]
    ; Return link to oauth view
    "alright, now make a payment"
    (render-oauth-link)))

(defn oauth-callback
  [code])

