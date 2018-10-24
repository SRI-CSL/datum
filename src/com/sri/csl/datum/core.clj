(ns com.sri.csl.datum.core
  (:gen-class)
  (:require [com.sri.csl.datum.sanity.core :as sanity]
            [com.sri.csl.datum
             [duplicate :as dup]
             [parse :as parse]
             [errors :as errors]
             [reader :as reader]
             [cli :as cli]]
            [cheshire.core :as cheshire]
            [clj-progress.core :as prg]))

(prg/set-progress-bar! ":header :done/:total [:bar] :percent")
(prg/config-progress-bar! :width 45)
(prg/set-throttle! 50)

(defn parse-datums [datums]
  (binding [*out* *err*]
    (let [title "Parsing"
          pbar (prg/init title (count datums))]
      (prg/done (pmap (comp prg/tick parse/parse-datum) datums)))))

(defn run [options]
  (let [{arguments :arguments
         {:keys [print-errors
                 group-errors
                 json json-pretty json-dest
                 duplicates merge-related]} :options}
        options

        raw (mapcat reader/extract arguments)
        results (parse-datums raw)
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

    (when json
      (cheshire/generate-stream merged json-dest {:pretty json-pretty}))

    merged))

(defn -main [& args]
  (run (cli/parse args))
  (System/exit 0))
