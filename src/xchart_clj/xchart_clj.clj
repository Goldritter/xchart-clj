(ns xchart-clj.xchart-clj
  (:require [xchart-clj.chart.chart :as c]
            [xchart-clj.chart.plot :as p])
  (:import (java.io OutputStream)
           (org.knowm.xchart.internal.chartpart Chart)))
(gen-interface
  :name xchartsclj.interface.ChartGenerator
  :methods [[generateChart [^java.util.Map chartMap] Chart]
            [generateSvgString [^Chart chart] String]
            [saveToOutputStream [^Chart chart ^OutputStream output-stream String format] void]
            [saveToOutputStream [^Chart chart ^OutputStream output-stream String format ^Integer dpi] void]
            [saveToFile [^Chart chart ^String filename String format] void]
            [saveToFile [^Chart chart ^String filename String format ^Integer dpi] void]
            ])

(gen-class
  :name xchartsclj.impl.ChartGenerator
  :implements [xchartsclj.interface.ChartGenerator]
  :prefix "impl-")

(defn impl-generateChart [this chartMap]
  (c/generate-chart chartMap))

(defn impl-generateChart [this chart ](p/get-svg-string chart))
(defn impl-saveToOutputStream [this chart output-stream format](p/save-chart-to-output-stream! chart output-stream (keyword format)))
(defn impl-saveToOutputStream [this chart output-stream format dpi](p/save-chart-to-output-stream! chart output-stream (keyword format) :dpi dpi))
(defn impl-saveToFile [this chart filename format](p/save-chart-to-file! chart filename (keyword format)))