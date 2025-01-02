(ns xchart-clj.util.helper
  (:import (java.awt Color)))

(defn get-color [color]
  (cond
    (string? color) (if (.startsWith color "#") (Color/decode color)
                        (eval (read-string (str "Color/" color))))
    (= 3 (count color)) (new Color (nth color 0) (nth color 1) (nth color 2))
    (= 4 (count color)) (new Color (nth color 0) (nth color 1) (nth color 2) (nth color 3))
    :else Color/BLACK))

 
