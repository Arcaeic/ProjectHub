# Regular Expressions and Grep
Created Thursday 27 October 2016


* Needed: a language-independant approach to expressing patterns
* Solution: a **regular expression**
	* Sometimes called a **regex **or **regexp**
* They are written in a formal language and have the property that we can be very fast recognizers for them
* Part of a hierarchy of languages
	* Type 0: Unrestricted grammars
	* Type1: Context-sensitive
	* Type2: Context-free
	* Type3: regular grammars
* Type 2 and 3 are used in Comp. Sci
	* Type 2 is used in parsers for computer languages n(ie compilers)
	* Type 3 is used in regular expressions and lexical analyzers for compilers



* **Grep**
	* We already can use regexp in UNIX at the command line
	* The grep utility accepts two sets of arguments
		* grep: **global regular expression point**
		* argument 1: a regular expression
		* argument2: A set of files through which grep will try to find strings matching the regex
	* The syntax for a regex is grep is somewhat simimlar to what we will use Python
		* Grep is a very old tool (i.e from 1973)
		* Superseded somewhat by fgrep (fixed-string grep)
		* A variety of extensions, opimizations, etc exist
	* Example: search for variants of apple:



* ![](./Regular_Expressions/pasted_image.png)



* our grep examples were relatively simple
* Sometimes we want to denote more complex sets of strings
	* Strings where the beginning and end match a pattern, while everything in between can vary
	* All possible spellings of a particular name
	* Match non-printable chars
	* Catch possible misspellings of a particular word
	* match unicode code points
* And we may want even more:
	* When matching patterns to string, extract the actual match itself
	* Look for strings where the matched pattern repeats exactly later in the string
	* extract multiple matches from one string

**Metasymbols**

* Fully fledged regexes initally look intimidating because of the metasymbols
* However, all that is required to understand them is patience
* Regexes never loop
	* nor are they recursive
* They read left-to-right
* ![](./Regular_Expressions/pasted_image001.png)
* ![](./Regular_Expressions/pasted_image002.png)

![](./Regular_Expressions/pasted_image003.png)
The match succeeded when applied to text1
the result is a match object
This has an interface which is used to extract matched substrings
In this case, we extracted the substring matching the pattern in the parentheses.
the parameter passed to group corresponds to the order of left parenthesis.
A regexp can have several groups given the use of parenthessis
Groups can be nests, but can never overlap
		
so far we have seen the use of re.match
For occasional regex matching this is fine
but whenever the match is performed, the Python interpreter must re-interpret the regex.
Means the refex must be re-parsed and the state machine re-constructed
If we want to search many strings using the same regex, it makes sense to eliminate the overhead of repeating this work
To elimate repeated work, we compile the pattern


### A compiled pattern

![](./Regular_Expressions/pasted_image004.png)

Python's re module has methods for:
matching
searching
substituting
precompiling
splitting
iterating through matches
Match objects also have methods:
group()
groups(), groupdict()

![](./Regular_Expressions/pasted_image005.png)

![](./Regular_Expressions/pasted_image006.png)
![](./Regular_Expressions/pasted_image007.png)
Although grouping may be used to control the regexp match, not all results need to be extracred
notice that sometimes part of the exstracted matches is ignored.
There are often more than one way to phrase the same regexp
[/d/d](file:///d/d) is the same as [/d{2}](file:///d%7B2%7D)
The preferred method depends on the readability of the code
	
**Problem Solving**
Python supports **shortstrings **and **longstrings**
All of our strings have been shortstrings
Docstrings are longstrings (strings delimited with """)
We can use longstrings to form a textual document
**Problem 6: Nigerian Form Letters**
We have a text block we want to customize
There are certain spots in the text block where we have "tags" that must be replaced with specific strings
We want to do this with regexp
![](./Regular_Expressions/pasted_image008.png)
To fill out the form we could perform the following **substitutions:**
Contents of "place" replace all spots with =LOCATION=
Contents of "title" replace all spots with =TITLE=
contents of "cash" repalce all spots with =AMOUNT=
Contents of "cashtext" repalce all spots with =AMOUNTSPELLED=
**We will be using re.sub()**
it takes atleast three arguments:	**searchpattern, replacement pattern, target string**
			
**Note use of """**
![](./Regular_Expressions/pasted_image009.png)
![](./Regular_Expressions/pasted_image010.png)
Will result in a longstring:
![](./Regular_Expressions/pasted_image011.png)



