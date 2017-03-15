# Lab 6
Created Friday 04 November 2016

DICTIONARIES
------------
Are created using curly-brackets
A = {key1:value1, key2:value2, ...}; A = {}
They consist of a set of K-V pairs
No restrictons of values
No dupe keys, keys must be immutable, dupe values are allowed
Valid keys would be strings, tuples, numbers, etc
Dictionaries, like lists, are mutable
A[key1] = value3 -> {key1:value3, key2:value2}
del A[key1] = {key2: value2}
To loop through a dictionary:
for key,value in _dict.items():

Dictionary methods:
Get list of keys:
D.keys()
Get list of values:
D.values()
Get list of (key,value) tuples:
D.items()
Remove all elements:
D.clear()
	
	
Since dictionaries are mutable, they are not in-place!


LIST RECAPS
-----------
Last week we learned that we can make lists using brackets
A = [1,2,3]
You can loop through lists and modify existing lists
A = []
for x in range(0,6):
if x % 2 == 0;
A.append(x)
print(A) #-> [0, 2, 4]


LIST COMPREHENSION
------------------
List comp is a tool for transforming lists into another lists
An easier way of doing the previous example
A = [x for x in range (0,6) if x % 2 == 0]
	
Examples:
doubled = [n * 2 for n in range(0,10)	]
matrix = 1,2], [3,4 ;  flattened = [n for row in matrix for n in row	]
_dict  = {1:2, 3:4} ; flipped = {value: key for key, value in dict.items()	}
	
Dictionaries can also be converted into lists using this method


FUNCTIONS
---------
Any code resting in outermost indentation is considered global.
Functions are defined using def
	
def main()
print ("Ran from terminal.)
if "__name__" == "__main__":
main():
	

MAP
---
Map will take the contents of a list and pass them into a function
This returns an iterable map object that you can use in a for loop, amon	g others
	
You can convert a map object to a list by using list(map_object) is you want to access it directly.
It's faster to use it as an iterable
	
Ex: Converting string chars to int
one,two,three = map(int, ["1","2","3"])


FILTER
------
Similar to map, except it extracts the element from the list that returns true from the passed function
ex: Get all values divisble by 2:
	
def f(x): retun x % 2 == 0
	
F = filter(f, range(2,10))
print(F) -> <filter object @ 0xas897d9a6...>
print(list(F)) -> [2,4,6,8]


LAMBDA
------
Anonymous functions
Because they exist in the local scope of wherever you call the, you can pass them local variables
They are very handy to be used with map() or filteR()

Syntax:
You can assign a lambda as a variable
square = lamba some_number: some_number *= some_number
square(4) -> 16
Or run it using map
X = list(map(lamba x:x*x, range(0,4)))
print(X) -> [1,4,9]


### TRICKY STUFF WITH LAMBDA
A = [lambda x: x*x for x in range(0,3)]
This will give you a list of the same lambda functions from A[0] to A[3]
A[0] is the lambda function at list index = 0
A[0](1) you pass the lambda functiona 1, printing 1*1 = 1
You can pass local variables to a lambda, but beware!
x = 10
A = [lambda i: x*i for i in range(0,3)]
print(A[1](1)) -> 10
print(A[2](1) -> 10
	

FILE I/O
--------
Syntax:
with open(filename, params) as file:
file.read() #Reads all contents
file.readlines() #Read in lines
file.readline()  #Read the next line
The code inside "with" will only be run if the file is successfully opened. Use this piece with file-specific code
	
Looping through the lines of a file directly:
with open(filename, "rt") as file:
for line in file: #Does not remove '\n'
Line = line.rstrip()	#Remove trailing \n
output = handleFileRow(line)
	
Writing to a file:
with open(filename, "w") as file: #If a file exists and you use "w" will overwrite the file.
file.write("Stuff\n")
file.write("{} is easier with {} varibles".format("This", 2))


PYTHON QUIRKS
-------------
Python 3 returns iterators and generators from its list creation functions
If you were to print the result of a map, you would get a map objec that is an iterator
That means when you assign variables it leaves the iterator


