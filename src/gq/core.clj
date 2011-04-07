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

(defprotocol SyntacticTreeNode
  "Specifies a node in a parse tree."
  (get-category [this] "Returns symbol as category label.")
  (get-index [this] "Returns integer index for binding NPs, empty categories, and verb arguments.")
  (get-lexical-content [this] "Returns string content of leaf node.")
  (get-children [this] "Returns list of child nodes.")
  (has-children? [this] "Returns true when this has no child nodes.")
  (lexical-node? [this] "Returns true when this is a lexical (leaf) node.")
  (trace-node? [this] "Returns true when this is an empty trace node."))

(defrecord Node [category index lexical-content children]
  SyntacticTreeNode
  (get-category [this] category)
  (get-index [this] index)
  (get-lexical-content [this] lexical-content)
  (get-children [this] children)
  (has-children? [this] (and (not (nil? children)) (not (empty? children))))
  (lexical-node? [this] (and (not (nil? lexical-content)) (not (has-children? this))))
  (trace-node? [this] (and (nil? lexical-content) (not (has-children? this)))))

(defn m
  "Returns the interpretation of expression."
  [expression]
  (cond (lexical-item? expression) (m-lexical-item expression)
        (number? expression) expression
        (list? expression)
        (cond (> (count expression) 1)
              ((m (first expression)) (m (rest expression)))
              :else
              (m (first expression)))
        (map? expression)
        (cond (lexical-node? expression)
              (m-lexical-item (get-lexical-content expression))
              (trace-node? expression)
              #(identity %)
              (= :s (get-category expression))
              ((m (first (get-children expression))) (m (rest (get-children expression))))
              (= :np (get-category expression))
              ((m (first (get-children expression))) (m (rest (get-children expression))))
              (= :vp (get-category expression))
              (m (first (get-children expression))))
        :else
        (throw (IllegalArgumentException. (str "Unknown type: " (prn-str expression))))))

