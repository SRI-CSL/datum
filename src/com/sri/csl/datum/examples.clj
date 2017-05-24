(ns com.sri.csl.datum.examples
  (:require [com.sri.csl.datum.parse :refer [parser]]
            [clojure.pprint :refer [pprint]]
            [com.sri.csl.datum.transform.datum :refer [datum]]))

(defn make-test-datum [text]
  (let [ast (parser text)]
    (pprint ast)
    (-> ast
        datum
        (dissoc :meta)
        pprint)))

(def test-datum "  *** NS Nfkb-reporter[Luc] is increased irt PMA + Ionomycin (12 hr)
    *** cells: DT40<RacGap1~null><xRacGap1> in BMLS
    *** unaffected by: xRacGap1(K182A/R183A/R184A) [substitution]
    *** source: 19158271-Fig-4c")

(def test-datum-2 "  *** NS prot-exp[WB] is increased irt UV + TrichostatinA (times)
    *** cells: A549 in BMS
    *** times: 0 2 4+ 8+ 24+ hr
    *** comment: new protein is acetylated on K382 [KAcAb]
    *** source: 9744860-Fig-7")

;;;; Subject examples
;; fully loaded xsprotein
(def subject-full "  *** xBraf(53-42/del(53-42)/(53-42)/pY3/D3D/foobaz)\"Foobar\"{blarg(pY39/floob)}{android(A2)}[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def subject-oprot "  *** rBraf IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def subject-ns "  *** NS IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def subject-chem "  *** Hydrogen[Ab] IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def subject-gene "  *** Sonic-hedgehog IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

;;;; Assay examples
;; Modification Assay
(def assay-mod "  *** NS prot-exp[WB] is increased irt UV + TrichostatinA (times)
    *** cells: A549 in BMS
    *** times: 0 2 4++ 8+ 24+ hr
    *** source: 9744860-Fig-7")

;; Binding Assay
(def assay-binding "  *** Tmem173[Ab] copptby[WB] (Ddx41[Ab] + Ddx40[Ab]) is unchanged irt c-diAMP-transfection (4 hr)
    *** cells: D2SC in BMS
    *** source: 23142775-Fig-5a")

;; IVKA Assay
(def assay-ivka "  *** xBraf[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

;; Activity Assay
(def assay-activity "  *** xCbp[Ab]IP IVHatAct(Histones)[14C-acetyl-CoA] is unchanged irt Thrombin (15 min)
    *** cells: cEFs<xEp300> in BMLS \"nuclear-extracts\"
    *** source: 12514134-Fig-5c")

;; locatedin
(def assay-locatedin "  *** Cbp[Ab] locatedin(cytoplasm)[IHC] is increased irt TrichostatinA (48 hr)
    *** cells: SKMEL37 in BMS
    *** source: 16481475-Fig-6c")

;; phos
(def assay-phos "  *** xRsks[Ab]IP STphos[32Pi] is increased irt Vanadate (5 min)
    *** cells: SWISS3T3 in BMLS
    *** source: 1848664-Fig-4")

;;;; Treatment examples
;; +

(def treatment-plus "  *** Jnks[phosAb] phos(TPY)[phosAb] is increased irt anti-Cd3 + anti-Cd28 (10 min)
    *** cells: JURKAT<Pten~null> in BMLS
    *** source: 15258589-Fig-2e")

(def treatments-k "  *** Accs[phosAb] phos(MSGLH)[phosAb] is increased itao Akt1 and Akt2 [DKO]
    *** cells: mEFs in BMLS
    *** source: 16027121-Fig-2d")

;;;; Environment Examples
;;
(def env-none "  *** Braf[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: none
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def env-null-mut "  *** Braf[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def env-o-mut "  *** Braf[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt anti-Cd3 + anti-IgG (times)
    *** cells: JURKAT<xRsks> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** source: 15339934-Fig-2c")

(def env-string "  *** S6k1[phosAb] phos(T412)[phosAb] is increased irt AminoAcids + Glucose (30 min)
    *** cells: MIN6 in BMLS \"Glucose and AminoAcid deprived\"
    *** source: 17287212(D)")

;;;; Extras
(def extra-addition "  *** S6k1[phosAb] phos(T412)[phosAb] is increased irt AminoAcids + Glucose (30 min)
    *** cells: MIN6 in BMLS \"Glucose and AminoAcid deprived\"
    *** unaffected by: Ins or Ice (tnr) [addition]
    *** source: 17287212(D)")

(def extra-stim-times "  *** S6k1[phosAb] phos(T412)[phosAb] is increased irt AminoAcids + Glucose (30 min)
    *** cells: MIN6 in BMLS \"Glucose and AminoAcid deprived\"
    *** unaffected by: Ins (1, 2, 3, 4 hr) [addition]
    *** source: 17287212(D)")

(def extra-substitution "  *** xPdpk1[tAb]IP Yphos[pYAb] is detectable-but unchanged irt AngII (5 min)
    *** cells: CHO<xAgtR1><xPdpk1><xSrc(mnr)\"CA\"> in BMS
    *** unaffected by: xFak2(K457A) [substitution]
    *** source: 14585963-Fig-5e")

(def extra-reqs-k "  *** xPdpk1[tAb]IP Yphos[pYAb] is increased irt AngII (5 min)
    *** cells: CHO<xAgtR1><xPdpk1><xFak2> in BMS
    *** reqs: xFak2 [omission]
    *** source: 14585963-Fig-3b")

(def extra-reqs-both "  *** Raf1[Ab]IP IVKA(Mek1)[32P-ATP] is increased irt Fn1-adherence + PP2 (times)
    *** cells: REF52 in BMS
    *** times: 0 5+ 10+ min
    *** reqs: both [omission]
    *** source: 12876277-Fig-8a")

;;;; Misc
;; Oligo

(def misc-oligo "  *** Stat6[Ab] oligo-binding[EMSA] is unchanged irt IL3 (15 min)
    *** cells: CTFL15 in BMS
    *** oligo: \"beta-casein GAS\" 5-AGATTTCTAGGAATTCAAATC-3
    *** source: 10072514-Fig-2a")

(def misc-ipfrom "  *** xRaf1(306-648)[tAb]IP IVKA(Mek12s)(SMANS)[phosAb] is decreased by PLX4032
    *** cells: none
    *** IPfrom: HEK293H<xRaf1(306-648)> in BMS
    *** source: 20179705-Fig-S8b")

(def misc-ip2from "  *** rRac1{GTPgS} boundby[WB] xMlk3(K144R)[tAb]IP is detectable
    *** cells: none
    *** IP1from: HEK293T in BMS
    *** IP2from: HEK293T in BMS
    *** inhibited by: rRac1{GDP} [substitution]
    *** source: 18851832-Fig-S5")
