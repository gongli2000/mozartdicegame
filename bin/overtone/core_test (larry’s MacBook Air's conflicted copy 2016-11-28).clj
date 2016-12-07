(ns overtone.core-test
  (:require [clojure.test :refer :all]
            [instaparse.core :as insta]
             [overtone.grammar  :refer :all]
             [overtone.parseRLParts :refer :all ]  ))




(def abcparser 
  (insta/parser grammar    
     :auto-whitespace  (insta/parser "whitespace = #'\\s+'")))


(def m 
 (rest (->>
         (slurp (clojure.java.io/resource "abcRH.txt"))
         (clojure.string/split-lines))))

(do
(deftest addition
  (is (= (applytrans (abcparser "G")) [:ABC [:Note "G"]]))
  (is (= (applytrans (abcparser "G3")) [:ABC [:Note "G" 1/3]]))
  (is (= (applytrans (abcparser "G1/3")) [:ABC [:Note "G" 1/3]]))
  (is (= (applytrans (abcparser "G/")) [:ABC [:Note "G" 1/2]]))
  (is (= (applytrans (abcparser "G,,,")) [:ABC [:Note "G-3"]]))
  (is (= (applytrans (abcparser "G'''")) [:ABC [:Note "G3"]]))
)
(run-tests))
           
;(doseq [x (take 1 m)]
; (println (applytrans (abcparser x))))

;(do
;(def parse (applytrans (abcparser (m 1))))
;(defn parseNote[note] note)
;(defn parseChord[chord] chord)
;(defn parsetoken[[token & rest]]
;  (case token
;    :Note (println "note")
;    :Chord (println "chord")))
;     
;parse
;)

