(ns com.sri.csl.datum.sanity.sorts
  (:require
   [com.sri.csl.datum.sanity.check :as check]
   [com.sri.csl.datum.ops :as ops]
   [clojure.string :as str]))

(defn sort-check [& sorts]
  (fn [datum path node]
    (when-not (some #(ops/check-op % node) sorts)
      (str node " is not a known " (str/join "/" sorts)))))

(defn simple-sort [label & sorts]
  [(check/postfix [label])
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

   (simple-sort :antibody "Antibody")
   (simple-sort :stress "Stress")
   (simple-sort :pathogen "Pathogen")

   [(check/postfix [#{:antibody :stress} :_ :!treatments])
    (check/error "Antibodies and Stresses are only valid as treatments.")]

   [(check/postfix [:gene :_ :treatments])
    (check/error "Genes are not valid treatments.")]

   [(check/postfix [:oligo :_ :!hooks])
    (check/error "oligo only valid as a hook.")]

   [(check/postfix [:assay :assay])
    (sort-check "AssayType")]

   (simple-sort :detect "DetectionMethod")

   [(check/postfix [:_ :sites])
    (check/check-or
     (check/regex #"[ACDEFGHIKLMNPQRSTVWY]\d+")
     (sort-check "Site"))]

   [(check/postfix [:position :assay])
    (sort-check "Position")]

   (simple-sort :fraction "Fraction")

   [(check/postfix [:cells])
    (check/check-or
     (sort-check "Cells")
     (check/eq "none"))]

   (simple-sort :medium "Medium")
   (simple-sort :mutation-type "Mutation")

   [(check/postfix [:symbol :_ :mutations])
    (sort-check "Mutation")]

   [(check/postfix [:symbol :_ :modifications])
    (sort-check "Modification")]

   [(check/postfix [:_ :substrates])
    (check/check-and
     (sort-check "Substrate")
     ivlka-check)]

   [(check/postfix [:_ :tests])
    (sort-check "Ktest")]

   (simple-sort :unit "TimeUnit")])
