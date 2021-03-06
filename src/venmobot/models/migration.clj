(ns venmobot.models.migration
  (:use venmobot.db)
  (:require [clojure.java.jdbc :refer [db-do-commands create-table-ddl]]))

(defn create-venmobot []
  (db-do-commands db-spec
    (create-table-ddl :users
                      [:username :text "PRIMARY KEY"]
                      [:slacktoken :text "NOT NULL"]
                      [:venmoname :text "NOT NULL"]
                      [:venmoid :text "NOT NULL"]
                      [:accesstoken :text "NOT NULL"]
                      [:refreshtoken :text "NOT NULL"])
    (create-table-ddl :nonces
                      [:username :text "NOT NULL"]
                      [:slacktoken :text "NOT NULL"]
                      [:nonce :text "PRIMARY KEY"])))

(defn -main []
  (print "Creating database...") (flush)
  (create-venmobot)
  (println "Done"))
