(ns overtone.leipzigtest
   (:require [overtone.live :refer :all]
              [leipzig.melody :refer :all]
              [leipzig.chord :refer :all]
              [instaparse.core :as insta]
              [overtone.grammar  :as grammar]
              [overtone.parseABC :as parser ] 
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

(defn myplay[speed parser-notes]
  (let [durations #(map (% 1)) parser-notes
        pitches #(map (% 0)) parser-notes]
	(->>
	  (phrase durations pitches)
	  (all :part :leader)
	  (where :time (bpm speed))
	  (live/play))))

(myplay  80 (repeat 1/4)
	       [{1  60 2 80 } {1  60 2 50 } 59 79 89 {1  60 2 70 }])


(def abcparser (parser/get-parser (grammar/get-grammar)))

(def rh
  (apply vector 
    (rest (->>
            (slurp (clojure.java.io/resource "abcRH.txt"))
            (clojure.string/split-lines)
            ))))


(parser/applytrans (abcparser (rh 1)))
        