(ns overtone.miditransform
    (:use [clojure.string :only (join split lower-case upper-case)]
          [overtone.music.pitch]))

(def      notes "C ^C D ^D E F ^F G ^G A ^A B")

(def     oddnotes ["_D" 61, "_E" 63 ,"_G" 66 ,"_A" 68 ,"_B" 70 , "_d" 73, "_e" 75, "_g" 78 ,"_a" 80 ,"_b" 82])
(def      na  (split notes #" "))
(def     nb (map lower-case na))
(def     n (concat na nb))
(def     midi-values (range 60 (+ 60 (count n))))



(def make-midi-map 
  (->>
    (map vector n midi-values)
    flatten
    (#(concat % oddnotes ))
    (apply hash-map)))

(def midi-map
  {"^G" 68, "d" 74, "^C" 61, "f" 77, "e" 76, "_e" 75, 
   "G" 67, "^D" 63, "^d" 75, "_E" 63, "^A" 70, "_d" 73, 
   "^g" 80, "E" 64, "^F" 66, "C" 60, "F" 65, "^f" 78, "_G" 66, 
   "B" 71, "_A" 68, "a" 81, "^c" 73, "_D" 61, "b" 83, "^a" 82, 
   "_B" 70, "g" 79, "A" 69, "_a" 80, "_b" 82, "_g" 78, "D" 62, "c" 72})

(def which-octave
  {"^G" 4, "d" 5, "^C" 4, "f" 5, "e" 5, "_e" 5, 
   "G" 4, "^D" 4, "^d" 5, "_E" 4, "^A" 4, "_d" 5, 
   "^g" 5, "E" 4, "^F" 4, "C" 4, "F" 4, "^f" 5, "_G" 5, 
   "B" 4, "_A" 4, "a" 5, "^c" 5, "_D" 4, "b" 5, "^a" 5, 
   "_B" 4, "g" 5, "A" 4, "_a" 5, "_b" 5, "_g" 5, "D" 4, "c" 5})




