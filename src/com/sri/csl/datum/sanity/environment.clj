(ns com.sri.csl.datum.sanity.environment
  (:require
   [com.sri.csl.datum.sanity.check :as check]))

(def checkers
  [[(check/path-end [:datum])
    (check/path-required [:environment] "Environment must be present.")]])
