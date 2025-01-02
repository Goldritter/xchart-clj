(ns xchart-clj.chart.axis
  (:import
   (org.knowm.xchart.style Styler$ChartTheme BoxStyler$BoxplotCalCulationMethod AxesChartStyler$TextAlignment Styler$LegendPosition)))

(def axis-alignment {:right  AxesChartStyler$TextAlignment/Right
                     :centre AxesChartStyler$TextAlignment/Centre
                     :left   AxesChartStyler$TextAlignment/Left})


(def x-axis {:label {:alginment ""
                     :rotation ""}
             :min (fn [chart chart-map]
                    (-> (.getStyler chart)
                        (.setXAxisMax (get-in chart-map [:x-axis :min])))
                    chart)
             :max  (fn [chart chart-map]
                     (-> (.getStyler chart)
                         (.setXAxisMin (get-in chart-map [:x-axis :max])))
                     chart)})

(def y-axis {:label {:alginment ""
                     :rotation ""}
             :logarithmic? (fn [chart chart-map]
                             (-> (.getStyler chart)
                                 (.setYAxisLogarithmic (get-in chart-map [:y-axis :logarithmic?] false))
                                 chart))
             :min (fn [chart chart-map]
                    (-> (.getStyler chart)
                        (.setYAxisMax (get-in chart-map [:y-axis :min])))
                    chart)
             :max  (fn [chart chart-map]
                     (-> (.getStyler chart)
                         (.setYAxisMin (get-in chart-map [:x-axis :max])))
                     chart)})