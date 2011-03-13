(ns gq.test.core
  (:use [gq.core] :reload)
  (:use [clojure.test]))

(deftest test-every
  ;; Every dog barked.
  (is (((m "every") (m "dog")) (m "bark")))
  ;; Every student studied.
  (is (false? (((m "every") (m "student")) (m "study")))))

(deftest test-some
  ;; Some dog barked.
  (is (((m "some") (m "dog")) (m "bark")))
  ;; Some student studied.
  (is (((m "some") (m "student")) (m "study")))
  ;; Some student barked.
  (is (false? (((m "some") (m "student")) (m "bark"))))
  ;; Some dog studied.
  (is (false? (((m "some") (m "dog")) (m "study"))))
  ;; Some developer laughed.
  (is (false? (((m "some") (m "developer")) (m "laugh")))))

(deftest test-most
  ;; Most dogs barked.
  (is (((m "most") (m "dog")) (m "bark")))
  ;; Most students studied.
  (is (((m "most") (m "student")) (m "study")))
  ;; Most students laughed.
  (is (false? (((m "most") (m "student")) (m "laugh"))))
  ;; Most developers barked.
  (is (false? (((m "most") (m "developer")) (m "bark"))))
  )

(deftest test-proper-noun
  ;; Ginger barked.
  (is ((m "Ginger") (m "bark")))
  ;; Zoe studied.
  (is ((m "Zoe") (m "study")))
  ;; Zoe barked.
  (is (false? ((m "Zoe") (m "bark"))))
  ;; Ginger studied.
  (is (false? ((m "Ginger") (m "study")))))

(deftest test-unknown-type
  (is (= (m {:unknown-phrase-type nil}) "Unknown type")))

(deftest test-object-np
  ;; bit some student
  (is (= (((m "some") (m "student")) (m "bite")) #{:ginger}))
  ;; bit Alan
  (is (= ((m "Alan") (m "bite")) #{:ginger})))

(deftest test-transitive-verb
  ;; Ginger bit Alan.
  (is ((m "Ginger") ((m "Alan") (m "bite"))))
  ;; Some dog bit Alan.
  (is (((m "some") (m "dog")) ((m "Alan") (m "bite"))))
  ;; Ginger bit some student.
  (is ((m "Ginger") (((m "some") (m "student")) (m "bite"))))
  ;; Some dog bit some student. (subject wide)
  (is (((m "some") (m "dog")) (((m "some") (m "student")) (m "bite"))))
  ;; Every dog bit some student. (subject wide)
  (is (false? (((m "every") (m "dog")) (((m "some") (m "student")) (m "bite")))))
  ;; Ginger bit every student.
  (is (false? ((m "Ginger") (((m "every") (m "student")) (m "bite")))))
  ;; Some dog bit Zoe.
  (is (false? (((m "some") (m "dog")) ((m "Zoe") (m "bite")))))
  ;; Ginger bit Zoe.
  (is (false? ((m "Ginger") ((m "Zoe") (m "bite"))))))
