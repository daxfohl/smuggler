# smuggler

This program is a command-line implementation of the recursive solution of the 0/1 Knapsack problem in Clojure.

## Installation

Clone this repository into a workstation that has lein and clojure installed.

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

### Bugs

The method m (named after the `m` function in the Knapsack Problem wikipedia article) is not tail-recursive and will explode for a long-enough list of dolls.  However the algorithm is exponential time wrt the number of dolls, so a list of dolls long enough to blow the stack would also take decades to compute on modern hardware.  By that time all your clients will have moved on to another dealer.

### Future Releases

* Separate puzzle logic and parsing logic onto different files.
* Investigate pseudo-polynomial algorithm on wikipedia page; while the article claims it only works on integer weights, but if we index the weights it should work in that way too.

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.