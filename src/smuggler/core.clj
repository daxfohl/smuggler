(ns smuggler.core
  (:gen-class))

(def ^:const OK 0)
(def ^:const NO-FILENAME-GIVEN 1)
(def ^:const FILE-NOT-FOUND 2)
(def ^:const FILE-EMPTY 3)
(def ^:const FILE-INVALID 4)

(defrecord Doll [name weight value])
(defrecord Calc [dolls sum-value])

(defn aggregate-value
  "Aggregates the value of all the value fields in a seq of Dolls"
  [dolls]
  (reduce #(+ %1 (:value %2)) 0 dolls))

(defn add-doll
  "Adds a doll to the `Calc` record, meaning it conjugates it to the vector and increases the cached value"
  [doll calc]
  (Calc. (conj (:dolls calc) doll) (+ (:sum-value calc) (:value doll))))

(defn next-cell
  "Determines the next cell at index `j` given the previous vector and the current doll in the `m` algorithm
  from wikipedia's Knapsack Problem page"
  [prev-vector doll j]
  (let [heaviest-set-without-head (nth prev-vector j)
        doll-weight (:weight doll)]
    (if (> doll-weight j)
      heaviest-set-without-head
      (let [heaviest-set-allowing-head (nth prev-vector (- j doll-weight))
            heaviest-set-with-head (add-doll doll heaviest-set-allowing-head)]
        (max-key :sum-value heaviest-set-without-head heaviest-set-with-head)))))

(defn next-vector
  "Determines the next vector in the reduction of the `m` algorithm from wikipedia's Knapsack Problem page"
  [prev-vector doll]
  (vec (map #(next-cell prev-vector doll %1) (range (count prev-vector)))))

(defn m
  "Calculates an optimal set of dolls that provides the most value but is under the weight limit given.
  This algorithm assumes all weights are non-negative integers, and may produce suboptimal results if negative
  weights are supplied."
  [dolls w]
  (let [m0 (vec (repeat (+ 1 w) (Calc. [] 0)))
        mf (reduce next-vector m0 dolls)]
    (:dolls (last mf))))

(defn print-dolls
  "Pretty-prints each doll to the console"
  [dolls]
  (doseq [doll dolls]
    (prn doll)))

(defn run-with-data
  "Runs the optimization algorithm and prints the results"
  [dolls weight]
  (let [dolls-to-keep (m dolls weight)]
    (print-dolls dolls-to-keep)))

(defn try-parse-int
  "Parses an int string, or returns nil if string is invalid"
  [s]
  (try (Integer/parseInt (clojure.string/trim s))
       (catch Exception e
         (do (prn (str "Value string given by \"" s "\" is not valid."))
             nil))))

(defn doll-valid?
  "Validity check for any null or negative values"
  [doll]
  (let [name (:name doll)
        weight (:weight doll)
        value (:value doll)]
    (and (not-any? nil? [name weight value])
         (not-any? neg? [weight value]))))

(defn parse-doll
  "Parses a doll string, given in format \"name,weight,value\", e.g. \"alice,4,9\".
  Non-negative floats are accepted for weight and value.
  Returns nil if string or resulting doll is invalid.
  Anything after a third comma is silently ignored."
  [s]
  (let [split (clojure.string/split s #",")
        [name weight-string value-string] split
        [weight value] (map try-parse-float [weight-string value-string])
        doll (Doll. name weight value)]
    (if (doll-valid? doll)
      doll
      (do (prn (str "Doll string given by \"" s "\" is not valid."))
          nil))))

(defn parse-dolls
  "Parses the doll-strings into a doll seq, but returns null if any Doll is invalid"
  [strings]
  (let [dolls (map parse-doll strings)]
    (if (some nil? dolls) nil dolls)))

(defn parse-data
  "Parses the full data file into a format `[RETURN-CODE [dolls weight]]`.
  `contents` is a multi-line string where the first line that contains the `weight` in `float` format,
  followed by N lines of doll strings (see `parse-doll`)"
  [contents]
  (let [lines (remove clojure.string/blank? (clojure.string/split-lines contents))]
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

(defn run-with-contents
  "Runs the smuggler algorithm given the contents of the a data file.
  See `parse-data` for the expected format of `contents`."
  [contents]
  (let [result (parse-data contents)
        [status data] result]
    (if (not= OK status)
      status
      (let [[dolls weight] data]
        (do (run-with-data dolls weight)
            OK)))))

(defn contents-or-nil
  "Returns the contents of a file, or nil if there is an error reading the file"
  [filename]
  (try (slurp filename)
       (catch Exception e nil)))

(defn run-with-file
  "Runs the smuggler algorithm with the corresponding input file"
  [filename]
  (let [contents (contents-or-nil filename)]
        (if (nil? contents)
          (do (prn (str "File \"" filename "\" not found or cannot be opened"))
              FILE-NOT-FOUND)
          (run-with-contents contents))))

(defn run
  "Runs the smuggler algorithm given the command-line arguments and returns the exit code"
  [args]
  (if (= args '())
    (do (prn "Supply a filename at the command prompt")
        NO-FILENAME-GIVEN)
    (let [filename (first args)]
      (run-with-file filename))))

(defn -main
  "Main entrypoint to program; expects the input file path to be the only command-line argument"
  [& args]  
  (System/exit (run args)))
