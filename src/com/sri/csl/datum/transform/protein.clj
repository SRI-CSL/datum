(ns com.sri.csl.datum.transform.protein
  (:require [com.sri.csl.datum.transform.common :as c]))

(defn del-mut [dom]
  {:mutation "del"
   :text (str "del(" (:text dom) ")")})

(defn domain [n1 n2]
  {:text (str n1 "-" n2)
   :mutation "domain"})

(defn s-mut [mut]
  {:mutation "s_mut"
   :text mut})

(defn point [l1 n l2]
  {:mutation "point"
   :text (str l1 n l2)})

(defn symbol-mut [sym]
  {:mutation "symbol"
   :text sym})

(defn mut-string [s]
  {:mutation "string"
   :text s})

(defn nullable-vec
  ([] [])
  ([& r] (vec r)))

(def origins
  {"" "endogenous"
   "x" "expressed"
   "r" "recombinant"
   "p" "purified"
   "k" "knockin"
   "s" "synthetic"})

(defn origin [orig]
  {:origin (origins orig)})

(def transformers
  {:origin origin
   :protein (c/component :protein)
   :oprotein (c/simple-merge {:type :oprotein})
   :xsprotein (c/simple-merge {:type :xsprotein})
   :modification (c/simple-merge)
   :mod_symbol (c/component :sym)
   :modifications (c/multi :modifications)
   :mutations (c/multi :mutations)
   :sites (c/multi :sites)

   :domain domain
   :del_mut del-mut
   :point point
   :s_mut s-mut
   :symbol_mut symbol-mut
   :mutation_string mut-string})
