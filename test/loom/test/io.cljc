(ns loom.test.io
  (:require
   [loom.network-simplex :refer [build-graph]]
   [loom.attr :refer [add-attr attr attrs]]
   [loom.io :refer [dot-str]]
   #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing run-tests]]
      :clj  [clojure.test :as t :refer        [is are deftest testing]])))

(def simple-graph
  (build-graph
   [[:a :b {:capacity 4 :cost 3}]
    [:a :c {:capacity 10 :cost 6}]
    [:b :d {:capacity 9 :cost 1}]
    [:c :d {:capacity 5 :cost 2}]]
   [[:a {:demand -5}]
    [:d {:demand 5}]]))

(deftest dot-string-test
  (testing "Dot string for simple graph"
    (let [g simple-graph
          dot (dot-str g)]
      (is (= dot
             (str "digraph \"graph\" {\n"
                  "  \":c\" -> \":d\" [\"capacity\"=\"5\",\"cost\"=\"2\",\"label\"=\"1\"]\n"
                  "  \":b\" -> \":d\" [\"capacity\"=\"9\",\"cost\"=\"1\",\"label\"=\"1\"]\n"
                  "  \":a\" -> \":b\" [\"capacity\"=\"4\",\"cost\"=\"3\",\"label\"=\"1\"]\n"
                  "  \":a\" -> \":c\" [\"capacity\"=\"10\",\"cost\"=\"6\",\"label\"=\"1\"]\n"
                  "  \":c\"\n"
                  "  \":b\"\n"
                  "  \":d\" [\"demand\"=\"5\"]\n"
                  "  \":a\" [\"demand\"=\"-5\"]\n"
                  "}")))))
  (testing "Dot string for labeled simple graph"
    (let [g simple-graph
          node-label (fn [n] (str (name n) (if-let [d (attr g n :demand)] (str "(" d ")"))))
          dot (dot-str g :node-label node-label)]
      (is (= dot
             (str "digraph \"graph\" {\n"
                  "  \"c\" -> \"d(5)\" [\"capacity\"=\"5\",\"cost\"=\"2\",\"label\"=\"1\"]\n"
                  "  \"b\" -> \"d(5)\" [\"capacity\"=\"9\",\"cost\"=\"1\",\"label\"=\"1\"]\n"
                  "  \"a(-5)\" -> \"b\" [\"capacity\"=\"4\",\"cost\"=\"3\",\"label\"=\"1\"]\n"
                  "  \"a(-5)\" -> \"c\" [\"capacity\"=\"10\",\"cost\"=\"6\",\"label\"=\"1\"]\n"
                  "  \"c\"\n"
                  "  \"b\"\n"
                  "  \"d(5)\" [\"demand\"=\"5\"]\n"
                  "  \"a(-5)\" [\"demand\"=\"-5\"]\n"
                  "}"))))))


