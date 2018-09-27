(ns com.sri.csl.datum.sanity.check)

(defn path-comp
  "DSL for checking elements of a path.

  :keywords match directly.
  :!keyword says 'anything but this keyword'
  fns are run with the element being checked
  :_ matches anything"
  [spec elt]
  (cond
    (= spec :_)
    true

    (fn? spec)
    (spec elt)

    (set? spec)
    (spec elt)

    (= \! (first (name spec)))
    (not= (-> spec name (subs 1) keyword) elt)

    :else
    (= spec elt)))

(defn postfix [post]
  (fn [datum path node]
    (and
     (>= (count path) (count post))
     (every? identity (map path-comp post path)))))

(defn path-and [& path-fns]
  (fn [datum path node]
    (every? #(% datum path node) path-fns)))

(defn path-or [& path-fns]
  (fn [datum path node]
    (some #(% datum path node) path-fns)))

(defn eq [val]
  (fn [datum path node]
    (when (not= node val)
      (str node " is not " val))))

(defn regex [re]
  (fn [datum path node]
    (when-not (re-matches re node)
      (str node " does not match " re))))

(defn error [msg]
  (fn [datum path node]
    (format msg (str node))))

(defn path-required [required-path msg]
  (fn [datum path node]
    (when-not (get-in datum required-path)
      msg)))

(defn path-value [value-path value]
  (fn [datum path node]
    (= (get-in datum value-path) value)))

(defn sibling-pred [sibling pred msg]
  (fn [datum path node]
    (let [outer-path (into [] (rest (reverse (rest path))))
          sibling-path (conj outer-path sibling)
          sibling-value (get-in datum sibling-path)]
      (when-not (pred sibling-value)
        msg))))

(defn sibling-exists [sibling msg]
  (sibling-pred sibling identity msg))

(defn check-pred [pred msg]
  (fn [datum path node]
    (when-not (pred datum path node)
      msg)))

(defn check-or [& checkers]
  (fn [datum path node]
    (let [results (map #(% datum path node) checkers)]
      (when (every? seq results)
        (flatten results)))))

(defn check-and [& checkers]
  (fn [datum path node]
    (flatten (map #(% datum path node) checkers))))

(defn checker-finder [checkers]
  (fn [datum path node]
    (->> checkers
         (map
          (fn [[applic check]]
            (when (applic datum path node)
              check)))
         (filter identity))))
