# xchart-clj 📊

**xchart-clj** is a declarative **Clojure wrapper** for the Java charting library [**XChart**](https://github.com/knowm/XChart). Inspired by the declarative configuration style of popular charting libraries like Highcharts, this wrapper enables you to build complex, highly customized charts using standard Clojure maps rather than imperative Java builders.

## ✨ Features

* **Declarative Configuration:** Describe your charts entirely using idiomatic Clojure maps (supports both keywords and string keys).
* **Comprehensive Chart Types:** Fully supports XY, Category, Pie, Bubble, Dial, Radar, OHLC, Boxplot, and Heat Map charts.
* **Advanced Series Styling:** Fine-grained control over line widths, styles, colors, markers, and multi-axis configurations (`y-axis-group`).
* **Flexible Export:** Save charts directly to files, write to Java `OutputStream` objects, or generate native SVG strings.

## 🚀 Installation

### Leiningen/Clojars

Add the following dependency to your `project.clj`:

```clojure
[com.github.goldritter/xchart-clj "LATEST_VERSION"]
```

## 💡 Usage

### 1. Chart Generation

The primary entry point is `xchart-clj.chart.chart/generate-chart`. It accepts a single configuration map. Because the library automatically normalizes keys, you can use either Clojure keywords or strings.

Require the core namespaces:

```clojure
(ns my-app.core
  (:require [xchart-clj.chart.chart :as xc]
            [xchart-clj.chart.plot :as xp]))
```

### 2. Example: Idiomatic XY Line Chart

Here is how to configure a smooth XY line chart with customized axis ranges, formatting, and theme styles, then export it as a PNG file:

```clojure
(let [chart-config
      {:chart  {:type "xy"
                :width 800
                :height 600
                :title {:name "Gaussian Distribution Density"}
                :theme "ggplot-2"
                :legend {:position "outside-s"}
                :legend-font {:name "SansSerif" :style "bold" :size 12}
                :plot-gridlines-color "lightGray"}
       
       :x-axis {:title {:name "Value"}
                :min -0.4
                :max 1.2
                :label {:rotation 0}}
       
       :y-axis {:title {:name "Density"}
                :decimal-pattern "#0.00"}
       
       :series [{:name "μ: 0.5, σ: 0.1"
                 :smooth? true
                 :show-in-legend true
                 :line-color "red"
                 :line-style "solid"
                 :marker "circle"
                 :data [[0.1 0.5] [0.3 1.2] [0.5 3.9] [0.7 1.2] [0.9 0.5]]}]}]
  
  (-> (xc/generate-chart chart-config)
      (xp/save-chart-to-file! "gaussian_density.png" :png)))
```

---

### 3. Configuration Reference

#### Chart Settings (Key: `:chart` or `"chart"`)

| Key | Type | Description | Default |
| :--- | :--- | :--- | :--- |
| `:type` | String | **Required.** Type of chart (`"xy"`, `"category"`, `"pie"`, `"boxplot"`, etc.). | - |
| `:width` / `:height` | Integer | Dimensions of the generated chart in pixels. | `600` / `800` |
| `:title` | Map | Contains `:name` (String) and optionally `:visible?` (Boolean). | - |
| `:theme` | String | Theme styles: `"matlab"`, `"ggplot-2"`, or `"x-chart"`. | `"matlab"` |
| `:legend` | Map | Contains `:position` (`"outside-e"`, `"outside-s"`, `"inside-nw"`, etc.). | `"outside-e"` |
| `:plot-background-color` | String/Vector | Color spec for the plot area (e.g., `"white"`, `"#FFFFFF"`, or `[255 255 255]`). | - |
| `:chart-background-color`| String/Vector | Color spec for the outer chart background. | - |
| `:plot-gridlines-color`  | String/Vector | Color of the major grid lines. | - |
| `:plot-grid-lines-visible?`| Boolean | Toggle background grid lines visibility. | `true` |
| `:axis-titles-visible?`  | Boolean | Toggle visibility of all axis titles. | `true` |
| `:overlapped?`           | Boolean | Controls overlapping for category charts (bars/sticks). | `false` |
| `:box-plot-calculation-method`| String | Method for boxplots: `"n-less-1"`, `"n-less-1-plus-1"`, `"n-plus-1"`, `"np"`. | `"default"` |

#### Axis Settings (Keys: `:x-axis` / `:y-axis` or `"x-axis"` / `"y-axis"`)

| Key | Type | Description |
| :--- | :--- | :--- |
| `:title` | Map | Contains `:name` (String) for the axis label text. |
| `:min` / `:max` | Double | Manual boundary override for axis rendering. |
| `:logarithmic?` | Boolean | *(Y-Axis only)* Toggle logarithmic scale. Defaults to `false`. |
| `:decimal-pattern` | String | Custom formatting pattern (e.g., `"#,###.00"`). |
| `:label` | Map | Contains `:rotation` (Integer in degrees) and `:alignment` (`"left"`, `"centre"`, `"right"`). |
| `:axis-tick-padding` | Integer | Margin between ticks and tick labels. |

#### Series Settings (Key: `:series` or `"series"`)

Pass a vector of maps. Each map configures an individual data series:

| Key | Type | Description | Default |
| :--- | :--- | :--- | :--- |
| `:name` | String | **Required.** Name of the data series (appears in the legend). | - |
| `:data` | Vector | **Required.** Collection of coordinates: `[[x1 y1] [x2 y2]]` or single values for boxplots. | - |
| `:render-style` | String | Specific series override (`"line"`, `"scatter"`, `"bar"`, `"area"`, `"step"`, etc.). | Chart default |
| `:line-style` | String | Line stroke type: `"solid"`, `"dash-dash"`, `"dash-dot"`, `"dot-dot"`, `"none"`. | `"solid"` |
| `:line-width` | Float | Thickness of the series rendering line. | `1.0` |
| `:line-color` | String/Vector | Color spec for the stroke. | - |
| `:fill-color` | String/Vector | Color spec for area fills (supports RGBA vectors like `[0 150 255 100]` for transparency). | - |
| `:marker` | String | Shape of data point markers (`"circle"`, `"cross"`, `"diamond"`, `"square"`, `"none"`). | `"none"` |
| `:marker-color` | String/Vector | Color spec for data point markers. | - |
| `:smooth?` | Boolean | Activates spline interpolation for smooth curves *(XY charts only)*. | `false` |
| `:y-axis-group` | Integer | Binds the series to a specific Y-axis group index for multi-axis charts. | `0` |
| `:show-in-legend?` | Boolean | Toggle entry visibility inside the legend box. | `true` |

---

## 4. Exporting Data

The `xchart-clj.chart.plot` namespace handles chart serialization:

```clojure
(require '[xchart-clj.chart.plot :as xp])

;; Save a single chart to file
(xp/save-chart-to-file! chart "output.png" :png)

;; Save with custom DPI to an output stream
(xp/save-chart-to-output-stream! chart my-os :jpg :dpi 300)

;; Get raw SVG markup as string
(let [svg-string (xp/get-svg-string chart)]
  (println svg-string))

;; Save a grid of multiple charts into a single image matrix
(xp/save-charts-to-file! [chart1 chart2] "matrix.png" :png 2 1)
```

---

## 🌐 Java Interoperability (Advanced Usage)

For polyglot applications running on the JVM, **xchart-clj** exposes a static Java interface via the compiled `interop.Chartgenerator` namespace. This allows Java applications to benefit from declarative map configurations (e.g., deserialized from JSON files) without writing extensive builder chains.

### 1. Generating a Chart from Java

```java
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Chart;
import interop.Chartgenerator;

public class ChartService {
    public Chart<?, ?> buildChartFromConfig(Map<String, Object> standardJavaMap) {
        // The Clojure layer normalizes the Java Map and builds the chart
        return Chartgenerator.generateChart(standardJavaMap);
    }
}
```

### 2. Native Java Export Signatures

| Java Method Signature | Description |
| :--- | :--- |
| `static Chart generateChart(Map chartMap)` | Builds a Chart instance from a nested `java.util.Map`. |
| `static String generateSvgString(Chart chart)` | Returns the chart as an SVG formatted XML string. |
| `static void saveToFile(Chart chart, String filename, String format)` | Saves the chart to a file path (e.g., `"png"`, `"svg"`). |
| `static void saveToOutputStream(Chart chart, OutputStream os, String format)` | Streams the graphic directly into a Java `OutputStream`. |

### 3. The "Escape Hatch" Pattern

If a highly specific configuration method from the underlying XChart library is not yet mapped in the Clojure configuration spec, you can safely cast the returned object to its native Java class and use XChart's imperative API directly:

```java
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import interop.Chartgenerator;

Map<String, Object> config = getChartMap();
Chart<?, ?> chart = Chartgenerator.generateChart(config);

if (chart instanceof XYChart) {
    XYChart xyChart = (XYChart) chart;
    // Utilize native XChart features directly
    xyChart.getStyler().setAxisTitlePadding(25);
}

Chartgenerator.saveToFile(chart, "hybrid_chart.png", "png");
```

---

## 🛠️ To-Do List

* **Extended Chart-Type Formatting:** Map dedicated styling options for specific layouts like Pie charts (start angle, inner/outer radius tokens), Dial charts, and Radar properties.
* **Category Chart Spacing:** Add configuration properties for precise bar widths and cluster padding setups inside category stylers.
* **Interactive Tooltips & Zoom:** Build standard support keys for toggling native crosshairs, interactive zoom triggers, and cursor color configurations safely across all platforms.

## License
Distributed under the MIT License. Copyright 2025 Marcus Lindner.