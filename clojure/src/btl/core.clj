(ns btl.core
  (:gen-class :main :true)
  (:use [clojure.java.shell :only (sh)]
        [clojure.data.json :only (read-json)]
        [clojure.string :only (split join)])
  (:require [clj-http.client :as client]))

; Server specific configurations go here
(def jenkins-host "http://jenkins.example.com")
(def app "C:\\cleware\\USBswitchCmd.exe")
(def working-dir "C:\\cleware")
(def commands {0 (str app " G")
               1 (str app " Y")
               2 (str app " R")})
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

(defn get-command []
  (let [url (str jenkins-host "/api/json")
        response (client/get url)
        body (read-json (:body response))
        status (reduce max 0 (map #(get {"blue" 0
                                         "blue_anime" 0
                                         "disabled" 0
                                         "disabled_anime" 0
                                         "yellow" 1
                                         "yellow_anime" 1
                                         "red" 2
                                         "red_anime" 2}
                                        (:color %1))
                                  (:jobs body)))]
       (get commands status)))

(defn -main []
  (while true
    (exec (get-command))
    (Thread/sleep interval)))
