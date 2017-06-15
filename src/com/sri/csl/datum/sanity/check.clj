(ns com.sri.csl.datum.sanity.check
  (:require [com.sri.csl.datum.ops :as ops]))

(defn sort-checker [label sort]
  [(fn [path] (= (last path) label))
   (fn [datum path node]
     (if (ops/check-op sort node)
       []
       [{:path path
         :error (str node " is not a known " sort)}]))])

(defn checkers []
  (concat
   [(sort-checker :protein "Protein")
    (sort-checker :chemical "Chemical")
    (sort-checker :gene "Gene")
    (sort-checker :handle "Handle")
    (sort-checker :detect "DetectionMethod")
    (sort-checker :position "Position")
    (sort-checker :fraction "Fraction")
    (sort-checker :cells "Cells")
    (sort-checker :medium "Medium")
    (sort-checker :mutation-type "Mutation")
    (sort-checker :unit "TimeUnit")
    ]))

(defn applicable
  [checks path]
  (->> checks
       (map
        (fn [[applic check]]
          (when (applic path)
            check)))
       (filter identity)))

