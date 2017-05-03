(ns com.sri.csl.datum.examples)

(def test-datum "  *** NS Nfkb-reporter[Luc] is increased irt (PMA + Ionomycin) (12 hr)
    *** cells: DT40<RacGap1~null><xRacGap1> in BMLS
    *** unaffected by: xRacGap1(K182A/R183A/R184A) [substitution]
    *** unaffected by: xRacGap1(K199A/K200A) [substitution]
    *** unaffected by: xRacGap1(K182A/R183A/R184A/K199A/K200A) [substitution]
    *** source: 19158271-Fig-4c")

(def test-datum-2 "  *** NS prot-exp[WB] is increased irt (UV + TrichostatinA) (times)
    *** cells: A549 in BMS
    *** times: 0 2 4+ 8+ 24+ hr
    *** comment: new protein is acetylated on K382 [KAcAb]
    *** comment: new protein is acetylated on K320 [KAcAb]
    *** comment: new protein is phosed on S37 [phosAb]
    *** source: 9744860-Fig-7")

;;;; Subject examples
;; fully loaded xsprotein
(def subject-full "  *** xBraf(53-42/del(53-42)/(53-42)/pY3/D3D/foobaz)\"Foobar\"{blarg(pY39/floob)}{android(A2)}[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

(def subject-oprot "  *** rBraf IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

(def subject-ns "  *** NS IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

(def subject-chem "  *** Hydrogen[Ab] IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

(def subject-gene "  *** Sonic-hedgehog IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

;;;; Assay examples
;; Modification Assay
(def assay-mod "  *** NS prot-exp[WB] is increased irt (UV + TrichostatinA) (times)
    *** cells: A549 in BMS
    *** times: 0 2 4+ 8+ 24+ hr
    *** source: 9744860-Fig-7")

;; Binding Assay
(def assay-binding "  *** Tmem173[Ab] copptby[WB] (Ddx41[Ab] + Ddx40[Ab]) is unchanged irt c-diAMP-transfection (4 hr)
    *** cells: D2SC in BMS
    *** source: 23142775-Fig-5a")

;; IVKA Assay
(def assay-ivka "  *** xBraf[Ab]IP IVKA(Mek1)(sitenr)[phosAb] is increased irt (anti-Cd3 + anti-IgG) (times)
    *** cells: JURKAT<Pten~null> in BMS
    *** times: 0 1+ 3+ 30+ 60+ min
    *** enhanced by: anti-Cd28 [cotreatment] no-bkg-control
    *** source: 15339934-Fig-2c")

;; Activity Assay
(def assay-activity "  *** xCbp[Ab]IP IVHatAct(Histones)[14C-acetyl-CoA] is unchanged irt Thrombin (15 min)
    *** cells: cEFs<xEp300> in BMLS \"nuclear-extracts\"
    *** source: 12514134-Fig-5c")

;; locatedin
(def assay-locatedin "  *** Cbp[Ab] locatedin(cytoplasm)[IHC] is increased irt TrichostatinA (48 hr)
    *** cells: SKMEL37 in BMS
    *** source: 16481475-Fig-6c")

;; oligo-binding
(def assay-oligo-binding "  *** Nfkb1[Ab] oligo-binding[EMSA] is decreased irt ValproicAcid (48 hr)
    *** cells: NWMEL1539<xStat1> in BMS
    *** reqs: xStat1 [omission]
    *** comment: no control for total Rela
    *** source: 16481475-Fig-4c")

;; phos
(def assay-phos "  *** xRsks[Ab]IP STphos[32Pi] is increased irt Vanadate (5 min)
    *** cells: SWISS3T3 in BMLS
    *** source: 1848664-Fig-4")

;;;; Treatment examples
;; +

(def treatment-plus "  *** Jnks[phosAb] phos(TPY)[phosAb] is increased irt (anti-Cd3 + anti-Cd28) (10 min)
    *** cells: JURKAT<Pten~null> in BMLS
    *** reqs: Mlk3 [RNAi]
    *** source: 15258589-Fig-2e")

(def treatments-x "")

(def treatments-k "")
