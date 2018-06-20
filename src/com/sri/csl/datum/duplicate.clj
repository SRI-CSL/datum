(ns com.sri.csl.datum.duplicate
  (:require [com.sri.csl.datum.errors :refer [format-message]]
            [clojure.string :as str]))

(defn by-pmid
  "Partitions a seq of datums by PMID."
  [datums]
  (vals (group-by (comp :pmid :source) datums)))

(def merge-text-patterns
  ["comment:"
   "enhanced by:"
   "inhibited by:"
   "repressed by:"
   "reversed by:"
   "unaffected by:"])

(defn mergeable-line
  "Determines if a line of datum text is of the kind that merged
  when merging similar datums."
  [line]
  (some (partial str/includes? line) merge-text-patterns))

(defn mergeable-lines [datum]
  (-> datum
      (get-in [:meta :text])
      (str/split #"\n")
      (->> (filter mergeable-line))))

(defn merged-text [[d1 & ds]]
  (let [base-text (get-in d1 [:meta :text])
        added-text (mapcat mergeable-lines ds)]
    (str/join "\n" (into [base-text] added-text))))

(defn merge-datums
  "Merges a seq of datums into a single datum.
  Assumes that all input datums are similar already."
  [datums]
  (if (> (count datums) 1)
    (let [metas (map :meta datums)
          comments (distinct (mapcat :comments datums))
          extras (distinct (mapcat :extras datums))]
      (assoc (first datums)
             :meta {:text (merged-text datums)
                    :merged metas}
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
