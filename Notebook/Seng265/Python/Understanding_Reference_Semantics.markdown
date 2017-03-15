# Understanding Reference Semantics
Created Friday 14 October 2016


* **Assigment manipulates references**

x = y does not make a copy of the object y references
x = y makes x references the object y references.

* Example

a = [1,2,3] a references list.
b = a		b references a.
a.append(4)	
print b = [1, 2, 3, 4]

* Explained: x =3
	* An int 3 is created and stored in memory
	* A name x is created
	* A reference to the memory location storing the 3 is then assigned to x
	* So when we say x = 3
		* We mean that x now refers to the integer 3.
	* When we change the value of x by one:
		* the reference of name x is looked up, value retrieved
		* The 3+1 calculation occurs, producing a new data element 4 which is assigned to a fresh memory location with a new reference
		* x is now assigned to data element 4
		* data element 3 is not assigned to anything, is garbage collected by the compiler.
	* Assignment works just as it does in any other language. **As long as the data element is immutable**(objects such as strings, ints, chars, floats)
	* For mutable data types (lists, dictionaries, user-defined types), assignments work differently
		* When we change this data, we do it *in place*
			* We dont copy them into a new memory address each time
			* If we type r=x, and then modify r, both r and x are changed.



