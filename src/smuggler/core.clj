(ns smuggler.core
  (:gen-class))

(defrecord Doll [name weight value])

(defn get-value [seq]
  (reduce #(+ %1 (:value %2)) 0 seq))

(defn m [seq w]
  (if (= seq '())
    '()
    (let [head (first seq)
          tail (rest seq)
          without-head (m tail w)]
      (if (> (:weight head) w)
        without-head
        (let [with-head (cons head (m tail (- w (:weight head))))]
          (if (> (get-value without-head) (get-value with-head))
            without-head
            with-head))))))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
