(ns overtone.parseABC
 (:use    [overtone.music.pitch])
 (:require [instaparse.core :as insta]
        
           [overtone.miditransform :as m]))

(:midi-note (note-info "c4"))


;(def whichoctave 
;  (let [noteslower "cdefgab"
;        notesupper "CDEFGAB"
;        octavelower (repeat (count noteslower) 5)
;        octaveupper (repeat (count notesupper) 4)
;        v  (map vector (map str (concat noteslower notesupper)
;                (concat octavelower octaveupper))]
;    (apply hash-map (flatten v))))
    
(def default-duration 1/8)
(def transform-funcs 
  {:PitchCommas (fn [pc commas]
                  (let [ numcommas (count (rest commas))]
                       [:Pitch (pc 1) (- (pc 2) numcommas)]))
   
   :PitchApos (fn [pc apos]
                  (let [numapos    (count (rest apos))]
                       [:Pitch (pc 1) (+ (pc 2) numapos)]))
   

   :Pitch (fn [p] [:Pitch  p  (m/which-octave p) ])
   :Number (fn [num]   (read-string num)),
   :Time (fn [v & args] 
           (case (count args)
            0 (if (= v "/")
                (/ default-duration 2) 
                (* default-duration v) )
            1 (/ default-duration (first args)))),
   :Note (fn [ & args  ]
           (let [p (first args)]
           (case (count args)
               1 [ (str (p 1) (p 2)) default-duration]
               2 [ (str (p 1) (p 2))(second args)])
               ))
   :Commas (fn [& args] (count (apply str args))),
   :Apos (fn [& args] (count (apply str args))),
   :Token (fn [ & rest] (first rest))
   :Accidental (fn [acc note  ]
                 (let [p (note 1)
                       pc (p 1)
                       octave (p 2)]    
                 (if (= "^" acc)
                   (if (= 2 (count note))
                     [:Note [:Pitch (str pc "#") octave]]
                     [:Note [:Pitch (str pc "#") octave] (note 2)])
                   (if (= 2 (count note))
                     [:Note [:Pitch (str pc "b") octave ]]
                     [:Note [:Pitch (str pc "b") octave]  (note 2)]))))
   })
   
(defn make-trans[key]
  (fn [tree]
      (insta/transform 
        {key (transform-funcs key) }
        tree)))

          
(defn applytrans[ntree]
  (->> ntree
   
       ((make-trans :Pitch))
       ((make-trans :PitchApos))
       ((make-trans :PitchCommas))
        ((make-trans :Number))
       ((make-trans :Time))
       ((make-trans :Token))
    ((make-trans :Accidental))
    ((make-trans :Note))
        
   

   )
 )
  


  

