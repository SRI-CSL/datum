(ns com.sri.csl.datum.transform.datum
  (:require [com.sri.csl.datum.transform
             [firstline :refer [parse-first-line]]
             [misc :as misc]
             [protein :as protein]
             [assay :as assay]
             [subject :as subject]
             [common :as c]]
            [clojure.pprint :refer [pprint]]
            [instaparse.core :as insta]))

(defn singleton? [line] (not (or (:extra line) (:comment line))))

(defn transformers []
  (merge {:cfirstline (c/simple-merge)
          :sfirstline (c/simple-merge)
          :subject (c/named-merge :subject)
          :change (c/component :change)
          :changetype (c/component :treatment_type)
          :treatments (c/placeholder :treatments)
          :extra (c/placeholder :extra)
          :environment (c/placeholder :environment)}
         protein/transformers
         assay/transformers
         misc/transformers
         ))

(defn datum [ast text file line]
  (let [transformed-ast (insta/transform (transformers) ast)
        [_ & lines] transformed-ast]
    (println)
    (pprint transformed-ast)
    (println)
    (apply merge
           {:meta {:ast ast
                   :fulltext text
                   :file file
                   :line line}
            :extras (filterv :extra lines)
            :comments (mapv :comment (filter :comment lines))}
           (filter singleton? lines))))

