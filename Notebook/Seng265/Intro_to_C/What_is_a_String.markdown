# What is a String
Created Thursday 29 September 2016

[+char arrays](./What_is_a_String/char_arrays.markdown)
[+String functions & Pointers](./What_is_a_String/String_functions_&_Pointers.markdown)


* Strings as a datatype known in Java do not exist in C
* Memory for strings is not automatically allocated on assignment
* Concatenation via the "+" operator is not possible.
* The boundaries between strings are not enforced by the C runtime


**A string is a character array**

* A C string is stored in a character array
* The start of a string is an address to a char
	* **The start of the string need not be identical with the start of an array!**
* The end of a string is indicated with a **null character **('\0')
* The size of a string need not necessarilty be the same size as the character array which it is stored
* C strings are often manipulated using special functions
	* strncpy()
	* strcmp()
	* strncat()
	* strtok()
* C strings are sometimes accessed char by char

![](./What_is_a_String/pasted_image.png)
![](./What_is_a_String/pasted_image001.png)
[+char arrays](./What_is_a_String/char_arrays.markdown)
[+String functions & Pointers](./What_is_a_String/String_functions_&_Pointers.markdown)


