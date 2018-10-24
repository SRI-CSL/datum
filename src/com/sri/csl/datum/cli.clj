(ns com.sri.csl.datum.cli
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [com.sri.csl.datum.ops :as ops]))

(defn quit-if-real [return-code]
  (if (str/includes? *file* "cider-repl")
    (throw (Exception. "Invalid args."))
    (System/exit return-code)))

(defn json-assoc [m k v]
  (assoc m
         :json true
         k true
         :json-dest
         (if (= v "--")
           *out*
           (io/writer v))))

(def cli-options
  [["-e" "--errors" "Print errors"
    :id :print-errors]

   ["-g" "--group-errors" "Group sanity errors together"]

   ["-o" "--ops-file FILE" "Provide an external ops json file"
    :id :ops-file
    :default-desc ""
    :parse-fn io/file
    :validate [#(.isFile %) "Ops file not found."]]

   ["-j" "--json FILE" "Print parsed datums as JSON"
    :assoc-fn json-assoc]
   ["-J" "--pretty-json FILE" "Pretty-print parsed datums as JSON"
    :id :json-pretty
    :assoc-fn json-assoc]
   ["-D" "--duplicates" "Print a list of duplicate datums"]
   ["-m" "--merge" "Merge datums that only differ in extras."
    :id :merge-related]])

(defn usage [summary]
  (->> ["Usage: java -jar datum.jar [options] [files/directories]"
        ""
        "Options:"
        summary]
       (str/join \newline)))

(defn error-message [summary errors]
  (->> ["Error: "
        (str/join \newline errors)
        ""
        (usage summary)]
       (str/join \newline)))

(defn parse [args]
  (let [{:keys [errors summary arguments options] :as opts}
        (cli/parse-opts args cli-options)]
    (when errors
      (println (error-message summary errors))
      (quit-if-real 1))

    (when (empty? arguments)
      (println (usage summary))
      (quit-if-real 0))

    (when (:ops-file options)
      (try
        (ops/load-ops! (:ops-file options))
        (catch Exception e
          (println "Failed to parse ops file.")
          (quit-if-real 1))))

    opts))
