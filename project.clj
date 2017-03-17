(defproject datum "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/algo.generic "0.1.2"]
                 [instaparse "1.4.1"]]
  :main ^:skip-aot com.sri.csl.datum.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
