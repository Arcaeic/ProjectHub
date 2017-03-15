# • Lab 5

* Created Thursday 20 October 2016



* PYTHON!:



* Import stuff to not reinvent the wheel.



* Python differs a lot from C.
	* - No brackets, braces, semi-colons
	* - No explicit declaration of data types
	* - A function can return multiple types
	* - Use words, not symbols in conditionals



* Running and compiling takes only one command
	* - Python3 file.py
	* - You can also add #!usr/bin/env python3 to make it executable


• PYTHON LISTS
--------------

* Arrays have 2 types:
	* - Dictionaries: {key, value}
	* - Lists:
		* OneD = [ 1, "two", 3 ]
		* TwoD = 1,2],[2,3
	* - Lists are always passed by reference to functions.



* Indexing
	* Negative Indexing
		* A = [1,2,3]; A[-1]
	* Range indexing
		* B = [1,2,3,4,5]; B[1:3]; B[:-2]; B[1:];
	* Mutable
		* - Add an element:
			* A.append(6)
		* - Combine two lists into 1:
			* A.extend(B)


• PYTHON TUPLES
---------------

* Tuple
	* - A = (1,2)	(Note parenthesis)
* Tuples are immutable (cannot be changed)
* Why use thm?
	* - Faster than lists
	* - Can be used as dictionary keys
	* - Can check if a tuple is isnt a list use in or not in


• STRING MANIPULATION
---------------------

* Just like in C, a character can be accessed inside a string using indexing
	* foo = "I am a string"
	* foo[3]; foo[-3]; foo[2:4]
* You can split up a string into a list
	* bar = foo.split(" ") ; bar
* You can create a string from lists
	* "-".join(bar)
* Get the length of a string:
	* len(foo)
* Count the number of instances of substring in string
	* foo.count("am");	foo.count("a")
* Replace substring in string with another string
	* foo.replace("String", "word")
* Get FIRST index of substring in string
	* foo.find("a")
* Get LAST index of substring in string
	* foo.rfind("a")
* Does string start or end with substring?
	* foo.startswith("I")
	* foo.endswith("word")


• PYTHONIC LOOPING
------------------

* Python supports for-loops and while-loops
	* while True:
		* doSomething()
	* for i in range(5):
		* doSomething(i)
* In-line looping
	* A = [x for x in range(5)]
		* ->[0, 1, 2, 3, 4]


• IMPORTING MODULES
-------------------

* You can import libraries and rename them
	* import argparse as ap
* You can also import just stuff that you want/need
	* Only load argv from library sys
	* - from sys import argv
* If the module is not installed in B215 you cannot use it.



