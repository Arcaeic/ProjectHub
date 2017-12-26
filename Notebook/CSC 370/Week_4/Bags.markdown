# Bags
Created Thursday 28 September 2017


* A bag is like a set, but an element may appear more than once
	* Multiset is another name for "bag".



* {1,2,1,3} is a bag
* {1,2,3} is bag that is also a set
* Order in a bag is unimportant
	* {1,2,1} = {1,1,2}, but
	* [1,2,1] != [1,1,2] as sets


### Why bags?
SQL is actually a bag language
SQL will eliminate duplicates, but usually only if you ask it to do so explicitly.
Except for union, intersection, and fifference where the default is "set mode"

Since Union, Intersect, and Difference work with sets by default, we can make some new definiitons

### Bag Union
"An element appears in the union of two bags the sum of the number of times it appears in each bag"
{1,2,1} UNION {1.1.2.3.1} = {1,1,1,1,1,2,2,3}

### Bag Intersection
"An element appears in the intersection of two bags the minimum of the niumber of times it appears in either."
{1,2,1} INTERSECT {1,2,3} = {1,2}

### Bag Difference
"An element appears in difference A - B of bags as many times as it appears in A, minus the number of times it appears in B."
Never less than 0.
{1,2,1} - {1,2,3} = {1}


