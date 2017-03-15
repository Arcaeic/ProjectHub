# Structures
Created Thursday 06 October 2016


* some language refer to these as records
* Aggregate data type
	* Multiple variable declarations inside a single structure
	* **Variables can be of diff types**
* Struc itself becomes a new data type
* Ex:

struc day_of_year {
int month;
int day;
int year;
float rating

* Note: No methods or functions can be associated with such a datatype
* Structures are used to create new aggregate types
* Declarations come in the following forms:
	* struct {int x; int y;} id;
	* struct point { int x; int y;};
	* struct point { int x; int y;} x, y, z[10]
	* typedef struct point { int x; int y; } Point;
		* struct point is a new type, Point is a synonym.
* Format 4 is easiest to maintain.
* To access members of a structure we emply the **member operator** (".") denoted by, **x.y**, and reads: "Get the value of member y from structure x"
* arrays of structures can be defined.


**Type Definitions! (typedef)**

* C allows a programmer to create a synonym  for data types
	* The new name is a synonym of an already defined type
	* Syntax: typedef datatype synonym
* Examples:

typedef unsigned long int ulong;
typedef unsigned char byte;
ulong x, y, z[10];
byte a, b[33]; 

