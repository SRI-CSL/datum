(ns com.sri.csl.datum.sanity.crawl
  (:require [com.sri.csl.datum.sanity.check :as check]))

(declare crawl)

(defn check-node [datum path checkers node]
  (let [applicable-checks (check/applicable checkers path)]
    (->> applicable-checks
         (map #(% datum path node))
         (filter identity)
         (apply concat))))

(defn crawl-map [datum path checkers m]
  (mapcat
   (fn [[label child]]
     (crawl datum (conj path label) checkers child))
   m))

(defn crawl-vec [datum path checkers v]
  (apply concat
         (map-indexed
          (fn [idx child]
            (crawl datum (conj path idx) checkers child))
          v)))

(defn crawl [datum path checkers node]
  (concat
   (check-node datum path checkers node)
   (cond
     (map? node)
     (crawl-map datum path checkers node)

     (vector? node)
     (crawl-vec datum path checkers node)

     :else
     [])))
