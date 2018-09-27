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
;; If it's in the cell mutations, and there's a ~pos/neg/null
;; tag on the protein, it's endogenous.
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
  sub-paths for each entry in th at vector."
  [d base]
  (if-let [items (get-in d base)]
    (map #(into [] (conj base %))
         (range 0 (count items)))
    []))

(defn assume-subject [d]
  (assume-path d [:subject] "endogenous"))

(defn assume-hooks [d]
  (let [paths (get-paths d [:assay :hooks])]
    (assume-paths d paths "endogenous")))

(defn assume-cellmuts [d]
  (let [paths (get-paths d [:environment :cell-mutations])
        mutated-paths (filter #(get-in d (conj % :mutation-type)) paths)]
    (assume-paths d mutated-paths "endogenous")))

(defn treatment-assumption [d]
  (case (:treatment_type d)
    "irt"
    "recombinant"

    "itao"
    "endogenous"

    nil))

(defn assume-treatments [d]
  (let [assumption (treatment-assumption d)
        paths (get-paths d [:treatment :treatments])]
    (if assumption
      (assume-paths d paths assumption)
      d)))

(defn assume-extras [d]
  (let [extra-paths (get-paths d [:extras])
        inner-paths (mapcat #(get-paths d (into % [:treatment :treatments])) extra-paths)]
    (assume-paths d inner-paths "endogenous")))

(defn assume-origins [d]
  (-> d
      assume-subject
      assume-hooks
      assume-cellmuts
      assume-treatments
      assume-extras))

(defn postprocess [d]
  (-> d
      stimes
      assume-origins))
