(ns xchart-clj.chart.axis
  (:import
    (org.knowm.xchart.style AxesChartStyler$TextAlignment)))

(def axis-alignment {"right"  AxesChartStyler$TextAlignment/Right
                     "centre" AxesChartStyler$TextAlignment/Centre
                     "left"   AxesChartStyler$TextAlignment/Left})


(def axis {"x-axis" {"label" {"alignment" (fn [chart chart-map]
                                         (-> (.getStyler chart)
                                             (.setXAxisLabelAlignment ((get-in chart-map ["alignment"]) axis-alignment)))
                                         chart)
                            "rotation"  (fn [chart chart-map]
                                         (-> (.getStyler chart)
                                             (.setXAxisLabelRotation (get-in chart-map ["rotation"]) axis-alignment))
                                         chart)}
                    "min"   (fn [chart chart-map]
                             (-> (.getStyler chart)
                                 (.setXAxisMin (double (get-in chart-map ["min"]))))
                             chart)
                    "max"   (fn [chart chart-map]
                             (-> (.getStyler chart)
                                 (.setXAxisMax (double (get-in chart-map ["max"]))))
                             chart)}

           "y-axis" {"decimal-pattern" (fn [chart chart-map]
                                       (-> (.getStyler chart)
                                           (.setYAxisDecimalPattern (get-in chart-map ["decimal-pattern"])))
                                       chart)
                    "label"           {"alignment" (fn [chart chart-map]
                                                   (-> (.getStyler chart)
                                                       (.setYAxisLabelAlignment ((get-in chart-map ["alignment"]) axis-alignment)))
                                                   chart)
                                      "rotation"  (fn [chart chart-map]
                                                   (-> (.getStyler chart)
                                                       (.setYAxisLabelRotation (get-in chart-map ["rotation"]) axis-alignment))
                                                   chart)}
                    "logarithmic?"    (fn [chart chart-map]
                                       (-> (.getStyler chart)
                                           (.setYAxisLogarithmic (get-in chart-map ["logarithmic?"] false))
                                           chart))
                    "min"             (fn [chart chart-map]
                                       (-> (.getStyler chart)
                                           (.setYAxisMin (double (get-in chart-map ["min"]))))
                                       chart)
                    "max"             (fn [chart chart-map]
                                       (-> (.getStyler chart)
                                           (.setYAxisMax (double (get-in chart-map ["max"]))))
                                       chart)}})