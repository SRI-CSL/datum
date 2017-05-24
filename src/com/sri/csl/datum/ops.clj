(ns com.sri.csl.datum.ops
  (:require [clojure.set :as set]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.algo.generic.functor :refer [fmap]]))

(defn parse-sort [sort]
  (apply hash-map
         (mapcat
          (fn [[k v]] [(keyword k) (set v)])
          sort)))

(defn load-ops [f]
  (->> f
       slurp
       json/read-str
       (fmap parse-sort)))

(defn valid-sorts [sorts]
  (and (map? sorts)
       (-> sorts
           first
           second
           :elements)))

(def datumsorts
  (atom (load-ops (io/resource "datumsorts.json"))
        :validator valid-sorts))

(defn load-ops! [f]
  (reset! datumsorts (load-ops f)))

#_(def allsorts
    (reduce set/union (map (comp :elements second) @datumsorts)))

(defn check-op [sort op]
  (if-let [{:keys [elements]} (@datumsorts sort)]
    (elements op)))

(defn sorts [op]
  (map first
       (filter
        (fn [[sort {elements :elements}]]
          (elements op))
        @datumsorts)))
