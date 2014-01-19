(ns venmobot.models.user
  (:use venmobot.db)
  (:require [clojure.java.jdbc :refer [query insert! delete!]]
            [crypto.random :as random]))

;; Users
(defn get-user-with-token
  [username token]
  (first (query db-spec ["SELECT * FROM users WHERE username = ? AND slacktoken = ?" username token])))

(defn get-user
  [username]
  (first (query db-spec ["SELECT * FROM users WHERE username = ?" username])))

(defn add-user!
  [user]
  (insert! db-spec :users user))

;; Nonces - used to hold state when authenticating as an alternative to cookies/sessions
;; Doing /venmo for the first time -> link is created to oauth with ?state=nonce
;; Then, when authenticated, lookup nonce for stored username/token and delete
;; TODO: Add created timestamp & script to clear out nonces that are >1h old

(defn insert-nonce!
  [username token]
  (-> (insert! db-spec :nonces {:username username
                       :slacktoken token
                       :nonce (random/url-part 16)})
      (first) :nonce))

(defn use-nonce!
  [nonce-value]
  (when-let [nonce (first (query db-spec ["SELECT * FROM nonces WHERE nonce = ?" nonce-value]))]
    (delete! db-spec :nonces ["nonce = ?" nonce-value])
    nonce))
