# C Arrays
Created Thursday 29 September 2016


* An array is a group of data elements of the same type accessed using the same identifer (ex X[3], X[11])
* arrays may be statically or dynamically allocated. Static arrays cannot grow at runtime, while Dynamic can (using stdlib.h functions)
* Arrays may be multidimensional
* Access to the elements of an array is accomplished using integer indices
* If an array is dimensioned to hold **size** elements, the elements are indexed from 0 up to size-1.
* **C provides no array bounds checking**, so accessing elements beyond index size-1 or below index[0] can cause a segmentation fault
* static arrays can be auto-initialized in run-time.
* syntax for a one-dimensional array:
	* <storage class> <type> <identifier>[<size>]
		* <size> must be known at complile time


