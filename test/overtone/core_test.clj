(ns overtone.core-test
  (:require 
     [leipzig.melody :refer [bpm all phrase then times where with tempo]]
      [leipzig.live :as live]
      [overtone.live :as ol]
     [overtone.music.pitch :refer :all]
     [clojure.test :refer :all]
      [instaparse.core :as insta]
      [overtone.grammar  :as grammar]
      [overtone.parseABC :as parser ] 
            ))

(def abcparser (parser/get-parser (grammar/get-grammar)))
;  
;  (insta/parser grammar    
;     :auto-whitespace  
;     (insta/parser "whitespace = #'\\s+'")))

(def rh
  (apply vector 
    (rest (->>
            (slurp (clojure.java.io/resource "abcRH.txt"))
            (clojure.string/split-lines)
            ))))

(defmacro maketests [testname  & arglist]
  (let [body (for [[k,v] (apply hash-map arglist)]
               `(is (= (parser/applytrans (abcparser ~k)) ~v)))]
  `(deftest ~testname
      ~@body)))

(maketests simpletests
"G" [:ABC [:Note "G"]],
"G3" [:ABC [:Note "G" 1/3]],
"G1/3" [:ABC [:Note "G" 1/3]],
"G/"  [:ABC [:Note "G" 1/2]] ,
"G,,,"  [:ABC [:Note "G-3"]],
"G'''"  [:ABC [:Note "G3"]]
)

(def x [[1,2] [3,4]])

(apply vector (map #(% 0) x))
(apply vector (map #(% 1) x))




(def notes (rest (parser/applytrans (abcparser "abcd"))))
(def pitches (map #(% 0) notes))
(def durations (map #(% 1) notes))

(doseq [x (phrase durations pitches)]
  (println x))


(doseq  [i (range 20)]
  (println i (rh i))
  (println (parser/applytrans (abcparser (rh i))) "\n"))



