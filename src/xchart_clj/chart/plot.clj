(ns xchart-clj.chart.plot
  (:import
   (java.io OutputStream)
   (java.nio.charset Charset)
   (org.apache.commons.io.output ByteArrayOutputStream)
   (org.knowm.xchart BitmapEncoder BitmapEncoder$BitmapFormat BitmapEncoder$BitmapFormat VectorGraphicsEncoder VectorGraphicsEncoder$VectorGraphicsFormat)))


(def formats {:vector {:svg VectorGraphicsEncoder$VectorGraphicsFormat/SVG
                       :pdf VectorGraphicsEncoder$VectorGraphicsFormat/PDF
                       :eps VectorGraphicsEncoder$VectorGraphicsFormat/EPS}
              :bitmap {:png BitmapEncoder$BitmapFormat/PNG
                       :gif BitmapEncoder$BitmapFormat/GIF
                       :bmp BitmapEncoder$BitmapFormat/BMP
                       :jpg BitmapEncoder$BitmapFormat/JPG}})

(defn new-illegal-format-exception [actual-format expected-formats]
  (new IllegalArgumentException
       (str actual-format " is an unknown format! Use one of the following: "
            (clojure.string/join ", " (sort expected-formats)))))

(defn save-chart-to-file! [chart ^String file-name format]
  (cond
    (contains? (:vector formats) format) (VectorGraphicsEncoder/saveVectorGraphic chart file-name (get-in formats [:vector format]))
    (contains? (:bitmap formats) format) (BitmapEncoder/saveBitmap chart file-name (get-in formats [:bitmap format]))

    :else (throw (new-illegal-format-exception format (concat (keys (:vector formats)) (keys (:bitmap formats)))))))

(defn save-chart-to-output-stream! [chart ^OutputStream output-stream format & {:keys [dpi]}]
  (cond
    (contains? (:vector formats) format) (VectorGraphicsEncoder/saveVectorGraphic chart output-stream (get-in formats [:vector format]))
    (contains? (:bitmap formats) format) (if (nil? dpi) (BitmapEncoder/saveBitmap chart output-stream (get-in formats [:bitmap format]))
                                             (BitmapEncoder/saveBitmapWithDPI chart output-stream (get-in formats [:bitmap format]) dpi))

    :else (throw (new-illegal-format-exception format (concat (keys (:vector formats)) (keys (:bitmap formats)))))))

(defn save-charts-to-file! [charts ^String file-name format ^Integer rows ^Integer cols]
  (if
   (contains? (:bitmap formats) format)
    (BitmapEncoder/saveBitmap (if (coll? charts) charts (list charts))
                              file-name (get-in formats [:bitmap format]) rows cols)

    (throw (new-illegal-format-exception format (keys (:bitmap formats))))))

(defn save-charts-to-output-stream! [charts ^OutputStream output-stream format ^Integer rows ^Integer cols]
  (if
    (contains? (:bitmap formats) format) 
    (BitmapEncoder/saveBitmap (if (coll? charts) charts (list charts)) 
                              output-stream (get-in formats [:bitmap format]) rows cols)

    (throw (new-illegal-format-exception format (keys (:bitmap formats))))))

(defn get-svg-string ^String [chart]
  (let [output-stream (new ByteArrayOutputStream)]
    (VectorGraphicsEncoder/saveVectorGraphic chart, output-stream, VectorGraphicsEncoder$VectorGraphicsFormat/SVG)
    (.toString output-stream (Charset/forName "utf-8"))))