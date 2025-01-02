(ns xchart-clj.chart.chart
  (:require [xchart-clj.chart.series :as SERIES]
            [xchart-clj.util.helper :refer :all])
  (:import
   (org.knowm.xchart PieChartBuilder BubbleChartBuilder HeatMapChartBuilder BoxChartBuilder OHLCChartBuilder RadarChartBuilder DialChartBuilder BitmapEncoder BitmapEncoder$BitmapFormat BitmapEncoder$BitmapFormat CategoryChartBuilder QuickChart SwingWrapper VectorGraphicsEncoder VectorGraphicsEncoder$VectorGraphicsFormat XYChartBuilder XYSeries$XYSeriesRenderStyle)
   (org.knowm.xchart.style BoxStyler$BoxplotCalCulationMethod Styler$ChartTheme Styler$LegendPosition)))


(def box-plot-calculation-methods {:n-less-1 BoxStyler$BoxplotCalCulationMethod/N_LESS_1
                                   :n-less-1-plus-1 BoxStyler$BoxplotCalCulationMethod/N_LESS_1_PLUS_1
                                   :n-plus-1 BoxStyler$BoxplotCalCulationMethod/N_PLUS_1
                                   :np BoxStyler$BoxplotCalCulationMethod/NP
                                   :default BoxStyler$BoxplotCalCulationMethod/NP})

(def legend-position {:inside-sw Styler$LegendPosition/InsideSW
                      :inside-n Styler$LegendPosition/InsideN
                      :inside-ne Styler$LegendPosition/InsideNE
                      :inside-nw Styler$LegendPosition/InsideNW
                      :inside-s Styler$LegendPosition/InsideS
                      :inside-se Styler$LegendPosition/InsideSE
                      :outside-e Styler$LegendPosition/OutsideE
                      :outside-s Styler$LegendPosition/OutsideS})

(def chart-types {:xy (fn [] (new XYChartBuilder))
                  :category (fn [] (new CategoryChartBuilder))
                  :pie (fn [] (new PieChartBuilder))
                  :bubble (fn [] (new BubbleChartBuilder))
                  :dial (fn [] (new DialChartBuilder))
                  :radar (fn [] (new RadarChartBuilder))
                  :ohlc (fn [] (new OHLCChartBuilder))
                  :boxplot (fn [] (new BoxChartBuilder))
                  :heat-map (fn [] (new HeatMapChartBuilder))})

(def theme-styles {:matlab Styler$ChartTheme/Matlab
                   :ggplot-2 Styler$ChartTheme/GGPlot2
                   :x-chart Styler$ChartTheme/XChart})

(defn get-chart-builder [type] ((type chart-types)))

(def chart-functions {:title {:visible? (fn [chart chart-map]
                                          (-> (.getStyler chart)
                                              (.setChartTitleVisible (get-in chart-map [:visible?] true)))
                                          chart)}

                      :legend {:position (fn [chart chart-map]
                                           (-> (.getStyler chart)
                                               (.setLegendPosition ((get-in chart-map [:position] :outside-e) legend-position)))
                                           chart)}

                      :default-series-render-style (fn  [chart chart-map]
                                                     (let [styler-name (.getCanonicalName (class (.getStyler chart)))]
                                                       (if (contains? SERIES/series-render-styles styler-name)
                                                         (-> (.getStyler chart)
                                                             (.setDefaultSeriesRenderStyle
                                                              (get-in SERIES/series-render-styles [styler-name (get-in chart-map [:default-series-render-style] :default)])))
                                                         nil)
                                                       chart))

                      :marker {:size (fn [chart chart-map]
                                       (-> (.getStyler chart)
                                           (.setMarkerSize (get-in chart-map [:marker :size])))
                                       chart)}

                      :overlapped? (fn [chart chart-map]
                                     (-> (.getStyler chart)
                                         (.setOverlapped (get-in chart-map  [:overlapped?] false)))
                                     chart)

                      :box-plot-calculation-method (fn [chart chart-map]
                                                     (-> (.getStyler chart)
                                                         (.setBoxplotCalCulationMethod ((get-in chart-map [:box-plot-calculation-method] :default)) box-plot-calculation-methods))
                                                     chart)})
(defn adjust-chart
  ([chart chart-map]
   (reduce #(if (map? (val %2))
              (adjust-chart %1 ((key %2) chart-map) (val %2))
              ((val %2) chart chart-map))
           chart
           (select-keys chart-functions (keys chart-map))))

  ([chart inner-chart-map inner-function-map]
   (reduce #(if (map? (val %2))
              (adjust-chart %1 ((key %2) inner-chart-map) (val %2))
              ((val %2) chart inner-chart-map))
           chart
           (select-keys inner-function-map (keys inner-chart-map)))))

(defn generate-chart [chart-map]
  (let [used-chart-map (transient chart-map)
        chart (-> (get-chart-builder (get-in used-chart-map [:chart :type]))
                  (.width (get-in used-chart-map [:chart :width] 600))
                  (.height (get-in used-chart-map [:chart :height] 800))
                  (.title (get-in used-chart-map [:chart :title :name]))
                  (.xAxisTitle (get-in used-chart-map [:x-axis :title :name]))
                  (.yAxisTitle (get-in used-chart-map [:y-axis :title :name]))
                  (.theme ((get-in used-chart-map [:chart :theme] :matlab) theme-styles (:matlab theme-styles)))
                  (.build))]

    (adjust-chart chart (:chart used-chart-map))
    (doall (map #(SERIES/add-series chart %) (:series used-chart-map)))
    chart))