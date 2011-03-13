(ns gq.core
  (:use [clojure.set] :reload))

(def universe #{:alan :brad :carl :david :edward :veronica :willa :xena :yolanda :zoe
                :lucky :rocky :ginger :sasha
                :joy-of-clojure :practical-clojure :stumbling-on-happiness})
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
   "Alan" #{:alan}
   "Brad" #{:brad}
   "Carl" #{:carl}
   "David" #{:david}
   "Edward" #{:edward}
   "Ginger" #{:ginger}
   "Lucky" #{:lucky}
   "Rocky" #{:rocky}
   "Sasha" #{:sasha}
   ;; Intransitive Verb
   "barked" #{:ginger :lucky :rocky :sasha}
   "laughed" #{:brad :edward :veronica :yolanda}
   "studied" #{:alan :brad :david :zoe}
   ;; Quantifier
   "every" (fn [p q] (if (subset? p q) true false))
   "most" (fn [p q] (if (> (count (intersection p q)) (count (difference q p))) true false))
   "some" (fn [p q] (if (not (empty? (intersection p q))) true false))
})

(defn m
  "Returns the interpretation of word."
  [word]
  (lexicon word))

