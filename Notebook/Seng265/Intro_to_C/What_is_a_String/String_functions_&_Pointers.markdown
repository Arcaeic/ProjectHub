# String functions & Pointers
Created Monday 03 October 2016


* In C, we can manipulate pointers in many ways
* This can help us when working with strings
	* char *cp = buffer is the same as cp = &buffer[0]
	* cp + n is the same as &buffer[n]
	* *(cp + n) is the same as buffer[n]
	* cp++ is the same as cp = cp + 1
	* *cp++ is the same as *cp, cp++
* To use strings functions, you must include string.h (#include <string.h>)
	* **strncpy (char *dest, const char *src, int length)**
		* Copies the contents of string src to the array pointed to by dest.
			* Src and dest should not overlap
	* **strncmp(const char *s1, const char *s2, int length)**
		* Compares the two strings s1 and s2, returning a negative, 0 or positive if s1 is lexicographically <, ==, > s2 respectively.
	* **strlen(const char *s)**
		* Compute the length of the string, not including the \0 character
* **Do not use strcpy!**
	* strcvpy() takes 2 parameters
		* destination char array
		* source char array
	* If the string in the src array is longer than the size of the destination,
		* then strcpy() will overwrite the end of destination array
		* and this is what happens in a buffer overflow attack


