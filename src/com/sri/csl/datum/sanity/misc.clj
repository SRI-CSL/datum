(ns com.sri.csl.datum.sanity.misc
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(def checkers
  [[(check/path-end ["irt" :treatment_type])
    (check/path-required [:times] "Times must be present in IRT datums.")]
   [(check/path-end [:unknown])
    (check/error "Unknown symbol %s.")]])
