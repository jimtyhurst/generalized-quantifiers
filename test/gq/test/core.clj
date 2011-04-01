(ns gq.test.core
  (:use [gq.core] :reload)
  (:use [clojure.test]))

(deftest test-every
  (is (((m "every") (m "dog")) (m "bark"))
      "Every dog barked.")
  (is (false? (((m "every") (m "student")) (m "study")))
      "Every student studied.")
  (is (((m "a") (m "student")) (((m "every") (m "book")) (m "read")))
      "A student read every book. (subject-wide scope)"))

(deftest test-some
  (is (((m "some") (m "dog")) (m "bark"))
      "Some dog barked.")
  (is (((m "some") (m "student")) (m "study"))
      "Some student studied.")
  (is (false? (((m "some") (m "student")) (m "bark")))
      "Some student barked.")
  (is (false? (((m "some") (m "dog")) (m "study")))
      "Some dog studied.")
  (is (false? (((m "some") (m "developer")) (m "laugh")))
      "Some developer laughed."))

(deftest test-most
  (is (((m "most") (m "dog")) (m "bark"))
      "Most dogs barked.")
  (is (((m "most") (m "student")) (m "study"))
      "Most students studied.")
  (is (false? (((m "most") (m "student")) (m "laugh")))
      "Most students laughed.")
  (is (false? (((m "most") (m "developer")) (m "bark")))
      "Most developers barked."))

(deftest test-less-than
  (is ((((m "less than") (m 5)) (m "dog")) (m "bark"))
      "Less than 5 dogs barked.")
  (is ((((m "less than") (m 5)) (m "student")) (m "study"))
      "Less than 5 students studied.")
  (is (false? ((((m "less than") (m 5)) (m "person")) (m "study")))
      "Less than 5 people studied.")
  (is (false? ((((m "less than") (m 3)) (m "dog")) (m "bark")))
      "Less than 3 dogs barked."))

(deftest test-more-than
  (is ((m "Alan") ((((m "more than") (m 1)) (m "woman")) (m "kiss")))
      "Alan kissed more than 1 woman.")
  (is ((((m "more than") (m 1)) (m "man")) ((m "Veronica") (m "kiss")))
      "More than 1 man kissed Veronica.")
  (is ((((m "at least") (m 2)) (m "man")) ((m "Veronica") (m "kiss")))
      "At least 2 men kissed Veronica."))

(deftest test-exactly
  (is ((m "Alan") ((((m "exactly") (m 2)) (m "woman")) (m "kiss")))
      "Alan kissed exactly 2 women.")
  (is ((((m "exactly") (m 2)) (m "man")) ((m "Veronica") (m "kiss")))
      "Exactly 2 men kissed Veronica.")
  (is (((m "some") ((m "male") (m "student"))) ((((m "exactly") (m 3)) (m "book")) (m "read")))
      "Some male student read exactly 3 books."))

;; FIXME: 'which' only works in subject position currently,
;; due to the way it is defined as a GQ. The definition needs
;; to be generalized for movement from the object position.
(deftest test-which
  (is (= (((m "which") (m "student")) (m "laugh")) #{:brad :yolanda}))
  (is (= (((m "which") (m "man")) ((m "Veronica") (m "kiss"))) #{:alan :brad})))

(deftest test-proper-noun
  (is ((m "Ginger") (m "bark"))
      "Ginger barked.")
  (is ((m "Zoe") (m "study"))
      "Zoe studied.")
  (is (false? ((m "Zoe") (m "bark")))
      "Zoe barked.")
  (is (false? ((m "Ginger") (m "study")))
      "Ginger studied."))

(deftest test-number
  (is (== (m 42) 42)))

(deftest test-unknown-type
  (is (= (m {:unknown-phrase-type nil}) "Unknown type")))

;; Denotation of object NP applied to transitive verb
;; yields a property, which is a subset of universe of discourse.
(deftest test-object-np
  (is (= (((m "some") (m "student")) (m "bite")) #{:ginger})
      "bit some student")
  (is (= ((m "Alan") (m "bite")) #{:ginger})
      "bit Alan"))

(deftest test-transitive-verb
  (is ((m "Ginger") ((m "Alan") (m "bite")))
      "Ginger bit Alan.")
  (is (((m "some") (m "dog")) ((m "Alan") (m "bite")))
      "Some dog bit Alan.")
  (is ((m "Ginger") (((m "some") (m "student")) (m "bite")))
      "Ginger bit some student.")
  (is ((m "Ginger") (((m "every") ((m "male") (m "student"))) (m "bite")))
      "Ginger bit every male student.")
  (is (((m "some") (m "dog")) (((m "some") (m "student")) (m "bite")))
      "Some dog bit some student. (subject wide)")
  (is (((m "every") (m "student")) (((m "some") (m "book")) (m "read")))
      "Every student read some book. (subject wide)")
  (is ((((m "at least") (m 1)) (m "student")) (((m "most") (m "book")) (m "read")))
      "At least 1 student read most books. (subject wide)")
  (is (((m "no") (m "dog")) (((m "any") (m "woman")) (m "bite")))
      "No dog bit any women.")
  (is (((m "no") (m "dog")) (((m "a") (m "book")) (m "read")))
      "No dog read a book.")
  (is (false? (((m "every") (m "student")) ((((m "at least") (m 3)) (m "book")) (m "read"))))
      "Every student read at least 3 books.")
  (is (false? (((m "every") (m "dog")) (((m "some") (m "student")) (m "bite"))))
      "Every dog bit some student. (subject wide)")
  (is (false? ((m "Ginger") (((m "every") (m "student")) (m "bite"))))
      "Ginger bit every student.")
  (is (false? (((m "some") (m "dog")) ((m "Zoe") (m "bite"))))
      "Some dog bit Zoe.")
  (is (false? ((m "Ginger") ((m "Zoe") (m "bite"))))
      "Ginger bit Zoe."))

(deftest test-adjective
  (is ((m "Ginger") (((m "a") ((m "male") (m "student"))) (m "bite")))
      "Ginger bit a male student.")
  (is (((m "no") ((m "female") (m "dog"))) (((m "any") ((m "female") (m "student"))) (m "bite")))
      "No female dog bit any female students.")
  (is (((m "every") ((m "male") (m "student"))) ((m "Veronica") (m "kiss")))
      "Every male student kissed Veronica.")
  (is (false? (((m "every") ((m "male") (m "student"))) (m "laugh")))
      "Every male student laughed.")
  (is (false? ((m "Ginger") (((m "a") ((m "female") (m "student"))) (m "bite"))))
      "Ginger bit a female student."))
