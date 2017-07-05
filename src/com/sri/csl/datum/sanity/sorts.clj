(ns com.sri.csl.datum.sanity.sorts
  (:require
   [com.sri.csl.datum.sanity.check :as check :refer [path-end check-or eq-check check-and]]
   [com.sri.csl.datum.ops :as ops]
   [clojure.string :as str]))

(defn sort-check [& sorts]
  (fn [datum path node]
    (when-not (some #(ops/check-op % node) sorts)
      (str node " is not a known " (str/join "/" sorts)))))

(defn simple-sort [label & sorts]
  [(path-end [label])
   (apply sort-check sorts)])

(defn ivlka-check [datum path node]
  (when (= (get-in datum [:assay :assay]) "IVLKA")
    (when-not (ops/check-op "Lipid" node)
      "IVLKA substrates must be Lipids.")))

(def checkers
  [(simple-sort :protein "Protein" "Peptide" "Composite")
   (simple-sort :chemical "Chemical")
   (simple-sort :gene "Gene")
   (simple-sort :handle "Handle")

   [(path-end [:assay :assay])
    (sort-check "AssayType")]

   (simple-sort :detect "DetectionMethod")

   [(path-end [:position :assay])
    (sort-check "Position")]

   (simple-sort :fraction "Fraction")

   [(path-end [:cells])
    (check-or (sort-check "Cells")
              (eq-check "none"))]

   (simple-sort :medium "Medium")
   (simple-sort :mutation-type "Mutation")

   [(path-end [:symbol :_ :mutations])
    (sort-check "Mutation")]

   [(path-end [:symbol :_ :modifications])
    (sort-check "Modification")]

   [(path-end [:_ :substrates])
    (check-and
     (sort-check "Substrate")
     ivlka-check)]

   [(path-end [:_ :tests])
    (sort-check "Ktest")]

   (simple-sort :unit "TimeUnit")])
