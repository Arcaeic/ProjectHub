# • Python3

* Created Thursday 08 December 2016


• Final Review
==============

* Created Thursday 08 December 2016


#### • Python:

* Logical operators are words (and / not / or)**l**
* No brackets, braces, or semicolons
* No declaration of data types
* **compiling:**
	* Python3 file.py
* **Lists:**
	* Arrays have 2 types:
		* Dictionaries
		* Lists
	* Lists are mutable
* **Tuples:**
	* Immutable
	* Items can be of mixed types, incluiding collection types
	* **Why use em?**
		* Faster than lists
		* Can be used as dictoinary keys
		* Can check if a tuple is in a list.
* **Strings:**
	* Substrings can be accessed using indexing
	* Immutable
	* Almost like a tuple
	* Can be split into a list
		* foo.split(" ")
	* Getting length
		* len(foo)
	* Join a string from lists
		* "-".join(bar)
	* Count the number of substring in string
		* foo.count("A")
	* Replace substring in string with another
		* foo.replace(String, "word")
	* Get FIRST index of substring in string
		* foo.find("am")
	* Others:
		* foo.startswith("I")
		* foo.endswith("word")
	* The built in str() function can convert an instance of any type into a string
		* "Hello " + str(2)
* **Difference between Lists, tuples, Strings**
	* Tuples and strings are immutable
	* Lists can be modified and changed in placed
	* We can convert between lists and tupels
		* li = list(tu)
		* tu = tuple(li)
* **Dictionaries**
	* A mapping type
	* Store a mapping between a set of keys and a set of values
		* Keys can be anything immutable
		* Values can be any type
	* You can define, modify, view, loookup, and delete k-v pairs in dictionaries
		* d = {'user' : 'bozo', 'pwd' : 1234}
		* d['user'] = clown
	* k-v are paired using a colon :
	* Duplicate values are allowed, no dupe keys allowed
	* Dictionaries are mutable
		* A[key1] = value3
			* {key1:value3, key2:value2}
	* **Dictionary methods:**
		* Grab list of keys:
			* D.keys()
		* Get list of values:
			* D.values()
		* Grab list of (key,value) tuples
			* D.items()
		* Remove all elements
			* D.clear()
* **Indexing:**
	* NegativeIndexing:
		* A = [1,2,3]
		* A[-1] -> 3
	* Range indexing
		* B = [1,2,3,4,5]
		* B[1:3] -> 2,3
* **Looping:**
	* for i in range(5)
		* doSomething()
	* A = [x for x in range(5)]
		* [0,1,2,3,4]
* **Importing:**
	* import argparse as ap
	* from sys import argv
* **Functions:**
	* Functions begin with 'def'
	* Functions without a return actuall return a constant **None**
	* Can be arguments to other functions
	* Return values of functions
	* Parts of tuples, list, etc
* **File I/O**
	* with open(filename, params) as file:
		* file.read() reads all contents
		* file.readlines() reads in lines
		* file.readline() Read the next line
	* Looping through the lines of a file directly:
		* with open(filename, "rt") as file:
			* for line in file: #Does not remove \n
			* line = line.rstrip()
			* output = handleFileRow(line)
	* Writing to a file:
		* with open(filename, "w") as file: #will overwrite if filename exists
			* file.write("Stuff\n")
			* file.write("{} is easier with {} variables".format("This, 2))




