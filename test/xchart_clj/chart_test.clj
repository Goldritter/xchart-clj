(ns xchart-clj.chart-test
  (:require [clojure.test :refer :all]
            [xchart-clj.chart.chart :refer :all]
            [xchart-clj.chart.plot :refer :all])
  (:import (org.apache.commons.math3.distribution NormalDistribution GammaDistribution BinomialDistribution)))

;; =====================================================================
;; Datenquellen
;; =====================================================================

(def
  ^{:doc "A map defining various Gaussian (Normal) distributions.
         These objects are used as data sources for generating sample data
         and density curves for the test charts."}
  normal-disitributions {"μ: 0.5, σ: 0.1"  (new NormalDistribution 0.5 0.1 0.001)
                         "μ: 0.75, σ: 0.1" (new NormalDistribution 0.75 0.1 0.001)
                         "μ: 0.3, σ: 0.2"  (new NormalDistribution 0.3 0.2 0.001)})

(def
  ^{:doc "A map defining Gamma distributions to test area styles."}
  gamma-distributions {"shape: 2.0, scale: 2.0" (new GammaDistribution 2.0 2.0)
                       "shape: 3.0, scale: 2.0" (new GammaDistribution 3.0 2.0)})

(def
  ^{:doc "A map defining Binomial distributions to test category/bar styles."}
  binomial-distributions {"n: 20, p: 0.5" (new BinomialDistribution 20 0.5)
                          "n: 20, p: 0.7" (new BinomialDistribution 20 0.7)})


;; =====================================================================
;; Verwendungsbeispiele (Diagramm-Konfigurationen)
;; =====================================================================

;; 1. XY-Chart: Standard Linien mit Spline-Interpolation
(def gaussian-density
  {:chart  {:title                       {:name "Gaussian Density"}
            :width                       800
            :height                      800
            :type                        :xy
            :legend-font                 {:name "SansSerif" :style :bold :size 12}
            :marker                      {:size 0.0}
            :default-series-render-style :line
            :legend                      {:position :outside-s}
            :theme                       :matlab}
   :x-axis {:title {:name "Value"}
            :min   -0.4
            :max   1.2}
   :y-axis {:title {:name "Density"}}

   :series [{:name            "μ: 0.5, σ: 0.1"
             :smooth?         true
             :show-in-legend? true
             :data            (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.5, σ: 0.1"))))}
            {:name            "μ: 0.3, σ: 0.2"
             :smooth?         true
             :show-in-legend? true
             :data            (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.3, σ: 0.2"))))}
            {:name            "μ: 0.75, σ: 0.1"
             :smooth?         true
             :show-in-legend? true
             :data            (doall (map #(vector %1 (.density %2 %1)) (range -0.5 1.5 0.001) (repeat (get normal-disitributions "μ: 0.75, σ: 0.1"))))}]})

;; 2. Boxplot: Generiert aus Rohdaten-Samples
(def gaussian-boxplot
  {:chart  {:title                       {:name "Gaussian Boxplot"}
            :width                       800
            :height                      800
            :type                        :boxplot
            :default-series-render-style :line
            :legend                      {:position :outside-s}
            :theme                       :matlab}
   :x-axis {:title {:name "Distributions"}}
   :y-axis {:title {:name "Values"}}

   :series [{:name            "μ: 0.5, σ: 0.1"
             :show-in-legend? true
             :data            (.sample (get normal-disitributions "μ: 0.5, σ: 0.1") 1000)}
            {:name            "μ: 0.3, σ: 0.2"
             :show-in-legend? true
             :data            (.sample (get normal-disitributions "μ: 0.3, σ: 0.2") 1000)}
            {:name            "μ: 0.75, σ: 0.1"
             :show-in-legend? true
             :data            (.sample (get normal-disitributions "μ: 0.75, σ: 0.1") 1000)}]})

;; 3. Category-Chart: Bar-Style, ggplot-2 Theme und Annotations
(def binomial-category
  {:chart {:title        {:name "Binomial Distribution (Bar Chart)"}
           :width        800
           :height       600
           :type         :category
           :theme        :ggplot-2
           :overlapped?  false}
   :x-axis {:title {:name "Number of Successes"}}
   :y-axis {:title {:name "Probability"}}
   :series [{:name         "n: 20, p: 0.5"
             :render-style :bar
             :data         (doall (map #(vector % (.probability (get binomial-distributions "n: 20, p: 0.5") %)) (range 0 21)))}
            {:name         "n: 20, p: 0.7"
             :render-style :bar
             :data         (doall (map #(vector % (.probability (get binomial-distributions "n: 20, p: 0.7") %)) (range 0 21)))}]})

;; 4. XY-Chart: Area-Style mit Transparenz (RGBA Farbcodes) und interner Legende
(def gamma-area
  {:chart {:title                       {:name "Gamma Distribution (Area Chart)"}
           :width                       800
           :height                      600
           :type                        :xy
           :theme                       :x-chart
           :default-series-render-style :area
           :plot-gridlines-color        "lightGray"
           :legend                      {:position :inside-nw}}
   :x-axis {:title {:name "x"}}
   :y-axis {:title {:name "Density"}}
   :series [{:name       "shape: 2.0, scale: 2.0"
             :fill-color [0 150 255 100] ; 100 = Alpha-Wert für Transparenz
             :data       (doall (map #(vector % (.density (get gamma-distributions "shape: 2.0, scale: 2.0") %)) (range 0.1 15.0 0.1)))}
            {:name       "shape: 3.0, scale: 2.0"
             :fill-color [255 100 0 100]
             :data       (doall (map #(vector % (.density (get gamma-distributions "shape: 3.0, scale: 2.0") %)) (range 0.1 15.0 0.1)))}]})

;; 5. XY-Chart: Streudiagramm (Scatter) mit benutzerdefinierten Markern und Tooltips
(def scatter-plot
  {:chart {:title                       {:name "Scatter Plot Demo"}
           :width                       800
           :height                      600
           :type                        :xy
           :theme                       :matlab
           :plot-background-color       "white"}
   :x-axis {:title {:name "X-Axis"}
            :min   -2.0
            :max   2.0}
   :y-axis {:title {:name "Y-Axis"}}
   :series [{:name         "Random Scatter"
             :render-style :scatter
             :marker       :cross
             :marker-color "black"
             :data         (doall (map #(vector % (+ % (* (- (rand) 0.5) 1.5))) (range -2.0 2.0 0.1)))}]})

;; 6. Dual-Axis Chart: Demonstration mehrerer Y-Achsen und gestrichelter Linien
(def dual-axis-chart
  {:chart {:title  {:name "Dual Y-Axis Chart"}
           :width  800
           :height 600
           :type   :xy
           :theme  :x-chart}
   :series [{:name         "Linear Growth (Left Axis)"
             :y-axis-group 0
             :render-style :line
             :line-style   :dash-dash
             :data         [[1 10] [2 20] [3 30] [4 40]]}
            {:name         "Exponential Growth (Right Axis)"
             :y-axis-group 1
             :render-style :line
             :line-style   :solid
             :data         [[1 10] [2 100] [3 1000] [4 10000]]}]})

;; 7. Fortgeschrittene Formatierung: Achsen-Rotation, Padding, Zahlenformate und Java-Farbkonstanten
(def custom-styled-category
  {:chart {:title                    {:name "Custom Styling" :visible? true}
           :width                    800
           :height                   600
           :type                     :category
           :plot-margin              10
           :axis-titles-visible?     false
           :plot-grid-lines-visible? false
           :chart-background-color   "lightGray"} ; Nutzt java.awt.Color/lightGray via Reflection
   :x-axis {:label             {:rotation 45}
            :axis-tick-padding 5}
   :y-axis {:decimal-pattern "#,###.00"}
   :series [{:name         "Q1 Sales"
             :render-style :bar
             :fill-color   "darkGray"
             :data         [["January" 1234.56] ["February" 2345.67] ["March" 1987.65]]}]})

;; =====================================================================
;; Generierungs- & Speicherfunktionen
;; =====================================================================

(defn generate-and-save-chart
  "Generates an XChart object from the provided map and immediately saves it to a file.
  The filename is derived from the chart's title defined in the map."
  [chart-map type]
  (save-chart-to-file! (generate-chart chart-map) (get-in chart-map [:chart :title :name]) type))