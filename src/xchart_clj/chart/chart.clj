(ns xchart-clj.chart.chart
  (:require [xchart-clj.chart.axis :refer :all]
            [xchart-clj.chart.series :as SERIES]
            [xchart-clj.util.helper :refer :all])
  (:import
    (java.util Locale)
    (org.knowm.xchart BoxChartBuilder BubbleChartBuilder CategoryChartBuilder DialChartBuilder HeatMapChartBuilder OHLCChartBuilder PieChartBuilder RadarChartBuilder XYChartBuilder)
    (org.knowm.xchart.style BoxStyler$BoxplotCalCulationMethod Styler$ChartTheme Styler$LegendPosition)))


(def box-plot-calculation-methods {"n-less-1"        BoxStyler$BoxplotCalCulationMethod/N_LESS_1
                                   "n-less-1-plus-1" BoxStyler$BoxplotCalCulationMethod/N_LESS_1_PLUS_1
                                   "n-plus-1"        BoxStyler$BoxplotCalCulationMethod/N_PLUS_1
                                   "np"              BoxStyler$BoxplotCalCulationMethod/NP
                                   "default"         BoxStyler$BoxplotCalCulationMethod/NP})

(def legend-position {"inside-sw" Styler$LegendPosition/InsideSW
                      "inside-n"  Styler$LegendPosition/InsideN
                      "inside-ne" Styler$LegendPosition/InsideNE
                      "inside-nw" Styler$LegendPosition/InsideNW
                      "inside-s"  Styler$LegendPosition/InsideS
                      "inside-se" Styler$LegendPosition/InsideSE
                      "outside-e" Styler$LegendPosition/OutsideE
                      "outside-s" Styler$LegendPosition/OutsideS})

(def chart-types {"xy"       (fn [] (new XYChartBuilder))
                  "category" (fn [] (new CategoryChartBuilder))
                  "pie"      (fn [] (new PieChartBuilder))
                  "bubble"   (fn [] (new BubbleChartBuilder))
                  "dial"     (fn [] (new DialChartBuilder))
                  "radar"    (fn [] (new RadarChartBuilder))
                  "ohlc"     (fn [] (new OHLCChartBuilder))
                  "boxplot"  (fn [] (new BoxChartBuilder))
                  "heat-map" (fn [] (new HeatMapChartBuilder))})

(def theme-styles {"matlab"   Styler$ChartTheme/Matlab
                   "ggplot-2" Styler$ChartTheme/GGPlot2
                   "x-chart"  Styler$ChartTheme/XChart})

(defn get-chart-builder [type] ((get chart-types type)))

(def chart-functions {"title"                            {"visible?" (fn [chart chart-map]
                                                                       (-> (.getStyler chart)
                                                                           (.setChartTitleVisible (get-in chart-map ["visible?"] true)))
                                                                       chart)}

                      "legend"                           {"position" (fn [chart chart-map]
                                                                       (-> (.getStyler chart)
                                                                           (.setLegendPosition (get legend-position (get-in chart-map ["position"] "outside-e"))))
                                                                       chart)}

                      "default-series-render-style"      (fn [chart chart-map]
                                                           (let [styler-name (.getCanonicalName (class (.getStyler chart)))]
                                                             (if (contains? SERIES/series-render-styles styler-name)
                                                               (-> (.getStyler chart)
                                                                   (.setDefaultSeriesRenderStyle
                                                                     (get-in SERIES/series-render-styles [styler-name (get-in chart-map ["default-series-render-style"] "default")])))
                                                               nil)
                                                             chart))

                      "marker"                           {"size" (fn [chart chart-map]
                                                                   (-> (.getStyler chart)
                                                                       (.setMarkerSize (get-in chart-map ["size"])))
                                                                   chart)}

                      "overlapped?"                      (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setOverlapped (get-in chart-map ["overlapped?"] false)))
                                                           chart)

                      "plot-margin"                      (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setPlotMargin (get-in chart-map ["plot-margin"])))
                                                           chart)
                      "plot-content-size"                (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setPlotContentSize (get-in chart-map ["plot-content-size"])))
                                                           chart)
                      "box-plot-calculation-method"      (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setBoxplotCalCulationMethod ((get-in chart-map ["box-plot-calculation-method"] "default")) box-plot-calculation-methods))
                                                           chart)


                      "plot-background-color"            (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setPlotBackgroundColor (get-color (get series-map "plot-background-color"))))
                                                           chart)

                      "plot-gridlines-color"             (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setPlotGridLinesColor (get-color (get series-map "plot-gridlines-color"))))
                                                           chart)

                      "chart-background-color"           (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setChartBackgroundColor (get-color (get series-map "chart-background-color"))))
                                                           chart)
                      "legend-background-color"          (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setLegendBackgroundColor (get-color (get series-map "legend-background-color"))))
                                                           chart)
                      "chart-font-color"                 (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setChartFontColor (get-color (get series-map "chart-font-color"))))
                                                           chart)
                      "chart-title-box-background-color" (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setChartTitleBoxBackgroundColor (get-color (get series-map "chart-title-box-background-color"))))
                                                           chart)

                      "chart-title-box-border-color"     (fn [chart series-map]
                                                           (-> (.getStyler chart)
                                                               (.setChartTitleBoxBorderColor (get-color (get series-map "chart-title-box-border-color"))))
                                                           chart)


                      "axis-titles-visible?"             (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAxisTitlesVisible (get-in chart-map ["axis-titles-visible?"] true)))
                                                           chart)
                      "chart-title-box-visible?"         (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setChartTitleBoxVisible (get-in chart-map ["chart-title-box-visible?"] true)))
                                                           chart)
                      "plot-grid-lines-visible?"         (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setPlotGridLinesVisible (get-in chart-map ["plot-grid-lines-visible?"] true)))
                                                           chart)
                      "axis-tick-padding"                (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAxisTickPadding (get-in chart-map ["axis-tick-padding"])))
                                                           chart)

                      "axis-tick-mark-length"            (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAxisTickMarkLength (get-in chart-map ["axis-tick-mark-length"])))
                                                           chart)

                      "date-pattern"                     (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setDatePattern (get-in chart-map ["date-pattern"])))
                                                           chart)
                      "decimal-pattern"                  (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setDecimalPattern (get-in chart-map ["decimal-pattern"])))
                                                           chart)
                      "locale"                           (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setLocale (Locale/forLanguageTag (get-in chart-map ["locale"]))))
                                                           chart)

                      "legend-font"                      (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setLegendFont (get-font (get-in chart-map ["legend-font"]))))
                                                           chart)
                      "axis-title-font"                  (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAxisTitleFont (get-font (get-in chart-map ["axis-title-font"]))))
                                                           chart)
                      "axis-tick-labels-font"            (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAxisTickLabelsFont (get-font (get-in chart-map ["axis-tick-labels-font"]))))
                                                           chart)

                      "series-colors"                    (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setSeriesColors
                                                                 (to-array (map get-color (get-in chart-map ["series-colors"])))))
                                                           chart)
                      "annotations?"                     (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setHasAnnotations (get-in chart-map ["annotations?"] false)))
                                                           chart)
                      "available-space-fill"             (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setAvailableSpaceFill (get-in chart-map ["available-space-fill"])))
                                                           chart)

                      "tooltip?"                         (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setToolTipsEnabled (get-in chart-map ["tooltip?"] false)))
                                                           chart)

                      "tooltip-visible?"                 (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setToolTipsAlwaysVisible (get-in chart-map ["tooltip-visible?"] false)))
                                                           chart)

                      "cursor?"                          (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setCursorEnabled (get-in chart-map ["cursor?"] false)))
                                                           chart)

                      "cursor-color"                     (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setCurserColor (get-in chart-map ["cursor-color"] "black")))
                                                           chart)

                      "cursor-line-width"                (fn [chart chart-map]
                                                           (-> (.getStyler chart)
                                                               (.setCurserLineWidth (get-in chart-map ["cursor-line-width"] 10.0)))
                                                           chart)

                      })

(defn adjust-chart
  ([chart chart-map]
   (reduce #(if (map? (val %2))
              (adjust-chart %1 (get chart-map (key %2)) (val %2))
              ((val %2) %1 chart-map))
           chart
           (select-keys chart-functions (keys chart-map))))

  ([chart inner-chart-map inner-function-map]
   (reduce #(if (map? (val %2))
              (adjust-chart %1 (get inner-chart-map (key %2)) (val %2))
              ((val %2) %1 inner-chart-map))
           chart
           (select-keys inner-function-map (keys inner-chart-map)))))

(defn adjust-axis
  [chart axis-map used-axis]
  (reduce #(if (map? (val %2))
             (adjust-chart %1 (get axis-map (key %2)) (val %2))
             ((val %2) %1 axis-map))
          chart
          (select-keys (get axis used-axis) (keys axis-map))))

(defn generate-chart [chart-map]
  (let [used-chart-map (transient (keyword->string chart-map))
        chart (-> (get-chart-builder (get-in used-chart-map ["chart" "type"]))
                  (.width (get-in used-chart-map ["chart" "width"] 600))
                  (.height (get-in used-chart-map ["chart" "height"] 800))
                  (.title (get-in used-chart-map ["chart" "title" "name"]))
                  (.xAxisTitle (get-in used-chart-map ["x-axis" "title" "name"]))
                  (.yAxisTitle (get-in used-chart-map ["y-axis" "title" "name"]))
                  (.theme (get theme-styles (get-in used-chart-map ["chart" "theme"] "matlab")))
                  (.build))]

    (adjust-chart chart (get used-chart-map "chart"))
    (adjust-axis chart (get used-chart-map "x-axis") "x-axis")
    (adjust-axis chart (get used-chart-map "y-axis") "y-axis")
    (doall (map #(SERIES/add-series chart %) (get used-chart-map "series")))
    chart))