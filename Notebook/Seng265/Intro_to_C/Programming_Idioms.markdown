# Programming Idioms
Created Wednesday 15 March 2017

### Programming idioms:

* "programming idiom"
	* Means of expressing a recurring construct in one or more programming langauges
	* Use of idioms indicates language fluency
	* also assumes some comfort with language
* idioms also imply terseness
	* expressions using idioms tend to be the ideal size
	* "terseness" can even have an impact as machine-code level
* non-string example: infinite loop

![](./pasted_image.png)

* Note!
	* Normally we use built-in library functions whenever possible
	* There is a built-in string-length function strlen
	* These libraries functions are very efficient and very fast (and bug free)
* Algorithm:
	* Function accepts pointer to a char array as a parameter
	* Some loop examines characters in the array
	* Loop terminates when null char is encountered
	* Number of char examined becomes string length


#### First example:
![](./pasted_image001.png)


* C Strings are usually manipulated via indexed loops
	* for loops
* For statements are suitable to use with loops
	* Where termination depends on the size of some array
	* Where term depends upon size of some linear structure
	* Where loop tests are at loop-top and loop-variable ipdate operations occur at the loop-end
* While statements are most suitable with loops:
	* Where termination depends on the change of some state
	* Where term depends on some prop of a complex data structure
	* where actual loop ops can possibly lengthen or shorten number of iterations


#### Second example:

![](./pasted_image002.png)


* C strings are most oftena ccessed via char pointers
* Accessing indiv characters by array index is rare
	* Principle is that strings are usually processed in one direction or another
	* that direction proceeds char by char
* More idiomatic usage also depends upon pointer arithmetic


#### Third example:

![](./pasted_image003.png)

#### Fourth example:

![](./pasted_image004.png)


* Char pointers were dereferenced
	* Value of dereference directly used to control loop
* Char pointers were incremented
	* The most idiomatic code combines dereferencing with incrementing
	* Example: *c++
	* Only works because "*" has a higher precendence than ++
	* Meaning of example:"read the value stored in variable

'c', read the memory address corresponding to that
value, return the value in that address as the expression
value, and then increment the address stored in variable
'c'."

#### Even better:

![](./pasted_image005.png)


