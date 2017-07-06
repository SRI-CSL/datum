(ns com.sri.csl.datum.transform.treatment
  (:require [com.sri.csl.datum.transform.common :as c]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.transform.protein :as protein]))

(defn multi-sym [s]
  (cond
    (ops/check-op "Protein" s) {:protein s}
    (ops/check-op "Composite" s) {:protein s}
    (ops/check-op "Peptide" s) {:protein s}
    (ops/check-op "Chemical" s) {:chemical s}
    (ops/check-op "Gene" s) {:gene s}

    ;; Only in treatments
    (ops/check-op "Antibody" s) {:antibody s}
    (ops/check-op "Stress" s) {:stress s}

    (ops/check-op "Protein" (subs s 1))
    {:protein (subs s 1)
     :origin (protein/origins (subs s 0 1))}

    (= s "oligo") {:oligo "oligo"}

    :else {:unknown s}))

(defn treatments [& vals]
  (let [treats (remove (some-fn :conjunction :test) vals)
        tests (mapv :test (filter :test vals))
        conjs (into #{} (map :conjunction (filter :conjunction vals)))
        cj (if (> (count conjs) 1)
             :inconsistent
             (first conjs))]
    {:treatment {:treatments (into [] treats)
                 :tests tests
                 :conjunction cj}}))

(def transformers
  {:subject_sym multi-sym
   :treat_sym multi-sym
   :hook_sym multi-sym
   :ktest (c/component :test)
   :conjunction (c/component :conjunction)
   :treatments treatments
   :ktreatment treatments
   :substitution treatments})
