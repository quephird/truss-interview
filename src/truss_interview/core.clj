(ns truss-interview.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn transform-zip [zip]
  (format "%05d" (Integer/parseInt zip)))

(defn process-data-line [[timestamp
                          address
                          zip
                          full-name
                          foo-duration
                          bar-duration
                          total-duration
                          notes :as line]]
  [timestamp
   address
   (transform-zip zip)
   full-name
   foo-duration
   bar-duration
   total-duration
   notes])

(defn -main [input-file-name output-file-name]
  (with-open [reader (io/reader input-file-name)
              writer (io/writer output-file-name)]
    (let [[column-headers & data-lines] (csv/read-csv reader)]
      (csv/write-csv writer [column-headers])
      (->> data-lines
           (map process-data-line)
           (csv/write-csv writer)))))
