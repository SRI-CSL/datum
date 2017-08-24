(ns com.sri.csl.datum.transform.extra
  (:require [com.sri.csl.datum.transform.common :as c]))

(def transformers
  {:extra (c/named-merge :extra)
   :extra_name (c/component :extra-type)
   :extra_adj (c/component :adjective)
   :req_name (c/component :extra-type)
   :addition_mode (c/component :mode)
   :sub_mode (c/component :mode)

   :stim_times (c/named-merge :stim-times)
   :numlist (c/multi :times)
   :stim_times_excuse (c/component :excuse)
   })
