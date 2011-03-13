(ns gq.core
  (:use [gq.model] :reload)
  (:use [clojure.set] :reload))

;; Interpretations for expressions with respect to a model.
;; Usage:
;;   (m expression)

(defn m
  "Returns the interpretation of expression."
  [expression]
  (cond (lexical-item? expression) (m-lexical-item expression)
        :else "Unknown type"))

