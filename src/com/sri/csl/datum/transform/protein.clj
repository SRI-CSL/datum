(ns com.sri.csl.datum.transform.protein
  (:require [com.sri.csl.datum.transform.common :as c]))

(def origins
  {"" "endogenous"
   "b" "baculovirus"
   "x" "expressed"
   "r" "recombinant"
   "p" "purified"
   "k" "knockin"
   "s" "synthetic"})

(defn origin [orig]
  {:origin (origins orig)})

(defn del-mut [rng]
  (assoc rng :mutation "del"))

(defn range-mut [n1 n2]
  {:start n1
   :end n2
   :mutation "range"})

(defn point [l1 n l2]
  {:position n
   :amino-acids [l1 l2]
   :mutation "point"})

(defn symbol-mut [sym]
  {:mutation "symbol"
   :symbol sym})

(defn mut-string [s]
  {:mutation "string"
   :string s})

(def transformers
  {:origin origin
   :protein (c/component :protein)
   :oprotein (c/simple-merge)
   :modification (c/simple-merge)
   :mod_symbol (c/component :sym)
   :modifications (c/multi :modifications)
   :mutations (c/multi :mutations)
   :sites (c/multi :sites)

   :range range-mut
   :del_mut del-mut
   :point point
   :s_mut (c/named-merge :s_mut {:mutation "s_mut"})
   :symbol_mut symbol-mut
   :mutation_string mut-string})
