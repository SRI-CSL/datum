(ns com.sri.csl.datum.transform.postprocess)

;; Times
;;
;; If a datum has (times) in its first line, autogenerate
;; what the times: line should say and inject it into the json.
;; TODO -- Also inject it into the fulltext?

(defn generate-times [{:keys [time unit]} change]
  {:unit unit
   :times
   (case change
     "increased" [[0 0] [time 1]]
     "decreased" [[0 1] [time 0]]
     [[0 0] [time 0]])})

(defn stimes [d]
  (if-let [stpt (:stpt d)]
    (-> d
        (dissoc :stpt)
        (assoc :times
               (generate-times stpt
                               (:change d))))
    d))

;; Origins
;;
;; If a protein is missing an origin, depending where it
;; is in the datum and the context, we may be able to fill
;; it in. If it's in the subject, hooks, or extras, it can
;; be assumed to be endogenous.
;;
;; If it's in the cell mutations or IPFrom, and there's a
;; ~pos/neg/null tag on the protein, it's endogenous.
;;
;; If it's in an itao treatment, it's endogenous.
;; If it's in an irt treatment, it's recombinant.
;;
;; Otherwise, make no assumption.

(defn assume-path
  "Given a datum and a path, check if there's a protein under
  that path. If there is, and it's missing an origin, fill it
  in with the given assumption."
  [d path assumption]
  (let [protein (get-in d (conj path :protein))
        origin-path (conj path :origin)
        origin (get-in d origin-path)]
    (if (and protein
             (not origin))
      (assoc-in d origin-path assumption)
      d)))

(defn assume-paths
  "Runs assume-path on a collection of paths, combining the
  edits made by each one."
  [d paths assumption]
  (reduce
   (fn [dr p]
     (assume-path dr p assumption))
   d
   paths))

(defn get-paths
  "Given a path into a datum, ending in a vector, generate
  sub-paths for each entry in that vector."
  [d base]
  (if-let [items (get-in d base)]
    (if (vector? items)
      (map #(into [] (conj base %))
           (range 0 (count items)))
      [base])
    []))

(defn get-paths-multi
  "Given a series of path segments, generate sub paths by
  expanding vectors between them."
  [d path-segments]
  (loop [[seg & rst] path-segments
         paths [[]]]
    (if seg
      (recur rst
             (mapcat #(get-paths d (into % seg)) paths))
      paths)))

(defn has-mutation-type [d path]
  (get-in d (conj path :mutation-type)))

(defn treatment-assumption [d]
  ({"irt" "recombinant"
    "itao" "endogenous"}
   (:treatment_type d)))

(def base-assumption
  {:assumption-fn (constantly "endogenous")
   :filter-fn (constantly true)})

(def origin-assumptions
  (map (partial merge base-assumption)
   [{:pathspec [[:subject]]}
    {:pathspec [[:assay :hooks]]}
    {:pathspec [[:environment :cell-mutations]]
     :filter-fn has-mutation-type}
    {:pathspec [[:ipfrom] [:cell-mutations]]
     :filter-fn has-mutation-type}
    {:pathspec [[:treatment :treatments]]
     :assumption-fn treatment-assumption}
    {:pathspec [[:extras] [:treatment :treatments]]}]))

(defn assume-spec
  "Given a datum and an assumption spec, fill in missing origins.

  An assumption spec consists of:
  - a pathspec, a vector of one or more path segments to be explored
  - a filter function, which takes a datum and a path and returns true
    if that path is a valid path to make an assumption about
  - an assumption function, which takes a datum and generations an
    assumption about the default protein type"
  [d {:keys [pathspec assumption-fn filter-fn]}]
  (if-let [assumption (assumption-fn d)]
    (let [paths (get-paths-multi d pathspec)
          filtered-paths (filter (partial filter-fn d) paths)]
      (assume-paths d paths assumption))
    d))

(defn assume-origins [d specs]
  (reduce assume-spec d specs))

(defn postprocess [d]
  (-> d
      stimes
      (assume-origins origin-assumptions)))
