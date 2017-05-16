(ns com.sri.csl.datum.transform.postprocess)

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

(defn postprocess [d]
  (-> d
      stimes))
