(ns com.sri.csl.datum.transform.postprocess)

(defn generate-times [{:keys [time unit]} change]
  (let [t2 (Double. time)]
    {:unit unit
     :times
     (case change
       "increased" [[0 0] [t2 1]]
       "decreased" [[0 1] [t2 0]]
       [[0 0] [t2 0]])}))

(defn stimes [d]
  (if-let [stpt (:stimes d)]
    (-> d
        (dissoc :stimes)
        (assoc :times
               (generate-times stpt
                               (:change d))))
    d))

(defn postprocess [d]
  (-> d
      stimes))
