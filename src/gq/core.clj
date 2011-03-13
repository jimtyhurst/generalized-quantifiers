(ns gq.core
  (:use [clojure.set] :reload))

(def universe #{:alan :brad :carl :david :edward :veronica :willa :xena :yolanda :zoe
                :lucky :rocky :ginger :sasha
                :joy-of-clojure :practical-clojure :stumbling-on-happiness})

(defn m-individual [entity]
  "Maps element of the universe of discourse to a generalized quantifier."
  (fn [q] (if (subset? #{entity} q) true false)))

(def lexicon
  {;; Adjective
   "female" #{:veronica :willa :xena :yolanda :zoe :ginger :sasha}
   "male" #{:alan :brad :carl :david :edward :lucky :rocky}
   ;; Noun
   "book" #{:joy-of-clojure :practical-clojure :stumbling-on-happiness}
   "developer" #{:carl :david :willa :xena}
   "dog" #{:ginger :lucky :rocky :sasha}
   "man" #{:alan :brad :carl :david :edward}
   "person" #{:alan :brad :carl :david :edward :veronica :willa :xena :yolanda :zoe}
   "student" #{:alan :brad :yolanda :zoe}
   "woman" #{:veronica :willa :xena :yolanda :zoe}
   ;; Proper Noun
   "Alan" (m-individual :alan)
   "Brad" (m-individual :brad)
   "Carl" (m-individual :carl)
   "David" (m-individual :david)
   "Edward" (m-individual :edward)
   "Ginger" (m-individual :ginger)
   "Lucky" (m-individual :lucky)
   "Rocky" (m-individual :rocky)
   "Sasha" (m-individual :sasha)
   "Veronica" (m-individual :veronica)
   "Willa" (m-individual :willa)
   "Xena" (m-individual :xena)
   "Yolanda" (m-individual :yolanda)
   "Zoe" (m-individual :zoe)
   ;; Intransitive Verb
   "barked" #{:ginger :lucky :rocky :sasha}
   "laughed" #{:brad :edward :veronica :yolanda}
   "studied" #{:alan :brad :david :zoe}
   ;; Quantifier
   "every" (fn [p q] (if (subset? p q) true false))
   "most" (fn [p q] (if (> (count (intersection p q)) (count (difference q p))) true false))
   "some" (fn [p q] (if (not (empty? (intersection p q))) true false))
   })

(defn lexical-item?
  "Returns true if expression is a lexical item."
  [expression]
  (and (string? expression) (not (nil? (lexicon expression)))))

(defn m-lexical-item
  "Returns the interpretation of word."
  [word]
  (lexicon word))

(defn m
  "Returns the interpretation of expression."
  [expression]
  (cond (lexical-item? expression) (m-lexical-item expression)
        :else "Unknown type"))
