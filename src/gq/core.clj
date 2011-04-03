(ns gq.core
  (:use [gq.model] :reload)
  (:use [clojure.set] :reload))

;; Interpretations for expressions with respect to a model.
;; Usage:
;;   (m expression)
;;
;; Note:
;; lexical-item? and m-lexical-item are defined in the model
;; namespace, because their implementation depends on the
;; implementation of the lexicon.

(defn m
  "Returns the interpretation of expression."
  [expression]
  (cond (lexical-item? expression) (m-lexical-item expression)
        (number? expression) expression
        :else (throw (IllegalArgumentException. (str "Unknown type: " (prn-str expression))))))

