(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [com.sri.csl.datum.sanity.core :as sanity]
            [com.sri.csl.datum
             [duplicate :as dup]
             [parse :as parse]
             [errors :as errors]
             [reader :as reader]
             [cli :as cli]]
            [cheshire.core :as cheshire]))

(defn load-datums [file]
  (->> file
       reader/extract
       (pmap parse/parse-datum)))

(defn run [& args]
  (let [{arguments :arguments
         {:keys [print-errors
                 group-errors
                 json pretty-json
                 duplicates merge-related]} :options}
        (cli/parse args)

        results (mapcat load-datums arguments)
        errors (filter errors/error? results)
        successes (remove errors/error? results)
        merged (if merge-related
                 (dup/merge-related successes)
                 successes)]

    (binding [*out* *err*]
      (when print-errors
        (println (errors/format-errors group-errors errors successes merged)))

      (when duplicates
        (-> successes
            dup/duplicates
            dup/format-duplicates
            println)))

    (when (or json pretty-json)
      (println (cheshire/generate-string merged {:pretty pretty-json})))))

(defn -main [& args]
  (apply run args)
  (System/exit 0))
