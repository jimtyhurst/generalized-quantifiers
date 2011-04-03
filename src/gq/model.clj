(ns gq.model
  (:use [clojure.set] :reload))

;; Model includes the:
;;   * universe of discourse.
;;   * mapping of lexical items to their interpretation
;;     with respect to that universe of discourse.

;; The universe of discourse, is organized alphabetically by distinguishing properties.
;; This is purely for convenience of maintaining the set of entities
;; as the model changes over time. The labels and ordering have
;; absolutely no significance to the model.
(def universe
  #{;; people
    :alan :brad :carl :david :edward :veronica :willa :xena :yolanda :zoe
    ;; dogs
    :lucky :rocky :ginger :sasha
    ;; books
    :joy-of-clojure :practical-clojure :stumbling-on-happiness})

(defn m-gq
  "Returns a function as the interpretation of a generalized quantifier."
  [quantifier-predicate]
  (fn [quantified-property]
    (fn [argument]
      (cond (set? argument) ;; arg is Verb Phrase denotation
            ;; Apply quantifier to two properties.
            (quantifier-predicate quantified-property argument)
            (map? argument) ;; arg is Transitive Verb denotation
            ;; Reduce verb denotation to just those first arguments
            ;; in relation with second arguments that satisfy the quantifier.
            (reduce
             (fn [reduced-set map-element]
               (if (quantifier-predicate quantified-property (second map-element))
                 ;; subset of relation satisfies the predicate
                 (union (hash-set (first map-element)) reduced-set)
                 ;; else map-element is not one of the satisfiers
                 reduced-set))
             (set '()) ;; reduced-set accumulator
             argument)))))

(defn m-individual
  "Maps element of the universe of discourse to a generalized quantifier."
  [entity]
  (fn [argument]
    (let [quantifier-predicate (fn [q] (subset? #{entity} q))]
      (cond (set? argument) ;; arg is Verb Phrase denotation
            (quantifier-predicate argument)
            (map? argument) ;; arg is Transitive Verb denotation
            (reduce
             (fn [reduced-set map-element]
               (if (quantifier-predicate (second map-element))
                 (union (hash-set (first map-element)) reduced-set)
                 reduced-set)
               )
             (set '()) ;; reduced-set accumulator
             argument)))))

(defn m-adjective
  "Maps a subset of the universe of discourse to a function that maps a subset to a subset, because functionally an adjective maps a noun to a noun."
  [p]
  (fn [q] (intersection p q)))

;; Lexical item is the key. Denotation is the value.
;; Words are listed alphabetically by syntactic category
;; and entities are listed alphabetically in sets
;; for ease of maintenance.
(def lexicon
  {;; Adjective
   "female" (m-adjective #{:veronica :willa :xena :yolanda :zoe :ginger :sasha})
   "male" (m-adjective #{:alan :brad :carl :david :edward :lucky :rocky})
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
   "bark" #{:ginger :lucky :rocky :sasha}
   "laugh" #{:brad :edward :veronica :yolanda}
   "study" #{:alan :brad :david :xena :zoe}
   ;; Transitive Verb
   "bite" {:ginger #{:alan :brad}}
   "kiss" {:alan #{:veronica :zoe}, :brad #{:veronica}, :veronica #{:alan :brad}, :zoe #{:rocky}}
   "read" {:alan #{:joy-of-clojure :practical-clojure :stumbling-on-happiness}, :brad #{:joy-of-clojure}, :veronica #{:stumbling-on-happiness}, :yolanda #{:practical-clojure}, :zoe #{:stumbling-on-happiness}}
   ;; Quantifier (simple)
   "a" (m-gq (fn [p q] (not (empty? (intersection p q))))) ;; "some"
   "all" (m-gq (fn [p q] (subset? p q))) ;; "every"
   "every" (m-gq (fn [p q] (subset? p q)))
   "most" (m-gq (fn [p q] (> (count (intersection p q)) (count (difference p q)))))
   "no" (m-gq (fn [p q] (empty? (intersection p q))))
   "some" (m-gq (fn [p q] (not (empty? (intersection p q)))))
   ;; Quantifier (negative polarity)
   "any" (m-gq (fn [p q] (not (empty? (intersection p q))))) ;; "some"
   ;; Quantifier (numerical)
   "at least" (fn [n] (m-gq (fn [p q] (>= (count (intersection p q)) n))))
   "at most" (fn [n] (m-gq (fn [p q] (<= (count (intersection p q)) n))))
   "exactly" (fn [n] (m-gq (fn [p q] (== (count (intersection p q)) n))))
   "less than" (fn [n] (m-gq (fn [p q] (< (count (intersection p q)) n))))
   "more than" (fn [n] (m-gq (fn [p q] (> (count (intersection p q)) n))))
   ;; WH questions
   ;; FIXME: 'which' only works in subject position currently.
   "which" (m-gq (fn [p q] (intersection p q)))
   ;; FIXME: 'who' only works in subject position currently.
   "who" (fn [q] q)
   })

(defn lexical-item?
  "Returns true if expression is a lexical item."
  [expression]
  (not (nil? (lexicon expression))))

;; m-lexical-item is in model namespace, because the implementation
;; depends on the implementation of the lexicon.
(defn m-lexical-item
  "Returns the interpretation of word."
  [word]
  (lexicon word))

