# • Regexp

* Created Thursday 08 December 2016



* A language independant approach to expressing patterns
* Part of a hierarchy of languages
	* Type 0: Unrestricted Grammars
	* Type 1: Context-sensitive
	* Type 2: Context-free
	* Type 3: Regular grammars
* Type 2 and 3 are mainly used in Comp Sci.
	* Type 2 is used in parsers for computer languages (such as compilers)
	* Type 3 is used in regexp and lexical analyzers for compilers

	

* **Grep:**
	* Regexp for bash is called grep
	* Grep accepts two sets of arguments:
		* A regular expression
		* A set of files through which grep will try to find strings matching the regexp
	* *grep <condition> <file.txt>*
	* *ex: grep -w apple fruitstuff.txt*

	

* **Regexp Metasymbols:**

.
Match any character except \n
a*
Match 0 or more reps of 'a'
a+
One or more reps of 'a'
a?
0 or 1 reps of 'a'
a{5}
Exactly 5 reps of a
a{3,7}
3 to 7 reps of a
[abc]
Any one character in the set
[^abc]
Any one charcter not in thr set
a|b
a or b
(...)
group a component of symbols in regexp
\
Escape a metasymbol
\d
any decimal digit character
\w 
any alphanumeric word
\s
Any whitespace character (\t\n\r\f\v)
\b
Empty string at a word boundary
^
Match 0 characters at start of string
$ 
match 0 characters at the end of string
\D
Match any non-digit character
\W 
match any non-alphanumeric charcter
\S 
match any non-whitespace character
\B
Empty string not at a word boundary

* ![](./Regexp/pasted_image.png)
* ![](./Regexp/pasted_image001.png)


#### • Re.sub

* re.sub(r<location>, <replacement> <originalString>)
	* re.sub(r"=LOCATION=", location,  string)




