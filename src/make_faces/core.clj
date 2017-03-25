(ns make-faces.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  ;; happiness should be in [-100, 100]
  {:happiness 100})

(defn update-state [state]
  (assoc state :happiness 80))

(defn draw-eye [size openness]
  (let [corner 10]
    (q/bezier (- size) 0
              (- corner size) openness
              (- size corner) openness
              size     0)))

(defn draw-eyes [openness]
  (let [size 30
        spacing 40
        tilt -0.3
        openness (/ openness -2.5)]
    (q/with-rotation [tilt]
      (q/with-translation [(- spacing) 0]
        (draw-eye size openness)))
    (q/with-rotation [(- tilt)]
      (q/with-translation [spacing 0]
        (draw-eye size openness)))))

(defn draw-mouth [smile]
  (let [size 60
        corner 30
        smile (/ smile 2)]
    (q/bezier (- size) 0
              (- corner size) smile
              (- size corner) smile
              size 0)))

(defn draw-ear []
  (let [width 40
        height 120
        tip-pos (/ width 0.5)
        y-low 20
        y-high (- 70)]
    (q/bezier (- width) y-low
              (- tip-pos) (- height)
              (- tip-pos) (- height)
              width y-high)))

(defn draw-ears []
  (let [spacing 80]
    (q/with-translation [(- spacing) 0]
      (draw-ear))
    (q/with-translation [spacing 0]
      (q/scale -1 1)
      (draw-ear))))

(defn draw-whiskers []
  (let [spacing 40
        whiskers (fn []
                   (q/line (- 0 spacing 115) 0 (- 0 spacing 5) -10)
                   (q/line (- 0 spacing 110) 20 (- 0 spacing) 10)
                   (q/line (- 0 spacing 115) 45 (- 0 spacing 5)  30))]
    (q/with-translation [(- spacing) 0]
      (whiskers))
    (q/with-translation [spacing 0]
      (q/scale -1 1)
      (whiskers))))

(defn draw-face [{:keys [happiness] :as state}]
  (let [axis1 240
        axis2 axis1
        eyes-pos [0 -40]
        mouth-pos [0 30]
        ears-pos [0 -40]
        whiskers-pos [0 30]
        ]
    (q/with-translation ears-pos
      (draw-ears))
    (q/ellipse 0 0 axis1 axis2)
    (q/with-translation eyes-pos
      (draw-eyes happiness))
    (q/with-translation mouth-pos
      (draw-mouth happiness))
    (q/with-translation whiskers-pos
      (draw-whiskers)
      )
    ))

(defn draw-state [state]
  (q/background 240)
  (q/fill 0 0 255)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (draw-face state)))

(q/defsketch make-faces
  :title "Clojure makes happy faces"
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode m/pause-on-error])
