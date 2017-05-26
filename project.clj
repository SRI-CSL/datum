(defproject datum "0.1.0"
  :description "A parser for biology experiment result shorthand."
  :url "http://pl.csl.sri.com/datumkb.html"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/algo.generic "0.1.2"]
                 [instaparse "1.4.5"]
                 [org.clojure/tools.cli "0.3.5"]
                 [cheshire "5.7.1"]]
  :main ^:skip-aot com.sri.csl.datum.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
