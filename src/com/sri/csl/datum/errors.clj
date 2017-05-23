(ns com.sri.csl.datum.errors
  (:require
   [instaparse.failure :as fail]
   [clojure.string :as str]
   [com.sri.csl.datum.sanity.core :as sanity]))

(defn format-message [& lines]
  (->> lines
       (filter identity)
       (map (partial apply str))
       (str/join \newline)))

(defn format-parse-error [err]
  (let [{:keys [file line]} (:meta err)
        {parse-line :line
         parse-column :column
         parse-text :text} (:parse-error err)
        true-line (+ line (dec parse-line))]
    (format-message
     ["File: " file]
     ["Line " true-line ", column " parse-column ":" ]
     parse-text
     (fail/marker parse-column))))

(defn format-transform-error [err]
  (let [{:keys [file line]} (:meta err)]
    (format-message
     ["File: " file]
     ["Line: " line]
     ""
     (:transform-error err))))

(def formatters
  {"Parse" format-parse-error
   "Transform" format-transform-error
   "Sanity" sanity/format-error})

(defn format-error-section [title errors]
  (when (seq errors)
    (format-message
     [title " errors: " (count errors)]
     (str/join "\n\n" (map (formatters title) errors))
     "")))

(defn format-errors [results]
  (let [total (count results)
        t-errors (filter :transform-error results)
        p-errors (filter :parse-error results)
        s-errors (filter :sanity results)
        successes (- total
                     (count t-errors)
                     (count p-errors)
                     (count s-errors))]
    (format-message
     (format-error-section "Parse" p-errors)
     (format-error-section "Transform" t-errors)
     (format-error-section "Sanity" s-errors)
     ["Successful datums: " successes])))
