(ns com.sri.csl.datum.duplicate
  (:require [com.sri.csl.datum.errors :refer [format-message]]
            [clojure.string :as str]))

;; (defn datum-eq [d1 d2]
;;   (= (dissoc d1 :meta)
;;      (dissoc d2 :meta)))

;; (defn datum-fuzzy-eq [d1 d2]
;;   (= (dissoc d1 :meta :extras)
;;      (dissoc d2 :meta :extras)))

(defn by-pmid
  "Partitions a seq of datums by PMID."
  [datums]
  (vals (group-by (comp :pmid :source) datums)))

(defn merge-datums
  "Merges a seq of datums into a single datum.
  Assumes that all input datums are similar already."
  [datums]
  (if (> (count datums) 1)
    (let [metas (map :meta datums)
          comments (distinct (mapcat :comments datums))
          extras (distinct (mapcat :extras datums))]
      (assoc (first datums)
             :meta {:merged metas}
             :comments comments
             :extras extras))
    (first datums)))

(defn merge-bucket
  "Groups a seq of datums into smaller buckets that
  only differ by :meta and :extras."
  [bucket]
  (->> bucket
       (group-by #(dissoc % :meta :comments :extras))
       vals
       (map merge-datums)))

(defn merge-related
  "Transforms a seq of datums by merging all datums
  that only differ by :meta and :extras."
  [datums]
  (mapcat merge-bucket (by-pmid datums)))

(defn dups-in-bucket
  "Finds all exact duplicate datums in a seq."
  [bucket]
  (->> bucket
       (group-by #(dissoc % :meta))
       vals
       (filter #(> (count %) 1))))

(defn duplicates [datums]
  (->> datums
       by-pmid
       (mapcat dups-in-bucket)
       (filter seq)))

(defn format-duplicate
  "Formats a single set of duplicate datums for error output."
  [dup-seq]
  (let [metas (map :meta dup-seq)]
    (format-message
     (:text (first metas))
     ""
     "Found at:"
     (map (fn [{:keys [file line]}]
            (str file ":" line \newline))
          metas))))

(defn format-duplicates [duplicates]
  (->> duplicates
       (map format-duplicate)
       (str/join "\n\n")))
