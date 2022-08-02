# MarkovTextParser

This project takes an input text file and transforms the contents using a Markov chain algorithm. A user can use the simple React web UI to input a text file and a prefix size.

Assumptions of File:
- The file is within 0 and 2147483647 characters (the String limit in Java). With more time, an Iterable<String> or a String[] could be used to parse a longer text file through chunks.
- We are going to assume all words are separated by spaces.