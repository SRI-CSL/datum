(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec :as s]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.reader :as reader]
            [com.sri.csl.datum.transform.datum :as datum]))

(insta/defparser parser (slurp (clojure.java.io/resource "grammar.bnf")))

(defn parse-datum [{:keys [text file line]}]
  (let [ast (parser text)
        result {:meta {:text text
                       :filename file
                       :line line}}]
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
  (let [{{:keys [text filename line]} :meta parse-error :parse-error} err]
    (println "File: " filename)
    (println "Line: " line)
    (println)
    (println parse-error)
    (println)))

(defn -main [f]
  (let [results (load-datums f)
        t-errors (filter :transform-error results)
        p-errors (filter :parse-error results)
        datums (remove (some-fn :transform-error :parse-error) results)]
    (println "Transform errors: " (count t-errors))
    (println "Parse errors: " (count p-errors))
    (println "Successful datums: " (count datums))))
