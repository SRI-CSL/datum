(ns com.sri.csl.datum.transform.common)

(defn placeholder [type] (constantly {type :placeholder}))

(defn component [name]
  (fn
    ([value] {name value})
    ([] {name nil})))

(defn flag [name]
  (fn
    ([] {name false})
    ([_] {name true})))

(defn multi [name]
  (fn [& values]
    {name (into [] values)}))

(defn simple-merge
  ([] (simple-merge {}))
  ([adds]
   (fn [& vals]
     (apply merge adds vals))))

(defn named-merge
  ([name] (named-merge name {}))
  ([name adds]
   (fn [& values]
     {name (apply merge adds values)})))
