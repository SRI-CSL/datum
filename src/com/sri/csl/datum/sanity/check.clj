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

    (= \! (first (name spec)))
    (not= (-> spec name (subs 1) keyword) elt)

    :else
    (= spec elt)))

(defn postfix [post]
  (fn [path]
    (and
     (>= (count path) (count post))
     (every? true? (map path-comp post path)))))

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

(defn check-or [& checkers]
  (fn [datum path node]
    (let [results (mapcat #(% datum path node) checkers)]
      (when (= (count checkers) (count results))
        results))))

(defn check-and [& checkers]
  (fn [datum path node]
    (map #(% datum path node) checkers)))

(defn applicable
  [checks path]
  (->> checks
       (map
        (fn [[applic check]]
          (when (applic path)
            check)))
       (filter identity)))
