(ns xchart-clj.chart-test
  (:require [clojure.test :refer :all]
            [xchart-clj.chart.chart :refer :all]
            [xchart-clj.chart.plot :refer :all])
  (:import (org.apache.commons.math3.distribution NormalDistribution GammaDistribution BinomialDistribution)))

(def normal-disitributions {"μ: 0.5, σ: 0.1" (new NormalDistribution 0.5 0.1 0.001)
                            "μ: 0.75, σ: 0.1" (new NormalDistribution 0.75 0.1 0.001)
                            "μ: 0.3, σ: 0.2" (new NormalDistribution 0.3 0.2 0.001)})



(def gaussian-density
  {:chart {:title {:name "Gaussian Density"}
           :width 800
           :height 800
           :type :xy
           :legend-font {:name "SansSerif":style :bolt :size 12}
           :marker {:size 0.0}
           :default-series-render-style :line
           :legend {:position :outside-s}
           :theme :matlab}
   :x-axis {:title {:name "Value"}
            :min -0.4
            :max 1.2}
   :y-axis {:title {:name "Density"}}
   
   :series [{:name "μ: 0.5, σ: 0.1"
             :smooth? true
             :show-in-legend? true
             :data (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.5, σ: 0.1"))))}
            {:name "μ: 0.3, σ: 0.2"
             :smooth? true
             :show-in-legend? true
             :data (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.3, σ: 0.2"))))}
            {:name "μ: 0.75, σ: 0.1"
             :smooth? true
             :show-in-legend? true
             :data (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.75, σ: 0.1"))))}]})


(def gaussian-boxplot
  {:chart {:title {:name "Gaussian Boxplot"}
           :width 800
           :height 800
           :type :boxplot
           :default-series-render-style :line
           :legend {:position :outside-s}
           :theme :matlab}
   :x-axis {:title {:name "Distributions"}}
   :y-axis {:title {:name "Values"}}

   :series [{:name "μ: 0.5, σ: 0.1"
             :show-in-legend? true
             :data (.sample (get normal-disitributions "μ: 0.5, σ: 0.1") 1000)}
            {:name "μ: 0.3, σ: 0.2"
             :show-in-legend? true
             :data (.sample  (get normal-disitributions "μ: 0.3, σ: 0.2") 1000)}
            {:name "μ: 0.75, σ: 0.1"
             :show-in-legend? true
             :data  (.sample (get normal-disitributions "μ: 0.75, σ: 0.1") 1000)}]})



(defn generate-and-save-chart [chart-map]
  (save-chart-to-file (generate-chart chart-map) (get-in chart-map [:chart :title :name]) :svg))