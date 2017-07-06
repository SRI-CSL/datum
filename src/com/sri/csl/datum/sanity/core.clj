(ns com.sri.csl.datum.sanity.core
  (:require [com.sri.csl.datum.sanity
             [crawl :as crawl]
             [sorts :as sorts]
             [subject :as subject]
             [assay :as assay]
             [environment :as environment]
             [treatment :as treatment]
             [misc :as misc]]))

(defn checkers []
  (concat
   sorts/checkers
   subject/checkers
   assay/checkers
   environment/checkers
   treatment/checkers
   misc/checkers))

(defn check [datum]
  (let [sanity-errors (crawl/crawl datum '(:datum) (checkers) datum)]
    (if (empty? sanity-errors)
      datum
      (assoc datum :sanity-errors sanity-errors))))

