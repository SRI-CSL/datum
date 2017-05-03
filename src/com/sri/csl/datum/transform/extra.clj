(ns com.sri.csl.datum.transform.extra
  (:require [com.sri.csl.datum.transform.common :as c]
            [clojure.string :as string]
            [com.sri.csl.datum.transform.treatment :as treatment]))

(defn extra-name
  ([adjective extra]
   {:adjective adjective
    :type extra})
  ([extra]
   {:type extra}))

(def transformers
  {:extra (c/named-merge :extra)
   :extra_name extra-name
   :req_name extra-name

   :extra_treat (c/named-merge :treatment)
   :substitution (c/multi :treatments)
   })
