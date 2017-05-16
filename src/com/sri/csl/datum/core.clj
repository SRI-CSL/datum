(ns com.sri.csl.datum.core
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec :as s]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.transform.datum :as datum]))

(insta/defparser parser (slurp (clojure.java.io/resource "grammar.bnf")))

(def test-file (slurp (io/file (io/resource "examples.txt"))))
(def big-test-file (slurp (io/file (io/resource "6-evidence.txt"))))

(defn first-line? [line]
  (.startsWith line "  ***"))

(defn datum-line? [line]
  (.startsWith line "    ***"))

(def relevant-line?
  (some-fn datum-line? first-line?))

(defn relevant-lines [file]
  (filter relevant-line? (str/split-lines file)))

(defn extract-datums [lines]
  (let [counter (atom 0)]
    (map (partial str/join "\n")
         (partition-by
          (fn [line]
            (if (first-line? line)
              (swap! counter inc))
            @counter)
          lines))))

(defn parse-datum [text filename line]
  (let [ast (parser text)
        result {:meta {:text text
                       :filename filename
                       :line line}}]
    (if (insta/failure? ast)
      (assoc result :parse-error ast)
      (try
        (merge result (datum/datum ast))
        (catch Exception e
          (assoc result :transform-error e))))))

(defn parse-file [file]
  (pmap #(parse-datum % 1 1)
       (extract-datums (relevant-lines file))))

(defn display-parse-error [err]
  (-> err :meta :text println)
  (println)
  (-> err :parse-error println)
  (println))
