(ns make-faces.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:color 0
   :angle 0})

(defn update-state [state]
  state)

(defn draw-eyes [_]
  (let [size 60
        spacing 40
        start Math/PI
        stop (* 2 Math/PI)
        tilt -0.3]
    (q/with-rotation [tilt]
      (q/arc (* -1 spacing) 0 size size start stop :open))
    (q/with-rotation [(- tilt)]
      (q/arc spacing 0 size size start stop :open))))

(defn draw-mouth [_]
  (let [size 120
        start (* 0.1 Math/PI)
        stop (* 0.9 Math/PI)]
    (q/arc 0 0 size size start stop :open)))

(defn draw-face [state]
  (let [axis1 200
        axis2 axis1]
    (q/ellipse 0 0 axis1 axis2)
    (q/with-translation [0 -30]
      (draw-eyes state))
    (q/with-translation [0 20]
      (draw-mouth state))
    ))

(defn draw-state [state]
  (q/background 240)
  (q/fill 0 0 255)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (draw-face state)))

(q/defsketch make-faces
  :title "Clojure makes happy faces"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
