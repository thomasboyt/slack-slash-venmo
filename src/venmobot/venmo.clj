(ns venmobot.venmo
  (:require [venmobot.models.user :as user]))

(defn do-venmo-transaction
  [{form-params :form-params}]
  ; first, look up current user in DB
  (let [existing-user (user/get-user-by-username (form-params :username))]
    ; Return link to oauth view
    (pr-str existing-user)))

