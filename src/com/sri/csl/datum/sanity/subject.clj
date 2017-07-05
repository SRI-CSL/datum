(ns com.sri.csl.datum.sanity.subject
  (:require
   [com.sri.csl.datum.sanity.check :as check]
   [clojure.string :as str]))

(defn subject-fields [datum path node]
  (when
      (and (not (:protein node))
           (some node [:origin :ip :modifications :mutations]))
    "Non-protein subject with protein-related fields."))

(def checkers
  [[(check/path-end [:subject])
    subject-fields]])
