(ns overtone.grammar  )
                           

(def     notestr "cdefgabCDEFGAB" )
(def      timequal "_ ^")


   
  (def notelist 
    (->> (for [n notestr ]
           (->>  (str n)
             (clojure.string/trim)
             (#(str "'" % "'"))))
      (clojure.string/join "|")))
  
  (def grammar1 
    "ABC = Token+
     Token =     TimeQualifier| Meter | Barline  | Group | Chord | 
                  Rest | Trill | Turn| Accidental | Note 
     Trill = '+trill+'
     Turn =   '+turn+'
     Chord = <'['> (Token+) <']'> 
     Group = <'('> (Token+) <')'>
     Barline = '|' | '||' | ':|' | '|:' | ':||' | '||:' | '[1' | '[2' | '|1' | '|2'
     Meter = 'L' Number '/' Number
     Time = Number | '/' | '/' Number | Number '/' Number
     Note = Pitch| PitchCommas| PitchApos|
            Pitch Time | PitchCommas Time | PitchApos Time
     Accidental = ('_' | '^') Note
     PitchApos = Pitch Apos
     PitchCommas = Pitch Commas
     Number = ('0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9')+
     Apos = \"'\"+ 
     Commas = ','+ 
     Rest = 'z'
     TimeQualifier =  ('<' | '>' | '<<' | '>>') \n"
    )
  (def grammar (str grammar1 "Pitch = " notelist))



