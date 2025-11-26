# xchart-clj 📊

**xchart-clj** is a **Clojure wrapper** for the powerful Java charting library [**XChart**](https://github.com/knowm/XChart). Inspired by the declarative configuration style of Highcharts, this wrapper allows you to easily create various chart types in Clojure by using **Clojure Maps for configuration**.

## ✨ Features

  * **Declarative Configuration:** Create complex charts using simple Clojure maps, mirroring a pattern similar to Highcharts.
  * **Broad Chart Support:** Supports all major XChart types (XY, Category, Pie, Bubble, Dial, Radar, OHLC, Boxplot, Heat Map).
  * **Extensive Customization:** Access to styling options for axes, legends, titles, colors, and more.
  * **Flexible Export:** Save charts as bitmap (PNG, GIF, BMP, JPG) or vector graphics (SVG, PDF, EPS).

## 🚀 Installation

### Leiningen

![https://clojars.org/com.github.goldritter/xchart-clj](https://clojars.org/com.github.goldritter/xchart-clj/latest-version.svg)

## 💡 Usage

### 1\. Chart Generation

The core of the library is the function `xchart-clj.chart.chart/generate-chart`, which expects a single Clojure map as an argument to describe the entire chart.

Require the main namespaces in your project:

```clojure
(require '[xchart-clj.chart.chart :as xc]
         '[xchart-clj.chart.plot :as xp])
```

The basic structure of the configuration map (using string keys as shown in your source code) looks like this:

```clojure
{"chart"  {"type" "xy"
           "width" 800
           "height" 600
           "title" {"name" "My Chart Title"}}
 "x-axis" {"title" {"name" "X-Axis Label"}}
 "y-axis" {"title" {"name" "Y-Axis Label"}}
 "series" [{"name" "Series 1"
            "data" [[1 10] [2 20] [3 30]]
            "render-style" "line"}]}
```

### 2\. Example: Simple XY Line Chart

This example creates an **XY Line Chart** and saves it as a PNG file.

```clojure
(let [chart-map
      {"chart" {"type" "xy"
                "width" 800
                "height" 600
                "title" {"name" "Simple XY Chart"}
                "theme" "ggplot-2"
                "legend" {"position" "inside-ne"}}

       "x-axis" {"title" {"name" "Data Points"}
                 "min" 0
                 "max" 5}

       "y-axis" {"title" {"name" "Values"}
                 "logarithmic?" false
                 "decimal-pattern" "#0.00"}

       "series" [{"name" "My Data Series"
                  "data" [[1 10.5] [2 20.2] [3 30.1] [4 40.8] [5 50.0]]
                  "render-style" "line"
                  "marker" "circle"
                  "line-color" "red"}]}

      chart (xc/generate-chart chart-map)]
  (xp/save-chart-to-file! chart "simple_xy_chart.png" :png))
```

### 3\. Customization Options (Highlights)

The map structure mirrors the chart hierarchy. Settings are applied by functions found in the Clojure namespaces (e.g., `xchart-clj.chart.chart/chart-functions` and `xchart-clj.chart.axis/axis`).

#### Chart Settings (Key: `"chart"`)

Controls general properties and styler settings.

| Key | Type | Description | Default |
| :--- | :--- | :--- | :--- |
| `"type"` | String | **Required.** Chart type (`"xy"`, `"category"`, `"pie"`, etc.). | - |
| `"title"`, `"name"` | String | Main chart title. | - |
| `"theme"` | String | Theme style (`"matlab"`, `"ggplot-2"`, `"x-chart"`). | `"matlab"` |
| `"legend"`, `"position"` | String | Legend position (e.g., `"outside-e"`, `"inside-sw"`, see `xchart-clj.chart.chart/legend-position`). | `"outside-e"` |
| `"plot-background-color"` | String/Color | Color of the plot background (e.g., `"black"` or hex code). | - |
| `"tooltip?"` | Boolean | Enable tooltips. | `false` |

#### Axis Settings (Keys: `"x-axis"` and `"y-axis"`)

Controls axis scaling and labels.

| Axis | Key | Type | Description |
| :--- | :--- | :--- | :--- |
| `"x-axis"`/`"y-axis"`| `"title"`, `"name"` | String | Axis title. |
| `"x-axis"`/`"y-axis"`| `"min"`, `"max"` | Double | Manual axis range (min/max). |
| `"y-axis"` | `"logarithmic?"` | Boolean | Use a logarithmic Y-axis scale. | `false` |
| `"x-axis"`/`"y-axis"`| `"label"`, `"alignment"` | String | Alignment of axis labels (`"right"`, `"centre"`, `"left"`). |

#### Series Settings (Key: `"series"`)

The `"series"` key takes a vector of maps, each defining a data series.

| Key | Type | Description | Default |
| :--- | :--- | :--- | :--- |
| `"name"` | String | **Required.** Series name (used in the legend). | - |
| `"data"` | Vector/List | **Required.** Data points (e.g., `[[x1 y1] [x2 y2] ...]`). | - |
| `"render-style"` | String | Render style for this series (e.g., `"line"`, `"scatter"`, `"bar"`, `"area"`, see `xchart-clj.chart.series/series-render-styles`). | Chart default |
| `"marker"` | String | Marker type for data points (e.g., `"circle"`, `"square"`, `"none"`, see `xchart-clj.chart.series/series-marker`). | `"none"` |
| `"line-color"` | String/Color | Color of the line/area fill (e.g., `"blue"` or hex code). | - |
| `"smooth?"` | Boolean | Render the line smoothly (spline). | `false` |

### 4\. Data Export

The `xchart-clj.chart.plot` namespace provides functions for saving the generated charts.

| Function | Description | Format Keywords |
| :--- | :--- | :--- |
| `(xp/save-chart-to-file! chart file-name format)` | Saves the chart to a file. | `:png`, `:jpg`, `:svg`, `:pdf`, `:eps`, etc. |
| `(xp/save-chart-to-output-stream! chart os format & {:keys [dpi]})`| Saves the chart to an Output Stream. | `:png`, `:jpg`, `:svg`, `:pdf`, `:eps`, etc. |
| `(xp/get-svg-string chart)` | Returns the chart as a SVG string. | - |
| `(xp/save-charts-to-file! charts file-name format rows cols)` | Saves multiple charts into a single file (bitmap only). | `:png`, `:jpg`, `:bmp`, `:gif` |

**Example:** Getting the chart as an SVG string

```clojure
(let [chart ...] ;; Your chart object
  (xp/get-svg-string chart))
```

## 🌐 Java Interoperability (Advanced Usage)

For Java projects that need to generate XChart diagrams using the declarative Clojure configuration, the `interop.Chartgenerator` namespace provides a static interface.

The compiled Java class `interop.Chartgenerator` offers the following static methods:

| Java Method Signature | Description |
| :--- | :--- |
| `Chart generateChart(Map chartMap)` | Creates a Chart object from a Java Map (e.g., converted from JSON or EDN). |
| `String generateSvgString(Chart chart)` | Returns the chart as an SVG string. |
| `void saveToOutputStream(Chart chart, OutputStream os, String format)` | Saves the chart to an output stream. |
| `void saveToFile(Chart chart, String filename, String format)` | Saves the chart to a file. |


**xchart-clj** is designed to be fully usable from Java applications. The core charting logic, implemented in Clojure, is automatically exposed to Java via a static helper class, eliminating the need to write complex Java configuration code.

### 1\. Generating a Chart

The compiled Clojure namespace `interop.Chartgenerator` is exposed as a static Java class, allowing you to generate an XChart object simply by passing a standard `java.util.Map` (which can be easily constructed from JSON or EDN data).

#### Method Signature (Java)

```java
import java.util.Map;
import org.knowm.xchart.internal.chartpart.Chart;
import interop.Chartgenerator; // Your compiled Clojure bridge

public class JavaDemo {
    public static Chart<?, ?> createChart(Map<String, Object> configMap) {
        // The Clojure logic translates the declarative map into a Chart object
        return Chartgenerator.generateChart(configMap); 
    }
}
```

### 2\. Exporting and Saving

The bridge class also provides direct methods to export the resulting `Chart` object.

| Java Method Signature | Description |
| :--- | :--- |
| `String generateSvgString(Chart chart)` | Returns the chart as a SVG string. |
| `void saveToOutputStream(Chart chart, OutputStream os, String format)` | Saves the chart to an output stream. |
| `void saveToFile(Chart chart, String filename, String format)` | Saves the chart to a file (e.g., `"png"`, `"svg"`). |

-----

### 3\. Advanced Customization in Java (The Escape Hatch)

One of the major advantages of **xchart-clj** being a wrapper around a Java library is the ability to use the raw **XChart Java API** for advanced customizations *after* the chart has been configured by Clojure.

This is particularly useful when:

1.  A specific customization option is **not yet implemented** in the Clojure configuration map (refer to the 🛠️ To-Do List).
2.  You need access to complex, low-level XChart features (e.g., custom tooltips, specific event handlers) that the wrapper does not expose.

The `generateChart` method returns the base XChart object (`org.knowm.xchart.internal.chartpart.Chart`). You can cast this object to its specific type (e.g., `XYChart`, `CategoryChart`) to access its full Java API.

#### Example: Modifying the Chart Styler in Java

Assume you need to set the `AxisTitlePadding` value, but the corresponding key is missing in the current `xchart-clj` implementation.

```java
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import interop.Chartgenerator;

// Assuming this map was created and passed from JSON/Configuration
Map<String, Object> basicConfig = ... ; 

// 1. Generate the base chart using the Clojure configuration
Chart<?, ?> rawChart = Chartgenerator.generateChart(basicConfig);

// 2. Cast the chart object to its specific type (e.g., XYChart)
//    and access the Styler via the Java API.
if (rawChart instanceof XYChart) {
    XYChart xyChart = (XYChart) rawChart;

    // 3. Apply manual, low-level customization via XChart Java API
    System.out.println("Applying manual Axis Title Padding via Java.");
    xyChart.getStyler().setAxisTitlePadding(25); // Setzt den Abstand
}

// 4. Chart is now fully configured and can be exported
Chartgenerator.saveToFile(rawChart, "customized_java_output.png", "png");
```

This pattern ensures that you always have an **"escape hatch"** to leverage the complete power of the underlying XChart library, even while primarily enjoying the simplicity of the Clojure map configuration.

-----

## 🛠️ To-Do List

This section outlines planned features and missing implementations across various chart types and customization options:

### 1\. General Functionality

  * **Further Customization Options:** Implement remaining styling options available in the core XChart library that are not yet exposed via the configuration map (e.g., specific font family settings, padding, border visibility).
  * **Documentation Expansion:** Provide detailed examples and documentation for all supported chart types beyond the basic XY chart (Category, Pie, Boxplot, etc.).

### 2\. Chart Type Specific Options

  * **Pie Chart Customization:** Add support for specific Pie chart styling options (e.g., setting start angle, inner/outer radius, slice separators).
  * **Category Chart Options:** Expose more options for Category charts, such as bar width customization, and grouped/stacked bar options.
  * **BoxPlot Calculation:** Ensure full support and clear documentation for all available `BoxStyler$BoxplotCalCulationMethod` options.
  * **Dial/Radar/HeatMap Charts:** Integrate and document specific styling and configuration options unique to Dial, Radar, and Heat Map charts.

### 3\. Series Customization

  * **Complete Series Styling:** Add missing series-level styling options (e.g., marker size customization, data label visibility/formatting).

-----

## License

Copyright 2025 Marcus Lindner

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
