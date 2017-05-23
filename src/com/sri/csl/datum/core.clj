(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [com.sri.csl.datum.sanity.core :as sanity]
            [com.sri.csl.datum
             [parse :as parse]
             [errors :as errors]
             [reader :as reader]
             [cli :as cli]]
            [clojure.data.json :as json]))

(defn load-datums [file]
  (->> file
       reader/extract
       (pmap parse/parse-datum)
       (pmap sanity/check)))


(defn -main [& args]
  (let [{arguments :arguments
         {:keys [ops-file print-errors json duplicates]} :options}
        (cli/parse args)

        results (mapcat load-datums arguments)]
    (when print-errors
      (println (errors/format-errors results)))
    (when json
      (println (json/write-str results)))

    (System/exit 0)))
