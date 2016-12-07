(ns overtone.core-test
  (:require 
     [leipzig.melody :refer [bpm all phrase then times where with tempo]]
      [leipzig.live :as live]
      [overtone.live :as ol]
     [overtone.music.pitch :refer :all]
     [clojure.test :refer :all]
      [instaparse.core :as insta]
      [overtone.grammar  :refer :all]
      [overtone.parseABC :refer :all ] 
            ))

(def abcparser (insta/parser grammar    
                  :auto-whitespace  
                  (insta/parser "whitespace = #'\\s+'")))

(def rh
  (apply vector 
    (rest (->>
            (slurp (clojure.java.io/resource "abcRH.txt"))
            (clojure.string/split-lines)
            ))))

(defmacro maketests [testname  & arglist]
  (let [body (for [[k,v] (apply hash-map arglist)]
               `(is (= (applytrans (abcparser ~k)) ~v)))]
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

;(run-tests)
;  



 (def melody
   (->> (abcparser "abcabcd")
   
        ((make-trans :Pitch))
        ((make-trans :PitchApos))
        ((make-trans :PitchCommas))
         ((make-trans :Number))
       ((make-trans :Time))  
        ((make-trans :Token))
      ((make-trans :Accidental))
     ((make-trans :Note))
     (rest)
    )
   )

(apply vector (map second melody))
(apply vector (map #(:midi-note (note-info (first %))) melody))




;(applytrans (abcparser "g/"))
;(applytrans (abcparser "^g"))
;(doseq  [i (range 20)]
;  (println i (rh i))
;  (println (applytrans (abcparser (rh i))) "\n"))



