(ns com.sri.csl.datum.errors
  (:require
   [instaparse.failure :as fail]
   [clojure.string :as str]
   [com.sri.csl.datum.sanity.core :as sanity]))

(def error?
  (some-fn :transform-error
           :parse-error
           :sanity-errors))

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

(defn format-sanity-error [err]
  (let [{:keys [text file line]} (:meta err)
        sanity (:sanity-errors err)]
    (format-message
     ["File: " file]
     ["Line: " line]
     ""
     text
     ""
     "Failed sanity checks:"
     (str/join \newline (map :error sanity)))))

(def formatters
  {"Parse" format-parse-error
   "Transform" format-transform-error
   "Sanity" format-sanity-error})

(defn format-error-section [title errors]
  (when (seq errors)
    (format-message
     [title " errors: " (count errors)]
     (str/join "\n\n" (map (formatters title) errors))
     "")))

(defn format-errors [errors successes merged]
  (let [t-errors (filter :transform-error errors)
        p-errors (filter :parse-error errors)
        s-errors (filter :sanity-errors errors)]
    (format-message
     (format-error-section "Parse" p-errors)
     (format-error-section "Transform" t-errors)
     (format-error-section "Sanity" s-errors)
     (if (seq merged)
       ["Successful datums: " (count merged)
        " (" (- (count successes) (count merged)) " merged)"]
       ["Successful datums: " (count successes)]))))
