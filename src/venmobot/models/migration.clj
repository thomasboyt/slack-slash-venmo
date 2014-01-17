(ns venmobot.models.migration
  (:require [clojure.java.jdbc :refer [db-do-commands create-table-ddl]]))

(def db
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/venmobot"})

(defn create-venmobot []
  (db-do-commands db
    (create-table-ddl :users
                      [:username :text "PRIMARY KEY"]
                      [:venmoname :text "NOT NULL"]
                      [:venmoid :text "NOT NULL"]
                      [:accesstoken :text "NOT NULL"]
                      [:refreshtoken :text "NOT NULL"])))

(defn -main []
  (print "Creating database...") (flush)
  (create-venmobot)
  (println "Done"))
