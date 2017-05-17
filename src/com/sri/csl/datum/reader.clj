(ns com.sri.csl.datum.reader
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.string :as string]))

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
       :text line}

      (datum-line? line)
      {:text line}

      true {})))

(defn build-datum
  "Builds a merged datum from a collection of tagged lines"
  [lines]
  (assoc (first lines)
         :text (str/join "\n" (map :text lines))))

(defn xf
  "Builds a transducer pipeline to extract datums from a sequence
  of lines and tag them with line number and filename."
  [filename]
  (comp (map-indexed (number-line filename))
        (filter :text)
        (partition-by :line)
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
   (string/ends-with? (.getName file)
                      ".txt")))

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
