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

(defn substitution-rules [datum path node]
  (let [extra-path [:extras (nth path 2)]
        extra (get-in datum extra-path)
        treatments (get-in extra [:treatment :treatments])]
    (when (some #(not (#{"expressed" "recombinant"}
                       (:origin %))) treatments)
      "Substitution treatments must be expressed or recombinant.")))

(def checkers
  [[(check/postfix ["expressed" :origin :_ :treatments :treatment :datum])
    expressed-treatment-proteins]

   [(check/postfix [#(not= "expressed" %) :origin :_ :treatments :treatment :datum])
    non-expressed-treatment-proteins]

   [(check/postfix ["substitution" :sub_mode])
    substitution-rules]])
