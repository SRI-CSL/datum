(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [com.sri.csl.datum.sanity.core :as sanity]
            [com.sri.csl.datum
             [duplicate :as dup]
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
         {:keys [ops-file print-errors json duplicates merge-related]} :options}
        (cli/parse args)

        results (mapcat load-datums arguments)
        errors (filter errors/error? results)
        successes (remove errors/error? results)
        merged (if merge-related
                 (dup/merge-related successes)
                 successes)]

    (when print-errors
      (println (errors/format-errors errors successes merged)))

    (when duplicates
      (-> successes
          dup/duplicates
          dup/format-duplicates
          println))

    (when json
      (println (json/write-str merged)))

    (System/exit 0)))
