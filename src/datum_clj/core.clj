(ns datum-clj.core
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(def parser (insta/parser (clojure.java.io/resource "grammar.bnf")))

(def test-file (slurp (io/file (io/resource "examples.txt"))))
(def big-test-file (slurp (io/file (io/resource "6-evidence.txt"))))
(defn first-line? [line]
  (.startsWith line "  ***"))
(defn datum-line? [line]
  (.startsWith line "    ***"))
(defn relevant-line [line]
  (or (datum-line? line)
      (first-line? line)))
(defn relevant-lines [file]
  (filter relevant-line (str/split-lines file)))
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

(def test-datum "  *** Nfkb-reporter[Luc] is increased irt (PMA + Ionomycin) (12 hr)
    *** cells: DT40<RacGap1~null><xRacGap1> in BMLS
    *** unaffected by: xRacGap1(K182A/R183A/R184A) [substitution]
    *** unaffected by: xRacGap1(K199A/K200A) [substitution]
    *** unaffected by: xRacGap1(K182A/R183A/R184A/K199A/K200A) [substitution]
    *** source: 19158271-Fig-4c")

(def test-datum-2 "  *** Tp53[Ab] prot-exp[WB] is increased irt (UV + TrichostatinA) (times)
    *** cells: A549 in BMS
    *** times: 0 2 4+ 8+ 24+ hr
    *** comment: new protein is acetylated on K382 [KAcAb]
    *** comment: new protein is acetylated on K320 [KAcAb]
    *** comment: new protein is phosed on S37 [phosAb]
    *** source: 9744860-Fig-7")
