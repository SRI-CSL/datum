(ns com.sri.csl.datum.transform.datum
  (:require [com.sri.csl.datum.transform
             [postprocess :refer [postprocess]]
             [misc :as misc]
             [protein :as protein]
             [assay :as assay]
             [treatment :as treatment]
             [environment :as environment]
             [extra :as extra]
             [common :as c]]
            [instaparse.core :as insta]))

(defn singleton? [line]
  (not (or
        (:extra line)
        (:comment line)
        (:ipfrom line))))

(defn transformers []
  (merge {:cfirstline (c/simple-merge)
          :sfirstline (c/simple-merge)
          :subject (c/named-merge :subject)
          :schange (c/component :change)
          :changetype (c/component :treatment_type)}
         protein/transformers
         assay/transformers
         treatment/transformers
         environment/transformers
         extra/transformers
         misc/transformers
         ))

(defn datum [ast]
  (let [transformed-ast (insta/transform (transformers) ast)
        lines (rest transformed-ast)]
    (postprocess
     (apply merge
            {:extras (mapv :extra (filter :extra lines))
             :ipfrom (mapv :ipfrom (filter :ipfrom lines))
             :comments (mapv :comment (filter :comment lines))}
            (filter singleton? lines)))))


