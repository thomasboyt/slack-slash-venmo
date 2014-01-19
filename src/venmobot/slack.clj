(ns venmobot.slack
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(def webhook-url (System/getenv "WEBHOOK_URL"))

(defn format-channel-name
  "Add a hash (#) to a channel name if it doesn't have one"
  [channel]
  (cond
    (not= (first channel) \#) (str "#" channel)
    :else channel))

(defn send-message
  [channel message]
  (let [json-message (json/write-str
                       {:text message 
                        :channel (format-channel-name channel)
                        :username "venmobot"
                        :parse "full"
                        ;; TODO: probably move this image off imgur/make it not fucking huge
                        "icon_url" "http://i.imgur.com/JknSeiD.png"})]
    (client/post webhook-url
                 {:body json-message :insecure? true})))
