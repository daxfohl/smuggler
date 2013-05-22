(ns smuggler.core
  (:gen-class))

(defrecord Doll [name weight value])

(defn m [seq w]
  (let [head (first seq)
        tail (rest seq)
        head-w (:weight head)
        head-v (:value head)]
    (if (= head nil)
      {:value 0 :flags '()}
      (if (> head-w w)
        (let [without-head-init (m tail w)]
            {:value (:value without-head-init) :flags (cons false (:flags without-head-init))})
        (let [without-head-init (m tail w)
              with-head-init (m tail (- w head-w))
              without-head-value (:value without-head-init)
              with-head-value (+ head-v (:value with-head-init))]
          (if (> without-head-value with-head-value)
            {:value without-head-value :flags (cons false (:flags without-head-init))}
            {:value with-head-value :flags (cons true (:flags with-head-init))}))))))

(def dolls
  [(Doll. "luke" 9 150)
   (Doll. "anthony" 13 35)
   (Doll. "candice" 153 200)
   (Doll. "dorothy" 50 160)
   (Doll. "puppy" 15 60)
   (Doll. "thomas" 68 45)
   (Doll. "randal" 27 60)
   (Doll. "april" 39 40)
   (Doll. "nancy" 23 30)
   (Doll. "bonnie" 52 10)
   (Doll. "marc" 11 70)
   (Doll. "kate" 32 30)
   (Doll. "tbone" 24 15)
   (Doll. "tranny" 48 10)
   (Doll. "uma" 73 40)
   (Doll. "grumpkin" 42 70)
   (Doll. "dusty" 43 75)
   (Doll. "grumpy" 22 80)
   (Doll. "eddie" 7 20)
   (Doll. "tory" 18 12)
   (Doll. "sally" 4 50)
   (Doll. "babe" 30 10)])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
