This assignment was locked Dec 10, 2021 at 6am.
Download HW5.hs  Download HW5.hs. For each of the three variables, write a regular expression (as a string) corresponding to the DFAs given below. Ensure that the regex you provide can be parsed using the parser provided in REP.hs.

For example,
```
*REP> parse (pREof pAlphabet) "A*B*"
Just (RSeq (RStar (RSym A)) (RStar (RSym B)))
```
You may use any of the posted sample code to check your answers, but only submit the regular expressions themselves.
