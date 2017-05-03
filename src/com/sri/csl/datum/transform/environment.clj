(ns com.sri.csl.datum.transform.environment
  (:require [com.sri.csl.datum.transform.common :as c]))

(def transformers
  {:environment (c/named-merge :environment)
   :env_none (c/component :cells)
   :cells (c/component :cells)
   :medium (c/component :medium)
   :cell_mutations (c/multi :cell-mutations)
   :cell_mutation (c/simple-merge)
   :mutation_type (c/component :mutation-type)})
