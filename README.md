# smuggler

This program is a command-line implementation of the recursive solution of the 0/1 Knapsack problem in Clojure.  It allows only integers and thus is able to use the integer-weight pseudopolynomial solution from wikipedia.  It transforms the loop into a tail-recursion-friendly `reduce` function, so there is no chance of blowing the stack.  It is much faster than the floating-point version in `master`, assuming the max-weight is not large compared to factorial of the number of dolls.

## Installation

Clone this repository into a workstation that has lein and clojure installed.
This was developed and tested against Leiningen 2.0.0 on Java 1.7.0_21 Java HotSpot(TM) Client VM.

## Usage

    $ lein run [input-filename]

## Examples

A sample input file would be of format:

    max-weight
    name1,weight1,value1
    name2,weight2,value2

For example
	
    100
    harold,80,49
    will,60,82
	
Only natural number weights and values are supported.

### Bugs

None known

### Future Releases

* Separate puzzle logic and parsing logic onto different files.

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.