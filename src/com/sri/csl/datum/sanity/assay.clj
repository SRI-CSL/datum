(ns com.sri.csl.datum.sanity.assay
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(defn sites-requires-single-substrate [datum path node]
  (let [substrates (get-in datum [:assay :substrates])]
    (when-not (= 1 (count substrates))
      [{:path path
        :error "Assay sites only make sense with exactly one substrate."}])))

(defn oligo-binding-has-no-sites [datum path node]
  (when (= (get-in datum [:assay :assay])
           "oligo-binding")
    [{:path path
      :error "Oligo-binding assays should not have sites."}]))

(def checkers
  [[(check/path-end [:sites :assay])
    (check/check-and
     sites-requires-single-substrate
     oligo-binding-has-no-sites)]])
