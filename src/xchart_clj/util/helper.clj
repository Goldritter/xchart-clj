(ns xchart-clj.util.helper
  (:import (java.awt Color Font)))

(defn get-color [color]
  (cond
    (string? color) (if (.startsWith color "#") (Color/decode color)
                                                (eval (read-string (str "Color/" color))))
    (= 3 (count color)) (new Color (nth color 0) (nth color 1) (nth color 2))
    (= 4 (count color)) (new Color (nth color 0) (nth color 1) (nth color 2) (nth color 3))
    :else Color/BLACK))

(def style-map {"italic" Font/ITALIC
                "bold"   Font/BOLD
                "plain"  Font/PLAIN
                })

(defn get-font [font-specs]
  (new Font (get font-specs "name" )
       (get style-map (get font-specs "style" "plain"))
       (get font-specs "size")))

(defn keys-keywordize
  "Recursively transforms all map keys from strings to keywords."
  [m]
  (let [f (fn [[k v]]
            [(if (string? k)
               (keyword k)
               k)
             v])]
    (clojure.walk/prewalk (fn [x]
                            (cond (map? x) (into {} (map f x))
                                  (instance? java.util.Collection x) (into [] (map keys-keywordize x))
                                  :else x)) m)))

(defn keyword->string
  "Recursively transforms all map keys and values from keywords to strings."
  [m]
  (let [f (fn [[k v]]
            [(if (keyword? k)
               (name k)
               k)
             (if (keyword? v)
               (name v)
               v)])]
    (clojure.walk/prewalk (fn [x]
                            (cond (map? x) (into {} (map f x))
                                  (instance? java.util.Collection x) (into [] (map keyword->string x))
                                  :else x)) m)))
