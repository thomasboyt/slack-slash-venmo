(ns venmobot.venmo
  (:require [venmobot.models.user :as user]
            [venmobot.slack :as slack]
            [clojure.string :as string]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(defn render-oauth-link
  [username token]
  (format "Hey, you need to <https://api.venmo.com/v1/oauth/authorize?client_id=%s&scope=make_payments&response_type=code&state=%s|authenticate> before you can send money!" (System/getenv "VENMO_CLIENT_ID") (user/insert-nonce! username token)))

(defn make-payment
  [username args channel]
  (slack/send-message channel
    (string/join ["[fake] @" username " just paid @" (args :recipient) " $" (args :amount) " for " (args :note)]))
  {:status 200})

(defn parse-args
  [text]
  (let [pieces (string/split text #"\s+")]
    ;; support either "@julian" or "julian"
    {:recipient (cond (= (first (pieces 0)) \@)
                      (subs (pieces 0) 1 (count (pieces 0)))
                      :else
                      (pieces 0))
     ;; TODO validate this bit
     :amount (pieces 1)
     ;; support either "julian 1.00 for foo" or "julian 1.00 foo"
     :note  (cond (= (pieces 2) "for")
                  (string/join " " (drop 3 pieces))
                  :else
                  (string/join " " (drop 2 pieces)))}))

(defn try-payment
  [{username "user_name" text "text" channel "channel_name"}]
  (if-let [args (parse-args text)]
    (make-payment username args channel)
    {:status 400 :body "Bad arguments"})
  (slack/send-message channel "Testing!")
  {:status 200})

;; TODO:
;; I need a way to auth incoming POST /venmo?username=..&token.. to confirm it's actually that user
;; This is so that I can't forge a request to associate the wrong account - i.e. I could say
;; username=Julian&token=whocares and associate my own account w/ Julian's Slack username
;;
;; Current token check does prevent other users from forging requests to make payments from an existing
;; user, but registration needs to be fixed!
 
(defn do-venmo-transaction
  [{form-params :form-params}]
  (if-let [paying-user (user/get-user (form-params "user_name") (form-params "token"))]
    (try-payment form-params)
    (render-oauth-link (form-params "user_name") (form-params "token"))))

(defn oauth-callback
  [{params :params}]
  (let [code (params "code")
        nonce (user/use-nonce! (params "state"))
        resp (client/post "https://api.venmo.com/v1/oauth/access_token"
               {:form-params {:client_id (System/getenv "VENMO_CLIENT_ID")
                              :client_secret (System/getenv "VENMO_CLIENT_SECRET")
                              :code code}})
        data (json/read-str (resp :body))
        user {:username (nonce :username)
              :slacktoken (nonce :slacktoken)
              :accesstoken (data "access_token")
              :refreshtoken (data "refresh_token")
              :venmoname ((data "user") "username")
              :venmoid ((data "user") "id")}]
    (user/add-user! user)
    {:body "Authenticated!"}))
