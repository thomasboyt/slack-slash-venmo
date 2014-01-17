(ns venmobot.models.user
  :require [clojure.java.jdbc :refer [with-connection insert!]])

(defn get-user-by-name
  [name]
  )

(defn add-user!
  [& user]
  (insert! db-spec :users user))
