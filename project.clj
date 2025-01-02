(defproject com.github.goldritter/xchart-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"
            :comment "MIT License"
            :year 2025
            :key "mit"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.knowm.xchart/xchart "3.8.8"]
                 [commons-io/commons-io "2.18.0"]]
  :plugins [[lein-license "1.0.0"]]
  :main ^:skip-aot xchart-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
