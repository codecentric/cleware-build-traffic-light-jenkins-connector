(ns btl.core
  (:gen-class :main :true)
  (:use [clojure.java.shell :only (sh)]
        [clojure.data.json :only (read-json)]
        [clojure.string :only (split join)])
  (:require [clj-http.client :as client]))

; Server specific configurations go here
(def commit-job-name "build")
(def sonar-job-name "sonar")
(def jenkins-host "http://jenkins.example.com")
(def app "C:\\cleware\\USBswitchCmd.exe")
(def working-dir "C:\\cleware")

(def endpoint-url "%s/job/%s/lastBuild/api/json")
(def reset-command (str app " 0"))
(def success-command (str app " G"))
(def warning-command (str app " Y"))
(def failure-command (str app " R"))
(def interval 5000)

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
        response (client/get url)]
       (str-to-status (:result (read-json (:body response))))))

(defn update-lights []
  (let [commit-job-status (get-last-status commit-job-name)
        sonar-job-status (get-last-status sonar-job-name)]
       (exec reset-command)
       (cond (and (= commit-job-status :success) (= sonar-job-status :success))
               (exec success-command)
              (= commit-job-status :failure)
                (exec failure-command)
              :else
                (exec warning-command))))

(defn -main []
  (while true
    (update-lights)
    (Thread/sleep interval)))