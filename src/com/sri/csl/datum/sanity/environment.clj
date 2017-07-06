(ns com.sri.csl.datum.sanity.environment
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(def checkers
  [[(check/postfix [:datum])
    (check/path-required [:environment] "Environment must be present.")]])
