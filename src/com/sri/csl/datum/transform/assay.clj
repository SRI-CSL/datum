(ns com.sri.csl.datum.transform.assay
  (:require [com.sri.csl.datum.transform.common :as c]))

(defn simple-assay [name]
  (fn [& parts]
    (apply merge {:assay name} parts)))

(def transformers
  {:detect (c/component :detect)
   :assay_sym (c/component :assay)

   :activity_assay (simple-assay :activity)
   :activity (c/component :assay)
   :substrates (c/multi :substrates)

   :locatedin (simple-assay :locatedin)
   :infraction (simple-assay :infraction)
   :boundto (simple-assay :boundto)
   :position (c/component :position)
   :fraction (c/component :fraction)
   :gene (c/component :gene)

   :phos (simple-assay :phos)
   :phostype (c/component :phostype)

   :generic_assay (simple-assay :generic)

   :binding (simple-assay :binding)
   :hooks (c/multi :hooks)
   :hook (c/simple-merge)
   :hook_entity (c/component :entity)})

