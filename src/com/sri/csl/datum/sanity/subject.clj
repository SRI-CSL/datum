(ns com.sri.csl.datum.sanity.subject
  (:require
   [com.sri.csl.datum.sanity.check :as check]
   [clojure.string :as str]))

(defn subject-fields [datum path node]
  (when
      (and (not (:protein node))
           (some node [:origin :ip :modifications :mutations]))
    "Non-protein subject with protein-related fields."))

(defn handle-required [datum path node]
  (and
   (not (empty? node))
   (not (:handle node))
   (not (:chemical node))
   (not (:gene node))
   (not (#{"recombinant" "purified"} (:origin node)))
   "Handle required for this subject."))

(def checkers
  [[(check/postfix [:subject])
    (check/check-and
     subject-fields
     handle-required)]])
