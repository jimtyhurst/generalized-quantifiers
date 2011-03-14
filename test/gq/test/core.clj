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
  (is (false? (((m "most") (m "developer")) (m "bark")))))

(deftest test-less-than
  ;; Less than 5 dogs barked.
  (is ((((m "less than") (m 5)) (m "dog")) (m "bark")))
  ;; Less than 5 students studied.
  (is ((((m "less than") (m 5)) (m "student")) (m "study")))
  ;; Less than 5 people studied.
  (is (false? ((((m "less than") (m 5)) (m "person")) (m "study"))))
  ;; Less than 3 dogs barked.
  (is (false? ((((m "less than") (m 3)) (m "dog")) (m "bark")))))

(deftest test-more-than
  ;; Alan kissed more than 1 woman.
  (is ((m "Alan") ((((m "more than") (m 1)) (m "woman")) (m "kiss"))))
  ;; More than 1 man kissed Veronica.
  (is ((((m "more than") (m 1)) (m "man")) ((m "Veronica") (m "kiss"))))
  ;; At least 2 men kissed Veronica.
  (is ((((m "at least") (m 2)) (m "man")) ((m "Veronica") (m "kiss")))))

(deftest test-proper-noun
  ;; Ginger barked.
  (is ((m "Ginger") (m "bark")))
  ;; Zoe studied.
  (is ((m "Zoe") (m "study")))
  ;; Zoe barked.
  (is (false? ((m "Zoe") (m "bark"))))
  ;; Ginger studied.
  (is (false? ((m "Ginger") (m "study")))))

(deftest test-number
  (is (== (m 42) 42)))

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
  ;; Every student read some book. (subject wide)
  (is (((m "every") (m "student")) (((m "some") (m "book")) (m "read"))))
  ;; No dog bit any women.
  (is (((m "no") (m "dog")) (((m "any") (m "woman")) (m "bite"))))
  ;; No dog read a book.
  (is (((m "no") (m "dog")) (((m "a") (m "read")) (m "read"))))
  ;; Every dog bit some student. (subject wide)
  (is (false? (((m "every") (m "dog")) (((m "some") (m "student")) (m "bite")))))
  ;; Ginger bit every student.
  (is (false? ((m "Ginger") (((m "every") (m "student")) (m "bite")))))
  ;; Some dog bit Zoe.
  (is (false? (((m "some") (m "dog")) ((m "Zoe") (m "bite")))))
  ;; Ginger bit Zoe.
  (is (false? ((m "Ginger") ((m "Zoe") (m "bite"))))))

(deftest test-adjective
  ;; Ginger bit a male student.
  (is ((m "Ginger") (((m "a") ((m "male") (m "student"))) (m "bite"))))
  ;; No female dog bit any female students.
  (is (((m "no") ((m "female") (m "dog"))) (((m "any") ((m "female") (m "student"))) (m "bite"))))
  ;; Ginger bit a female student.
  (is (false? ((m "Ginger") (((m "a") ((m "female") (m "student"))) (m "bite"))))))
