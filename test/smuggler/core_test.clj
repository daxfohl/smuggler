(ns smuggler.core-test
  (:use clojure.test
        smuggler.core)
  (:import smuggler.core.Doll))

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

(defn getf [s] (Float/parseFloat s))

(deftest algorithm-works-on-sample
  (testing "Algorithm provides the correct result with the given sample"
    (let [output (m dolls 400)]
      (is (= (get-value output) 1030))
      (is (= (count output) 12)))))

(deftest algorithm-works-on-empty-set
  (testing "Empty set should always return empty set"
    (is (= '() (m [] 400)))))

(deftest algorithm-works-on-zero-weight
  (testing "Algorithm will provide only zero-weight dolls if zero weight allowed"
    (let [dolls [(Doll. "0" 0 50) (Doll. "1" 1 50)]
          output (m dolls 0)]
      (is (= (filter #(zero? (:weight %1)) dolls) output)))))

(deftest algorithm-throws-on-negative-doll-weight
  (testing "Algorithm will abort with an exception if the weight is negative"
    (is (thrown? IllegalArgumentException (m [(Doll. "-1" -1 20)])))))

(deftest try-parse-float-works
  (testing "try-parse-float returns the correct number or nil"
    (is (= (try-parse-float "38.3") (getf "38.3")))
    (is (= (try-parse-float "-5") (getf "-5")))
    (is (not (nil? (try-parse-float " 39 "))))
    (is (nil? (try-parse-float "fisidj")))))

(deftest doll-valid-works
  (testing "doll-valid rejects invalid dolls and accepts valid ones"
    (is (not (doll-valid? (Doll. nil 1 1))))
    (is (not (doll-valid? (Doll. "a" nil 1))))
    (is (not (doll-valid? (Doll. "a" 1 nil))))
    (is (doll-valid? (Doll. "" 0 0)))
    (is (not (doll-valid? (Doll. "a" 1 -1))))
    (is (not (doll-valid? (Doll. "a" -1 1))))))

(deftest parse-doll-works
  (testing "parse-doll returns correctly parsed dolls for valid dollstrings but nil for invalid ones"
    (is (nil? (parse-doll "")))
    (is (nil? (parse-doll "a,1,-1")))
    (is (= (Doll. "a" (getf "1") (getf "1")) (parse-doll "a,1,1")))))

(deftest parse-dolls-works
  (testing "parse-dolls returns correctly parsed dolls for valid dollstrings but nil if any is invalid"
    (is (= '() (parse-dolls [])))
    (is (nil? (parse-dolls ["a,1,1" ""])))
    (is (= 3 (count (parse-dolls (repeat 3 "a,1,1")))))))

(deftest parse-data-works
  (testing "parse-data returns exit code plus result for dolls/weight both valid, or nil for either invalid"
    (is (= [FILE-EMPTY nil] (parse-data "\n")))
    (is (= [FILE-EMPTY nil] (parse-data "")))
    (is (= [FILE-INVALID nil] (parse-data "x")))
    (is (= [FILE-INVALID nil] (parse-data "400\nbad-doll")))
    (is (= [OK [[]  (getf "400")]] (parse-data "400")))
    (is (= [OK [[(Doll. "a" (getf "1") (getf "1"))] (getf "400")]] (parse-data "400\na,1,1")))))

