(ns com.sri.csl.datum.transform.treatment
  (:require [com.sri.csl.datum.transform.common :as c]
            [com.sri.csl.datum.ops :as ops]
            [com.sri.csl.datum.transform.protein :as protein]))

(defn treat-sym [s]
  (cond
    (ops/check-op "Chemical" s) {:chemical s}
    (ops/check-op "Antibody" s) {:antibody s}
    (ops/check-op "Stress" s) {:stress s}
    (ops/check-op "Protein" s) {:protein s}

    (ops/check-op "Protein" (subs s 1))
    {:protein (subs s 1)
     :origin (protein/origins (subs s 0 1))}

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
  {:treat_sym treat-sym
   :ktest (c/component :test)
   :conjunction (c/component :conjunction)
   :treatments treatments
   :ktreatment treatments
   :extra_treat treatments
   :substitution treatments})
