(ns venmobot.models.user
  (:use venmobot.db)
  (:require [clojure.java.jdbc :refer [query insert!]]))

(defn find-user-by-username
  [username]
  ; TODO: does this properly escape?
  (first (query db-spec ["SELECT * FROM users WHERE username = ?" username])))

(defn add-user!
  [user]
  (insert! db-spec :users user))
