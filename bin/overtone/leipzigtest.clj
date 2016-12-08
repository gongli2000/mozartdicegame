(ns overtone.leipzigtest
   (:require [overtone.live :refer :all]
              [leipzig.melody :refer :all]
              [leipzig.chord :refer :all]
              [leipzig.live :as live]
              [overtone.inst.piano]))

(definst beep [freq 440]
  (-> freq
      saw
      (* (env-gen (perc) :action FREE))))

(definst ping [freq 440]
  (-> freq
      square
      (* (env-gen (perc) :action FREE))))

(definst seeth [freq 440 dur 1.0]
  (-> freq
      saw
      (* (env-gen (perc (* dur 1/2) (* dur 1/2)) :action FREE))))

(defmethod live/play-note :leader [{midi :pitch}]
  (-> midi midi->hz beep))

(defmethod live/play-note :follower [{midi :pitch}]
  (-> midi midi->hz ping))

(defmethod live/play-note :bass [{midi :pitch seconds :duration}]
  (-> midi midi->hz (/ 2) (seeth seconds)))

;
(->>
  (phrase (repeat 1/16)
          [{1  60 2 70 } 89 {1  60 2 70 } {1  60 2 70 }])
  (all :part :leader)
  (live/play))

        
        