(ns overtone.core  
  (:use [leipzig melody scale live chord]
        [overtone.inst.piano]
   )
  (:require [overtone.live :as ot]))

   
(defmethod play-note :default [{midi :pitch}] (piano midi))

(defn make-phrase [notes-vec]
  (apply vector
         (map #(hash-map :pitch (% 0) :duration (% 1)) notes-vec)))

(map #(phrase [(% 1)] [(% 0)]  ) [[60 1] [70 1] [70 1] [70 1] [70 1] [70 1] [70 1] ])

(phrase [60 10] [1 1])


;(defn play-melody 
;  [pitches durations]
;  (->> (phrase durations pitches)
;    (where :part (is :melody))
;    (where :time (bpm 260))
;    (where :pitch (comp C major))
;    play))
;
; C major scale
;(play-melody [0 1 2 3 4 5 6 7] 
;            [1 1 1 1 1 1 1 1])


(defn play-melody [aphrase tempo key]
  (->> aphrase
    (where :part (is :melody))
    (where :time (bpm 100))
    (where :pitch key)
    play))

(defn play-melody2 [aphrase t ]
  (->> aphrase
    (where :part (is :melody))
    (where :time (bpm t))
    play))

(map #(-> triad (root %)) [0 3 4 3])






(def p (phrase
         [1 1 1]
         [(-> triad (root 3)) (-> triad (root 5))
          (-> triad (inversion 2) (root 4))]))

(let [dur (repeat 30 1/4)]
  (def p2
       (->> (range 1 (count dur))
            (map #(+ % 65))
            (phrase dur ))))

(def nn  (range 66 94))
(def dur (repeat (count nn) 1/4))
(def p3 (phrase dur nn))
;

(defmacro makekey[ & args ]
  `(def ~(symbol (apply str args)) (comp ~@args)))
(macroexpand-1 '(makekey B flat major))
(play-melody p 123 (comp B flat major))
;(play-melody2 p2 60 )
