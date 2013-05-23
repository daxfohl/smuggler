(ns smuggler.core
  (:gen-class))

(defrecord Doll [name weight value])

(defn get-value [seq]
  (reduce #(+ %1 (:value %2)) 0 seq))

(defn m
  "Calculates an optimal set of dolls that provides the most value but is under the weight limit given.
  This algorithm assumes all weights are non-negative, and may produce suboptimal results if negative
  weights are supplied."
  [seq w]
  (if (= seq '())
    '()
    (let [head (first seq)
          tail (rest seq)
          without-head (m tail w)
          head-weight (:weight head)]
      (if (neg? head-weight)
        (throw (IllegalArgumentException. "negative weights not supported"))
        (if (> head-weight w)
          without-head
          (let [with-head (cons head (m tail (- w head-weight)))]
            (if (> (get-value without-head) (get-value with-head))
              without-head
              with-head)))))))



(def ^:const OK 0)
(def ^:const NO-FILENAME-GIVEN 1)
(def ^:const FILE-NOT-FOUND 2)
(def ^:const FILE-EMPTY 3)
(def ^:const FILE-INVALID 4)

(defn print-dolls [dolls]
  (doseq [doll dolls]
    (prn doll)))

(defn run-with-data [dolls weight]
  (let [dolls-to-keep (m dolls weight)]
    (print-dolls dolls-to-keep)))

(defn try-parse-float [s]
  (try (Float/parseFloat s)
       (catch Exception e
         (do (prn (str "Value string given by \"" s "\" is not valid."))
             nil))))

(defn doll-valid? [doll]
  (let [name (:name doll)
        weight (:weight doll)
        value (:value doll)]
    (and (not-any? nil? [name weight value])
         (not-any? neg? [weight value]))))

(defn parse-doll [s]
  (let [split (clojure.string/split s #",")
        [name weight-string value-string] split
        [weight value] (map try-parse-float [weight-string value-string])
        doll (Doll. name weight value)]
    (if (doll-valid? doll)
      doll
      (do (prn (str "Doll string given by \"" s "\" is not valid."))
          nil))))

(defn parse-dolls [strings]
  (let [dolls (map parse-doll strings)]
    (if (some nil? dolls) nil dolls)))

(defn parse-data [contents]
  (let [lines (clojure.string/split-lines contents)]
    (if (or (= lines '()) (= contents ""))
      (do (prn "Requested file is empty")
          [FILE-EMPTY nil])
      (let [head (first lines)
            tail (rest lines)
            weight (try-parse-float head)
            dolls (parse-dolls tail)
            result [dolls weight]]
        (if (some nil? result)
          [FILE-INVALID nil]
          [OK result])))))

(defn run-with-contents [contents]
  (let [result (parse-data contents)
        [status data] result]
    (if (not= OK status)
      status
      (let [[dolls weight] data]
        (do (run-with-data dolls weight)
            OK)))))

(defn contents-or-null [filename]
  (try (slurp filename)
       (catch Exception e nil)))

(defn run-with-file [filename]
  (let [contents (contents-or-null filename)]
        (if (nil? contents)
          (do (prn (str "File \"" filename "\" not found or cannot be opened"))
              FILE-NOT-FOUND)
          (run-with-contents contents))))

(defn run [args]
  (if (= args '())
    (do (prn "Supply a filename at the command prompt")
        NO-FILENAME-GIVEN)
    (let [filename (first args)]
      (run-with-file filename))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]  
  (System/exit (run args)))
