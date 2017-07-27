(ns com.sri.csl.datum.sanity.treatment
  (:require
   [com.sri.csl.datum.sanity
    [check :as check]]))

(defn expressed-treatment-proteins [datum path node]
  (when (#{"itao" "irt"} (:treatment_type datum))
    (str (:treatment_type datum) " treatments must not be expressed proteins.")))

(defn non-expressed-treatment-proteins [datum path node]
  (when (= (:treatment_type datum) "itpo")
    "itpo treatment proteins must be expressed."))

(defn sub-protein-origin [{:keys [protein origin]}]
  (and
   protein
   (not (#{"expressed" "recombinant"
           "synthetic" "baculovirus"}
         origin))))

(defn substitution-rules [datum path node]
  (let [extra-path [:extras (nth path 2)]
        extra (get-in datum extra-path)
        treatments (get-in extra [:treatment :treatments])]
    (when (some sub-protein-origin treatments)
      "Substitution treatment proteins must be e/r/s/b.")))

(def checkers
  [[(check/postfix ["expressed" :origin :_ :treatments :treatment :datum])
    expressed-treatment-proteins]

   [(check/postfix [#(not= "expressed" %) :origin :_ :treatments :treatment :datum])
    non-expressed-treatment-proteins]

   [(check/postfix ["substitution" :sub_mode])
    substitution-rules]])
