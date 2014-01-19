(defproject venmobot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :main venmobot.core
  :ring {:handler venmobot.core/app-routes}
  :plugins [[lein-ring "0.8.10"]]
  :min-lein-version "2.3.4"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [org.clojure/java.jdbc "0.3.2"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [clj-http "0.7.8"]
                 [postgresql "9.1-901.jdbc4"]
                 [crypto-random "1.1.0"]
                 [org.clojure/data.json "0.2.4"]])
