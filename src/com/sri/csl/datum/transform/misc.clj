(ns com.sri.csl.datum.transform.misc
  (:require [clojure.string :as string]
            [com.sri.csl.datum.transform.common :as c]))

(defn parse-comment [text]
  {:comment (string/trim text)})

(defn timepoint [tpt mag]
  [(Integer. tpt) (.length mag)])

(defn ip
  ([] {:ip false})
  ([_] {:ip true}))

(defn stpt [t unit]
  {:time t
   :unit unit})

(defn change [& vals]
  {:change (string/join " " vals)})

(def transformers
  {:change change
   :comment parse-comment
   :pmid (c/component :pmid)
   :figures (c/multi :figures)
   :source (c/named-merge :source)

   :timepoint timepoint
   :timepoints (c/multi :times)
   :unit (c/component :unit)
   :times (c/named-merge :times)
   :stpt stpt
   :excuse (c/component :excuse)

   :ip ip
   :handle (c/component :handle)})