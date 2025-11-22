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

## 🌐 Interoperability (Java/Clojure)

For Java projects that need to generate XChart diagrams using the declarative Clojure configuration, the `interop.Chartgenerator` namespace provides a static interface.

The compiled Java class `interop.Chartgenerator` offers the following static methods:

| Java Method Signature | Description |
| :--- | :--- |
| `Chart generateChart(Map chartMap)` | Creates a Chart object from a Java Map (e.g., converted from JSON or EDN). |
| `String generateSvgString(Chart chart)` | Returns the chart as an SVG string. |
| `void saveToOutputStream(Chart chart, OutputStream os, String format)` | Saves the chart to an output stream. |
| `void saveToFile(Chart chart, String filename, String format)` | Saves the chart to a file. |


## License

Copyright 2025 Marcus Lindner

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
