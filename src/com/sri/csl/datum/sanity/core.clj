(ns com.sri.csl.datum.sanity.core
  (:require [com.sri.csl.datum.sanity
             [crawl :as crawl]]))

(defn check [datum]
  (let [sanity-errors (crawl/crawl-datum datum)]
    (if (empty? sanity-errors)
      datum
      (assoc datum :sanity-errors sanity-errors))))

