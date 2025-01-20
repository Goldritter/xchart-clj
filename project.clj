(defproject com.github.goldritter/xchart-clj "0.1.0.a-SNAPSHOT"
  :description "Clojure wrapper to use Xchart in a Highcharts manner."
  :url "https://github.com/Goldritter/xchart-clj"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"
            :comment "MIT License"
            :year 2025
            :key "mit"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.knowm.xchart/xchart "3.8.8"]
                 [commons-io/commons-io "2.18.0"]
                 [org.apache.commons/commons-math3 "3.6.1"]]
  :plugins [[lein-license "1.0.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
