(ns com.sri.csl.datum.sanity.check
  (:require [com.sri.csl.datum.ops :as ops]
            [clojure.string :as str]))

(defn path-comp
  "DSL for checking elements of a path.

  :keywords match directly.
  :!keyword says 'anything but this keyword'
  fns are run with the element being checked
  :_ matches anything"
  [spec elt]
  (cond
    (= spec :_)
    true

    (fn? spec)
    (spec elt)

    (= \! (first (name spec)))
    (not= (-> spec name (subs 1) keyword) elt)

    :else
    (= spec elt)))

(defn path-end [postfix]
  (fn [path]
    (and
     (>= (count path) (count postfix))
     (every? true? (map path-comp postfix path)))))

(defn sort-check [& sorts]
  (fn [datum path node]
    (when-not (some #(ops/check-op % node) sorts)
      [{:path path
        :error (str node " is not a known " (str/join "/" sorts))}])))

(defn eq-check [val]
  (fn [datum path node]
    (when (not= node val)
      [{:path path
        :error (str node " is not " val)}])))

(defn simple-sort [label & sorts]
  [(path-end [label])
   (apply sort-check sorts)])

(defn check-or [& checkers]
  (fn [datum path node]
    (let [results (mapcat #(% datum path node) checkers)]
      (when (= (count checkers) (count results))
        results))))

(defn check-and [& checkers]
  (fn [datum path node]
    (mapcat #(% datum path node) checkers)))

(defn checkers []
  (concat
   [(simple-sort :protein "Protein" "Peptide" "Composite")
    (simple-sort :chemical "Chemical")
    (simple-sort :gene "Gene")
    (simple-sort :handle "Handle")

    ;; Needs logic to prevent barfing at outer-level assays
    ;; (simple-sort :assay "AssayType")
    (simple-sort :detect "DetectionMethod")
    ;; (simple-sort :position "Position")
    (simple-sort :fraction "Fraction")
    (simple-sort :cells "Cells")
    (simple-sort :medium "Medium")
    (simple-sort :mutation-type "Mutation")
    (simple-sort :unit "TimeUnit")
    ]))

(defn applicable
  [checks path]
  (->> checks
       (map
        (fn [[applic check]]
          (when (applic path)
            check)))
       (filter identity)))

