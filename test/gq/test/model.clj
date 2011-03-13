(ns gq.test.model
  (:use [gq.model] :reload)
  (:use [clojure.test] :reload))

(deftest test-lexical-item-predicate
  (is (lexical-item? "dog"))
  (is (lexical-item? "Rocky"))
  (is (false? (lexical-item? "unknown word"))))

