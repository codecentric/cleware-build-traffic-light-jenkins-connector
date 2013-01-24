(ns btl.core
  (:gen-class :main :true)
  (:use [clojure.java.shell :only (sh)]
        [clojure.data.json :only (read-json)]
        [clojure.string :only (split join)])
  (:require [clj-http.client :as client]))

; Server specific configurations go here
(def jobs ["build" "sonar" "itests" "jbehave"])
(def jenkins-host "http://jenkins.example.com")
(def app "C:\\cleware\\USBswitchCmd.exe")
(def working-dir "C:\\cleware")

(def endpoint-url "%s/job/%s/lastBuild/api/json")
(def commands {
  :success (str app " G")
  :warning (str app " Y")
  :failure (str app " R")
  })
(def interval 3000)

(defn exec [cmd]
  (let [command (conj (split cmd #"\s+") :dir working-dir)
        result (apply sh command)
        exit-code (:exit result)]
       (if (not (= 0 exit-code))
           (println (format "Command \"%s\" failed (result code %s): %s"
                            (join " " command)
                            exit-code
                            (:err result))))))

(defn str-to-status [status]
  (cond (= status "SUCCESS") :success
        (= status "FAILURE") :failure
        :else :warning))

(defn get-last-status [job]
  (let [url (format endpoint-url jenkins-host job)
        response (client/get url)
        body (read-json (:body response))]
       (if (= (:building body) true) :warning (str-to-status (:result body)))))

(defn combine-status [first-status second-status]
  (cond (or (= first-status :failure) (= second-status :failure))
      :failure
    (or (= first-status :warning) (= second-status :warning))
      :warning
    :else
      :success))

(defn update-lights []
  (let [status (reduce combine-status (map get-last-status jobs))]
    (exec (status commands))))

(defn -main []
  (while true
    (update-lights)
    (Thread/sleep interval)))
