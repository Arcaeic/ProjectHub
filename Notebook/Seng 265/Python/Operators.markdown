# Operators
Created Thursday 13 October 2016

In
--

* Boolean test whether a value is inside a container
	* t = [1,2,3,5]
	* 4 in t returns false
	* 4 not in t returns true
* For Strings:
	* s = "abcde"
	* 'c' in s returns true
	* 'c' not in s returns false
	* 'ac' returns false
	* 'cd' returns true
* careful: in is also used in the syntax of for loops and list comprehensions


+
-

* The + operator produces a new tuple, list, or string whose value is the concatenation of its arguments
* It creates a **new** list, in other words, a new reference in memory. In contrast to extend, that uses the old reference and modifies it.


*
-
The * operator produces a new tuple, list, or string that repeats the original content
multiplication, in essence

Operations on Lists only
========================

.insert and .append
-------------------
When fucking with lists, .insert(int pos, value) will insert the value at the given pos
.append(value) will add the value to the end of the list.

Extend takes a list as an argument
Append takes only a singleton, A.K.A a single thing, whether its a list, int, value.

.extend vs append
-----------------
.extend(value(s)) will append one or more than one value to the end of the list. If you extend a list with another list it will add the contents of value into the original ist

.append will add the list object into the list.

.index
------
.index(value) will return the index of the first occurance of *value*
.count(value) will return the number of occurances of *value*
.remove(value) will remove the specified *value, *not returning anything
.reverse() will reverse the list.
.sort() will sort the list.
Using .sort(some_function) will use a user-defined comparison.





