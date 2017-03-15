# char arrays
Created Monday 03 October 2016


* Always be aware that C strings are really just C char arrays
* To store a string in your program
	* You must have enough room in some char array for all the string's chars plus one extra char for the null.
	* Therefore correct program behaviour often boils down to declaring char arrays which have correct sizes for your purpose.
* Must be scrupulous about specifying "maximum" sizes
	* note the third parameter of "strncpy"
* Also use "strncat" to append a string to an already existing string


![](./char_arrays/pasted_image.png)

