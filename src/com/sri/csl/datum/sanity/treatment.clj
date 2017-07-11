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
  (let [nth-extra (second path)
        extra (get-in datum [:extras nth-extra])
        treatments (get-in extra [:treatment :treatments])]
    (when (some #(not (#{"expressed" "recombinant"}
                       (:origin %))) treatments)
      "Substitution treatments must be expressed or recombinant.")))

(def checkers
  [[(check/postfix ["expressed" :origin :_ :treatments :treatment :datum])
    expressed-treatment-proteins]

   [(check/postfix [#(not= "expressed" %) :origin :_ :treatments :treatment :datum])
    non-expressed-treatment-proteins]

   [(check/postfix [:sub_mode])
    substitution-rules]])
