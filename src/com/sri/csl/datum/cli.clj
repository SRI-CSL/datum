(ns com.sri.csl.datum.cli
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]))

(defn quit-if-real [return-code]
  (if *command-line-args*
    (System/exit return-code)
    (throw (Exception. "Invalid args."))))

(def cli-options
  [["-e" "--errors" "Print errors"
    :id :print-errors]

   ["-o" "--ops-file FILE" "Provide an external ops json file"
    :id :ops-file
    :default (io/resource "datumsorts.json")
    :default-desc ""
    :parse-fn io/file]

   ["-j" "--json" "Print parsed datums as JSON"] 
   ["-D" "--duplicates" "Print a list of duplicate datums"]])

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
  (let [{:keys [errors summary arguments] :as options}
        (cli/parse-opts args cli-options)]
    (when errors
      (println (error-message summary errors))
      (quit-if-real 1))

    (when (empty? arguments)
      (println (usage summary))
      (quit-if-real 0))

    options))
