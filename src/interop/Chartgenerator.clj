(ns interop.Chartgenerator
  (:require [xchart-clj.chart.chart :as c]
            [xchart-clj.chart.plot :as p])
  (:import (java.io OutputStream)
           (java.util Map)
           (java.lang Integer)
           (org.knowm.xchart.internal.chartpart Chart))

  (:gen-class
    :methods [#^{:static true} [generateChart [java.util.Map] org.knowm.xchart.internal.chartpart.Chart]
              #^{:static true} [generateSvgString [org.knowm.xchart.internal.chartpart.Chart] String]
              #^{:static true} [saveToOutputStream [org.knowm.xchart.internal.chartpart.Chart java.io.OutputStream String] void]
              #^{:static true} [saveToOutputStream [org.knowm.xchart.internal.chartpart.Chart java.io.OutputStream String Integer] void]
              #^{:static true} [saveToFile [org.knowm.xchart.internal.chartpart.Chart String String] void]
              #^{:static true} [saveToFile [org.knowm.xchart.internal.chartpart.Chart String String Integer] void]
              ]))

(defn -generateChart [chartMap]
  (c/generate-chart chartMap))

(defn -generateSvgString [chart] (p/get-svg-string chart))
(defn -saveToOutputStream [chart output-stream format] (p/save-chart-to-output-stream! chart output-stream (keyword format)))
(defn -saveToOutputStream [chart output-stream format dpi] (p/save-chart-to-output-stream! chart output-stream (keyword format) :dpi dpi))
(defn -saveToFile [chart filename format] (p/save-chart-to-file! chart filename (keyword format)))