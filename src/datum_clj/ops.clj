(ns datum-clj.ops
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.algo.generic.functor :refer [fmap]]))

(defn parse-sort [sort]
  (apply hash-map
         (mapcat
          (fn [[k v]] [(keyword k) (set v)])
          sort)))

(def datumsorts
  (->> "datumsorts.json"
       io/resource
       io/file
       slurp
       json/read-str
       (fmap parse-sort)))

(defn check-op [sort op]
  (if-let [{:keys [elements]} (datumsorts sort)]
    (elements op)))

