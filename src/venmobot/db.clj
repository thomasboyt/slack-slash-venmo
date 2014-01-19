(ns venmobot.db
  (:refer-clojure :exclude [replace reverse])
  (:use [clojure.string :as str])
  (:import (java.net URI)))

(defn heroku-db
  "Generate the db map according to Heroku environment when available."
  []
  (when (System/getenv "DATABASE_URL")
    (let [url (URI. (System/getenv "DATABASE_URL"))
          host (.getHost url)
          port (if (pos? (.getPort url)) (.getPort url) 5432)
          path (.getPath url)]
      (merge
       {:subname (str "//" host ":" port path)}
       (when-let [user-info (.getUserInfo url)]
         {:user (first (str/split user-info #":"))
          :password (second (str/split user-info #":"))})))))

(def db-spec
  (merge {:classname "com.mysql.jdbc.Driver"
          :subprotocol "postgresql"}
         (heroku-db)))
