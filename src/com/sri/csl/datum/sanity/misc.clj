(ns com.sri.csl.datum.sanity.misc
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(def checkers
  [[(check/postfix ["irt" :treatment_type])
    (check/path-required [:times] "Times must be present in IRT datums.")]
   [(check/postfix [:unknown])
    (check/error "Unknown symbol %s.")]])
