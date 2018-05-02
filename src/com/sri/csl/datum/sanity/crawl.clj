(ns com.sri.csl.datum.sanity.crawl)

(declare crawl)

(defn check-node [checkers datum path node]
  (let [applicable-checks (checkers datum path node)]
    (->> applicable-checks
         (map #(% datum path node))
         flatten
         (filter identity)
         (map (fn [err]
                {:path path
                 :error err})))))

(defn crawl-map [checkers datum path m]
  (mapcat
   (fn [[label child]]
     (crawl checkers datum (conj path label) child))
   m))

(defn crawl-seq [checkers datum path s]
  (apply concat
         (map-indexed
          (fn [idx child]
            (crawl checkers datum (conj path idx) child))
          s)))

(defn crawl [checkers datum path node]
  (concat
   (check-node checkers datum path node)
   (cond
     (map? node)
     (crawl-map checkers datum path node)

     (sequential? node)
     (crawl-seq checkers datum path node)

     :else
     (check-node checkers datum (conj path node) nil))))

(defn crawl-datum [checkers datum]
  (crawl checkers datum '(:datum) datum))
