(ns com.sri.csl.datum.sanity.assay
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(defn ivlka-has-no-sites [datum path node]
  (when (= (get-in datum [:assay :assay])
           "IVLKA")
    "IVLKA assays should not have sites."))

(defn oligo-binding-has-no-sites [datum path node]
  (when (= (get-in datum [:assay :assay])
           "oligo-binding")
    "Oligo-binding assays should not have sites."))

(def checkers
  [[(check/path-end [:sites :assay])
    (check/check-and
     ivlka-has-no-sites
     oligo-binding-has-no-sites)]])
