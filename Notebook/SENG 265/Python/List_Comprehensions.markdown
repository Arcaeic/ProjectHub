# List Comprehensions
Created Monday 17 October 2016

A powerful feature of Python
Generate a new list by applying a function to every member of an original list
Python programmers use list comprehensions extensively.
The syntax is somewhat tricky
Syntax suggests a for-loop, an in operation, or an if statement
all three are also used in the sytan of forms in list comp.

[*expression* for **name** in __list__]
Where *expression* is some calculation or operation acting on **name**
For each **name** of the __list__, the list comp:
sets **name **equal to that member
calculates the new value using *expression*
Which then collects the new values calculated, adding each into a new list that is then returned.
	
![](./List_Comprehensions/pasted_image001.png)

Filtered list comprehension
===========================


* [*expression for ***name** if __list__ if [filter]]
* [filter] determines whether *expression* is performed on each member of the __list__
* for each element of __list__, check if it satisfies the [filter] condition
* If it returns false, for the [filter] condition, it is omitted from the list before the list comp is evaluated.

![](./List_Comprehensions/pasted_image002.png)

Nested List Comprehensions
==========================


* Since list comp take sa list and produce a list as output, they are easily nested:

![](./List_Comprehensions/pasted_image003.png)

* The inner comprehension produces: [4,3,5,2]
* The outer comp takes in the result of the inner comp, and produces [8, 6, 10, 4]






