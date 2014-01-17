(ns venmobot.models.user
  (:use venmobot.db)
  (:require [clojure.java.jdbc :refer [query insert!]]))

(defn get-user
  [username token]
  ; TODO: does this properly escape?
  (first (query db-spec ["SELECT * FROM users WHERE username = ? AND slacktoken = ?" username token])))

(defn add-user!
  [user]
  (insert! db-spec :users user))

;(defn update-user!
;  [user]
;  ())
