(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [instaparse.core :as insta]
            [instaparse.failure :as fail]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec :as s]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.reader :as reader]
            [com.sri.csl.datum.transform.datum :as datum]))

(insta/defparser parser (slurp (clojure.java.io/resource "grammar.bnf")))

(defn parse-datum [datum]
  (let [ast (parser (:text datum))
        result {:meta datum}]
    (if (insta/failure? ast)
      (assoc result :parse-error ast)
      (try
        (merge result (datum/datum ast))
        (catch Exception e
          (assoc result :transform-error e))))))

(defn load-datums [file]
  (->> file
       reader/extract
       (pmap parse-datum)))

(defn display-parse-error [err]
  (let [{:keys [file line]} (:meta err)
        {parse-line :line
         parse-column :column
         parse-text :text} (:parse-error err)
        true-line (+ line (dec parse-line))]
    (println "File:" file)
    (println (str "Line " true-line ", column " parse-column ":"))
    (println parse-text)
    (println (fail/marker parse-column))
    (println)))

(defn -main [f]
  (let [results (load-datums f)
        t-errors (filter :transform-error results)
        p-errors (filter :parse-error results)
        datums (remove (some-fn :transform-error :parse-error) results)]
    (println "Successful datums: " (count datums))
    (println "Transform errors: " (count t-errors))
    (println "Parse errors: " (count p-errors))
    (println)
    (doall (map display-parse-error p-errors))
    (System/exit 0)))
