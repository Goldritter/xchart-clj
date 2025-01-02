(ns xchart-clj.chart-test
  (:require [clojure.test :refer :all]
            [xchart-clj.chart.chart :refer :all]
            [xchart-clj.chart.plot :refer :all]))

(def test-chart
  {:chart {:title {:name "My test Chart"}
           :width 500
           :height 500
           :type :xy
           :default-series-render-style :line
           :legend {:position :outside-s}
           :theme :matlab}
   :x-axis {:title {:name "X-Axis"}}
   :y-axis {:title {:name "Y-Axis"}}
   :series [{:name "Series 1"
             :smooth? true
             :show-in-legend? true
             :data [[1 2] [1 3] [1 4] [2 3] [2 4]]}]})