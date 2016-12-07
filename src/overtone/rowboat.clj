(ns overtone.rowboat
  (:use [overtone.live ]
        [overtone.inst.piano])
  (:use [leipzig.melody :refer :all])
  )

(def durs [1/4,1/4, 1/4, 1/4])
(def cumdur (reductions + durs))
[durs cumdur]

(defn mybpm [b bn]
  (/ 60000 b bn))
;; Lets play some notes.
;; To play a note we use the 'piano' comamand which takes the midi number for a note, 60 is middle c and plays it on 
;; a piano.

(piano 60)

;; Let's play another note
(piano 62)

;Notice that lisp puts the parenthesis around the whole statment instead of around the argument like this:
;; piano (60)
;; There's a good reason for this which we will get to later.
;; Just rememember for now that most lisp statments look like that.
;; A parenthesis with a bunch of things inside and a closed parenthesis.
;; Lets play a sequence of notes
;; Select all the notes and hit command return to run all the lines
(piano 60)
(piano 62)
(piano 64)

;; That didnt' work. It played all the notes at the same time.
;; To play a sequence of notes we need to schedule the note to be played at a certain time
;; Overtone's  'at' function does this. 

  (at (+ (now) 500) (piano 60))
  (at (+ (now) 1000) (piano 62))
  (at (+ (now) 1500) (piano 64))

;; These commmands tell overtone to start playing the note 60 1/2 second from when you execute the command
;; the note 62 1 second from the execution time and
;; the note 64 1.5 seconds from execution time.
  

;; If we had to play 500 notes this would be a lot of typing.
;; We would like to just specify the notes and the times to play them and then pass that
;; off to a function which would schedule the time for each note to be played
;; We can specify the notes by putting them in an array like this:
;; [500,1000,1500] and for the notes [60,62,64]
;; For now lets just say an array is a collection of things which we denote by [a,b,c,....]
;; Then we want to write a command like this
;; (playNoteSequence [500,1000,1500], [60,62,64]) and have it
;; do the same thing as the above three 'at' statements or more if there are more notes and times.




(defn phrase [ dur notes]
  (let [times (reductions + (cons 0 dur))]
      (map #((if (= (type %2) clojure.lang.PersistentVector)
               (hash-map :duration %1 :pitch %2 :time %3)
           dur notes times)))))

(defn where[key f music]
  (map #(update-in %1 [key] f) music))
  
(defn bpm[n b] 
  (fn [t] (* t  (/ 60000 n b))))

(def p (phrase [1/4 1/4 1/4 1/4 ] [:c4 :d4 :e4 :f4]))


(defn play-phrase [ p]
  (let [n (now)]
  (doseq [{:keys [time duration pitch]} p]
    (at (+ n time) (piano  pitch)))))

(->> (phrase (repeat 1/16) 
             (concat (range 90 120) (range 120 90 -1)))
  (where :time (bpm 60 ))
  (where :notes identity)
  (play-phrase))


;; Now to make a program do what we did in the lines of code above we need to store the list
;; of notes and times in a vector
;; [[ 500, :c4], [1000, :d4], [1500 :e4]]
;; Then we need to write a program that loops through this vector getting a [time, note] from it
;; and then we use this variables in the 'at' function

;; So we need to be able to execute instructions once for each element in a sequence.
;; The 'doseq' command is what we need.
;; Let's print each element in a vector
(doseq [i [5,7,9]]
  (println i))
;; This is equivalent to
;; (println 5)
;; (println 7)
;; (println 9)

;; So this 'doseq' gets the first element in the vector [5,7,9] which is 5 and lets you refer to that 5
;; by the symbol i. 
;; Then it executes the command (println i) where i refers to 5.
;; Then doseq goes back and gets the next element of the vector which is 7 , lets you refer to 7 by the symbol i
;; and executes the commmand (println i) where this time i refers to 7, and so on.

 (def current-time (now)) 
 (doseq [[t,n] [[500,:c4], [1000,:d4], [1500,:e4]]]
   (at (+ current-time t) (piano (note n))))
 
 ;; The doseq executes the '(at (+...' statement once for each element in the vector.
 
 
 
 
 
  (defn time-from-durations[durations]
    (butlast (cons 0 (reductions + durations))))
  
  (time-from-durations [100,200,100])
  

(defn make-note [p,t] [t, p])

[(make-note :A4 0), (make-note :C4 1) , (make-note :D4 2)]
;
;(def notes (map make-note [:a4,:c4,:d4] , [500,500,500]))
;
;
;(doseq [[ t,p ] notes]
;  (let [n (now)]
;    (at (+ n t) (piano (note p)))))
;
;(piano (note :A4))
;(note :A4)
;(midi->hz 69)
;
;(chord :C4 :minor)



(defn play-chord [a-chord ]
  (doseq [note a-chord] (piano note)))

;; We can play many types of chords.
;; For the complete list, visit https://github.com/overtone/overtone/blob/master/src/overtone/music/pitch.clj and search for "def CHORD"
;(play-chord (chord :C4 :major) )



(defn bpm [n]
  (* 1000 (/ 60 n)))

(defn make-beats[b] 1)

(defn play-chord-progression[beats-per-min chords ]
  (doseq [[t,chord] (map vector (make-beats beats-per-min) chords)]
    (at t (play-chord chord))))

(defn make-chords[chords]
  (map #(chord % :major) chords))

;
;(play-chord-progression 300 (make-chords [:g4 :a4 :g3 :g4]))



;; We can play a chord progression on the synth
;; using times:
(defn chord-progression-time []
  (let [time (now)]
    (at time (play-chord (chord :C4 :major)))
    (at (+ 2000 time) (play-chord (chord :G3 :major)))
    (at (+ 3000 time) (play-chord (chord :F3 :sus4)))
    (at (+ 4300 time) (play-chord (chord :F3 :major)))
    (at (+ 5000 time) (play-chord (chord :G3 :major)))))

;(chord-progression-time)

;; or beats:
(defonce metro (metronome 120))
(metro)
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major)))  
)

(chord-progression-beat metro (metro))

;; We can use recursion to keep playing the chord progression
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 12 beat-num)) (play-chord (chord :F3 :major)))
  (apply-at (m (+ 16 beat-num)) chord-progression-beat m (+ 16 beat-num) [])
)
;(chord-progression-beat metro (metro))