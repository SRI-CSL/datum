(ns com.sri.csl.datum.sanity.core
  (:require [com.sri.csl.datum.sanity
             [crawl :as crawl]
             [sorts :as sorts]
             [subject :as subject]]))

(defn checkers []
  (concat
   sorts/checkers
   subject/checkers))

(defn check [datum]
  (let [sanity-errors (crawl/crawl datum '(:datum) (checkers) datum)]
    (if (empty? sanity-errors)
      datum
      (assoc datum :sanity-errors sanity-errors))))

