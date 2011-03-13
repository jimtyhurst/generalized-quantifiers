(ns gq.test.core
  (:use [gq.core] :reload)
  (:use [clojure.test]))

(deftest test-every
  (is ((m "every") (m "dog") (m "barked")))
  (is (false? ((m "every") (m "student") (m "studied")))))

(deftest test-some
  (is ((m "some") (m "dog") (m "barked")))
  (is ((m "some") (m "student") (m "studied")))
  (is (false? ((m "some") (m "student") (m "barked"))))
  (is (false? ((m "some") (m "dog") (m "studied"))))
  (is (false? ((m "some") (m "developer") (m "laughed")))))

(deftest test-most
  (is ((m "most") (m "dog") (m "barked")))
  (is ((m "most") (m "student") (m "studied")))
  (is (false? ((m "most") (m "student") (m "laughed"))))
  (is (false? ((m "most") (m "developer") (m "barked"))))
  )
