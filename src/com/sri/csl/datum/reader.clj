(ns com.sri.csl.datum.reader
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn first-line? [line]
  (.startsWith line "  ***"))

(defn datum-line? [line]
  (.startsWith line "    ***"))

(defn number-line [filename]
  (fn [i line]
    (cond
      (first-line? line)
      {:line (inc i)
       :file filename
       :text line
       :first true}

      (datum-line? line)
      {:line (inc i)
       :text line
       :file filename}

      true {})))

(def date-pattern "yyy-MM-dd HH:mm:ss")
(def datestamp
  (-> date-pattern
      (java.text.SimpleDateFormat.)
      (.format (java.util.Date.))))

(defn build-datum
  "Builds a merged datum from a collection of tagged lines"
  [lines]
  (assoc (dissoc (first lines) :first)
         :text (str/join "\n" (map :text lines))
         :date datestamp))

(defn xf
  "Builds a transducer pipeline to extract datums from a sequence
  of lines and tag them with line number and filename."
  [filename]
  (comp (map-indexed (number-line filename))
        (filter :text)
        ;; Only first-lines are tagged with the :line field, so
        ;; partitioning on that field splits the stream into a
        ;; sequence of firstlines followed by the rest of the lines
        ;; for that datum, which we then group up and concatenate
        (drop-while (complement :first))
        (partition-by :first)
        (partition-all 2)
        (map (partial apply concat))

        (map build-datum)))

(defn extract-file
  "Extracts all the datums from a file, returning maps of the form
  {:line X
   :file <filename>
   :text <datum>}"
  [file]
  (let [f (io/file file)
        lines (line-seq (io/reader f))
        filename (.getPath f)]
    (transduce (xf filename)
               conj
               []
               lines)))

(defn text-file? [file]
  (and
   (.isFile file)
   (str/ends-with? (.getName file
                             ".txt"))))

(defn extract-directory
  "Extract and tag all datums from a directory."
  [dirname]
  (->> dirname
       io/file
       file-seq
       (filter text-file?)
       (mapcat extract-file)))

(defn extract [path]
  (let [f (io/file path)]
    (if (.isDirectory f)
      (extract-directory f)
      (extract-file f))))
