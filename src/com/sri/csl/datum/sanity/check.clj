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

(defn path-end [postfix]
  (fn [path]
    (and
     (>= (count path) (count postfix))
     (every? true? (map path-comp postfix path)))))

(defn eq-check [val]
  (fn [datum path node]
    (when (not= node val)
      [{:path path
        :error (str node " is not " val)}])))

(defn error [msg]
  (fn [datum path node]
    {:path path
     :error msg}))

(defn check-or [& checkers]
  (fn [datum path node]
    (let [results (mapcat #(% datum path node) checkers)]
      (when (= (count checkers) (count results))
        results))))

(defn check-and [& checkers]
  (fn [datum path node]
    (mapcat #(% datum path node) checkers)))

(defn applicable
  [checks path]
  (->> checks
       (map
        (fn [[applic check]]
          (when (applic path)
            check)))
       (filter identity)))
