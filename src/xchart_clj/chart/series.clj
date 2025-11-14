(ns xchart-clj.chart.series
  (:require [xchart-clj.util.helper :refer :all])
  (:import
    (org.knowm.xchart BubbleSeries$BubbleSeriesRenderStyle CategorySeries$CategorySeriesRenderStyle OHLCSeries$OHLCSeriesRenderStyle PieSeries$PieSeriesRenderStyle XYSeries$XYSeriesRenderStyle)
    (org.knowm.xchart.style.lines SeriesLines)
    (org.knowm.xchart.style.markers SeriesMarkers)))

(def series-render-styles {"org.knowm.xchart.style.XYStyler"       {"line"         XYSeries$XYSeriesRenderStyle/Line
                                                                    "area"         XYSeries$XYSeriesRenderStyle/Area
                                                                    "step"         XYSeries$XYSeriesRenderStyle/Step
                                                                    "step-area"    XYSeries$XYSeriesRenderStyle/StepArea
                                                                    "polygon-area" XYSeries$XYSeriesRenderStyle/PolygonArea
                                                                    "scatter"      XYSeries$XYSeriesRenderStyle/Scatter
                                                                    "default"      XYSeries$XYSeriesRenderStyle/Line}
                           "org.knowm.xchart.style.CategoryStyler" {"line"       CategorySeries$CategorySeriesRenderStyle/Line
                                                                    "area"       CategorySeries$CategorySeriesRenderStyle/Area
                                                                    "scatter"    CategorySeries$CategorySeriesRenderStyle/Scatter
                                                                    "steppedBar" CategorySeries$CategorySeriesRenderStyle/SteppedBar
                                                                    "bar"        CategorySeries$CategorySeriesRenderStyle/Bar
                                                                    "stick"      CategorySeries$CategorySeriesRenderStyle/Stick
                                                                    "default"    CategorySeries$CategorySeriesRenderStyle/Line}
                           "org.knowm.xchart.style.PieStyler"      {"pie"     PieSeries$PieSeriesRenderStyle/Pie
                                                                    "donut"   PieSeries$PieSeriesRenderStyle/Donut
                                                                    "default" PieSeries$PieSeriesRenderStyle/Pie}
                           "org.knowm.xchart.style.BubbleStyler"   {"default" BubbleSeries$BubbleSeriesRenderStyle/Round
                                                                    "round"   BubbleSeries$BubbleSeriesRenderStyle/Round}
                           "org.knowm.xchart.style.OHLCStyler"     {"default" OHLCSeries$OHLCSeriesRenderStyle/Line
                                                                    "line"    OHLCSeries$OHLCSeriesRenderStyle/Line
                                                                    "candle"  OHLCSeries$OHLCSeriesRenderStyle/Candle
                                                                    "hi-lo"   OHLCSeries$OHLCSeriesRenderStyle/HiLo}})


(def series-lines {"dash-dash" SeriesLines/DASH_DASH
                   "dash-dot"  SeriesLines/DASH_DOT
                   "dot-dot"   SeriesLines/DOT_DOT
                   "none"      SeriesLines/NONE
                   "solid"     SeriesLines/SOLID})

(def series-marker {"cirlce"        SeriesMarkers/CIRCLE
                    "cross"         SeriesMarkers/CROSS
                    "diamond"       SeriesMarkers/DIAMOND
                    "none"          SeriesMarkers/NONE
                    "oval"          SeriesMarkers/OVAL
                    "plus"          SeriesMarkers/PLUS
                    "rectangle"     SeriesMarkers/RECTANGLE
                    "square"        SeriesMarkers/SQUARE
                    "trapezoid"     SeriesMarkers/TRAPEZOID
                    "triangle-down" SeriesMarkers/TRIANGLE_DOWN
                    "triangle-up"   SeriesMarkers/TRIANGLE_UP})

(def series {"render-style"    (fn [series series-map]
                                 (.setChartXYSeriesRenderStyle series
                                                               (get-in series-render-styles ["org.knowm.xchart.style.XYStyler" (get series-map "render-style")]))
                                 series)
             "label"           (fn [series series-map]
                                 (.setLabel series (get series-map "label"))
                                 series)
             "marker"          (fn [series series-map]
                                 (.setMarker series ((get series-map "marker" "none") series-marker))
                                 series)
             "y-axis-group"    (fn [series series-map]
                                 (.setYAxisGroup series (get series-map "y-axis-group"))
                                 series)
             "smooth?"         (fn [series series-map]
                                 (.setSmooth series (get series-map "smooth?" false))
                                 series)
             "show-in-legend?" (fn [series series-map]
                                 (.setShowInLegend series (get series-map "show-in-legend?" true))
                                 series)
             "enabled?"        (fn [series series-map]
                                 (.setEnabled series (get series-map "enabled?" true))
                                 series)
             "line-widht"      (fn [series series-map]
                                 (.setLineWidth series (float (series-map "line-width" 1.0)))
                                 series)
             "line-style"      (fn [series series-map]
                                 (.setLineStyle series (get series-lines (series-map "line-style" "solid")))
                                 series)
             "line-color"      (fn [series series-map]
                                 (.setLineColor series (get-color (get series-map "line-color")))
                                 series)
             "marker-color"    (fn [series series-map]
                                 (.setMarkerColor series (get-color (get series-map "marker-color")))
                                 series)
             "fill-color"      (fn [series series-map]
                                 (.setFillColor series (get-color (get series-map "fill-color")))
                                 series)})

(defn add-series [chart series-map]
  (reduce
    #(%2 %1 series-map)
    (if (instance? org.knowm.xchart.BoxChart chart)
      (.addSeries chart (get series-map "name") (get series-map "data"))
      (.addSeries chart (get series-map "name") (map first (get series-map "data"))
                  (map second (get series-map "data"))))
    (vals (select-keys series (keys series-map)))))

  