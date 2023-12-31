(ns truss-interview.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import [java.time Duration ZoneId ZonedDateTime]
           [java.time.format DateTimeFormatter]))

(defn parse-timestamp [timestamp]
  (let [pacific-datetime-formatter (-> (DateTimeFormatter/ofPattern "M/d/yy h:mm:ss a")
                                       (.withZone (ZoneId/of "US/Pacific")))]
    (ZonedDateTime/parse timestamp pacific-datetime-formatter)))

(defn transform-timestamp [zoned-timestamp]
  (let [eastern-datetime-formatter (-> (DateTimeFormatter/ofPattern "M/d/yy h:mm:ss a")
                                       (.withZone (ZoneId/of "US/Eastern")))]
    (.format eastern-datetime-formatter zoned-timestamp)))

(defn transform-zip [zip]
  (format "%05d" (Integer/parseInt zip)))

(defn transform-full-name [name]
  (str/upper-case name))

(defn parse-duration [input]
  (let [[hours minutes seconds millis] (str/split input #"[:.]")]
    (-> (Duration/ofHours (Integer/parseInt hours))
        (.plusMinutes (Integer/parseInt minutes))
        (.plusSeconds (Integer/parseInt seconds))
        (.plusMillis (Integer/parseInt millis)))))

(defn transform-duration [duration]
  (.toSeconds duration))

(defn process-data-line [[timestamp
                          address
                          zip
                          full-name
                          foo-duration
                          bar-duration
                          total-duration
                          notes :as data-line]]
  (let [parsed-timestamp (parse-timestamp timestamp)
        parsed-foo-duration (parse-duration foo-duration)
        parsed-bar-duration (parse-duration bar-duration)
        computed-total-duration (.plus parsed-foo-duration parsed-bar-duration)]
    [(transform-timestamp parsed-timestamp)
     address
     (transform-zip zip)
     (transform-full-name full-name)
     (transform-duration parsed-foo-duration)
     (transform-duration parsed-bar-duration)
     (transform-duration computed-total-duration)
     notes]))

(defn -main [input-file-name output-file-name]
  (with-open [reader (io/reader input-file-name)
              writer (io/writer output-file-name)]
    (let [[column-headers & data-lines] (csv/read-csv reader)]
      (csv/write-csv writer [column-headers])
      (->> data-lines
           (map process-data-line)
           (csv/write-csv writer)))))
