# Sequence Types
Created Thursday 13 October 2016


* Tuple
	* A simple immutable ordered sequence of items
	* Items can be of mixed types, including collection types
* Strings
	* Immutable
	* Conceptually very much like a tuple.
* List
	* Mutable order of sequence items of mixed types



* All three sequences share much of the same syntax and functionality
* Key diff:
	* Tuples and strings are immutable
	* Lists can be modified and changed in place
* the following operations can be applied to all sequence types
* We can access any piece of a sequence using array notation li[i], tu[i], etc
	* Negative indentation is also possibly, used to access starting from the back of the list
	* using range indexing, It is exclusive.
		* Ex: [1:4], seq[1] is contained up to [4-1]
		* Ex: [1:-1]
	* You can omit the first index to make a copy starting from the beginning of the container
		* [:4]
		* Omitting the second index does the opposite.
			* [3:]
	* To copy the entire list, use seq[:]
	* Note:

![](./Sequence_Types/pasted_image.png)


Tuples
------
Tuples are defined using parentheses and commas
tu = (23, 'abc', 4.56, (2,3), 'def')


Lists
-----
Lists are defined using square brackets and commands
li = ['abc', 34, 4.34, 23]
	
Strings
-------
Strings are defined using quotes
st = "Hello World"
'Hello World'
"""Totally rad "dude""""
		
		


