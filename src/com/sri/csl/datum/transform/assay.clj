(ns com.sri.csl.datum.transform.assay
  (:require [com.sri.csl.datum.transform.common :as c]))

(defn simple-assay [name]
  (fn [& parts]
    (apply merge {:assay name} parts)))

(def transformers
  {:detect (c/component :detect)
   :assay_sym (c/component :assay)
   :hooks (c/multi :hooks)
   :hook (c/simple-merge)
   :hook_entity (c/component :entity)
   :substrates (c/multi :substrates)

   :modification_assay (simple-assay :modification)
   :binding (simple-assay :binding)
   :activity_assay (simple-assay :activity)
   :activity (c/component :assay)

   :locatedin (simple-assay :locatedin)
   :infraction (simple-assay :infraction)
   :boundto (simple-assay :boundto)
   :position (c/component :position)
   :fraction (c/component :fraction)
   :gene (c/component :gene)

   :phos (simple-assay :phos)
   :phostype (c/component :phostype)
   })

