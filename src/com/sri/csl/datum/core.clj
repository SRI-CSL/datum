(ns com.sri.csl.datum.core
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec :as s]
            [clojure.pprint :refer [pprint]]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.examples :as examples]
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

(defn parse-file [file]
  (pmap parser
        (extract-datums (relevant-lines file))))

(defn make-test-datum [text]
  (let [ast (parser text)]
    (pprint ast)
    (-> ast
        (datum/datum text 1 1)
        (dissoc :meta)
        pprint)))

(make-test-datum examples/test-datum-2)
