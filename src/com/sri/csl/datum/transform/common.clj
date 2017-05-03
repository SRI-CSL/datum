(ns com.sri.csl.datum.transform.common)

(defn placeholder [type] (constantly {type :placeholder}))

(defn named-merge [name]
  (fn [& values]
    {name (apply merge {} values)}))

(defn component [name]
  (fn
    ([value] {name value})
    ([] {name nil})))

(defn multi [name]
  (fn [& values]
    {name (into [] values)}))

(defn simple-merge
  ([] (simple-merge {}))
  ([adds]
   (fn [& vals]
     (apply merge adds vals))))
