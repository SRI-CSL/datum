(ns com.sri.csl.datum.sanity.environment
  (:require
   [com.sri.csl.datum.sanity
    [check :as check]
    [crawl :as crawl]]))

(defn expressed-proteins-from-firstline [datum path node]
  (let [protein-path (rest (reverse (conj (drop 2 path) :protein)))
        protein (get-in datum protein-path)
        checkers (check/checker-finder
                  [[(check/postfix [:_ :cell-mutations])
                    (fn [datum path node]
                      (and (= "expressed" (:origin node))
                           (= protein (:protein node))
                           protein))]])
        crawl-results (crawl/crawl checkers datum '(:datum) datum)]
    (when (empty? crawl-results)
      (str
       protein
       " expressed in first line but not found in environment/IPFrom."))))

(def checkers
  [[(check/postfix [:datum])
    (check/path-required [:environment] "Environment must be present.")]

   [(check/path-or
     (check/postfix ["expressed" :origin :_ :hooks])
     (check/postfix ["expressed" :origin :subject])
     (check/path-and
      (check/postfix ["expressed" :origin :_ :treatments :treatment])
      (check/path-value [:treatment_type] "by")))
    expressed-proteins-from-firstline]])
