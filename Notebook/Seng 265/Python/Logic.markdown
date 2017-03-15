# Logic
Created Friday 14 October 2016


* True and False are constants in python
* Other values equiv to true and false
	* False: zero, None, empty container or object
	* True: non-zero number, non-empty objects
* Comparison operators: ==, !=, <, <=, etc
	* X and Y have same value: X == Y
	* compare with X is Y
		* X and Y are two variables that refer to the identical same object
* Special properties of and and or
	* actually and or dont return True or False
	* They return the value of one of their sub-expressions (which may not be a boolean value)
		* X and Y and Z
			* If true, returns Z
			* if false: returns value of first false
		* X or Y or Z
			* If false: returns Z
			* If true: Returns value of first true
	* x = (*true_value* if *condition *else * false_value)*



