(ns com.sri.csl.datum.sanity.assay
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(defn ivka-sites-must-be-sty [datum path node]
  (let [sites (get-in datum [:assay :sites])]
    (when-not (every? (fn [site]
                        (or
                         (#{\S \T \Y} (first site))
                         (= "sitenr" site)))
                      sites)
      "IVKA sites must be S/T/Y or sitenr.")))

(defn ivlka-has-no-sites [datum path node]
  (when (= (get-in datum [:assay :assay])
           "IVLKA")
    "IVLKA assays should not have sites."))

(defn oligo-binding-has-no-sites [datum path node]
  (when (= (get-in datum [:assay :assay])
           "oligo-binding")
    "Oligo-binding assays should not have sites."))

(def checkers
  [[(check/postfix [:sites :assay])
    (check/check-and
     ivlka-has-no-sites
     oligo-binding-has-no-sites)]

   [(check/postfix ["IVKA" :assay])
    ivka-sites-must-be-sty]])
