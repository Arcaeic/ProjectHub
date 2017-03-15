# Data Types
Created Monday 26 September 2016

![](./Data_Types/pasted_image.png)

* C supports the following aggregate data types:
	* struct types: one mechanism to decalre user-defined types
		* like records in Pascal/Modula/Oberon
		* we'll look at these later
		* signif. different from Java classes
* Array types: you can define an array of any scalar or aggregate type
	* describe these later
* Union types: similar to structs, but members are overlaid (sharing storage)

**Literals**

* Character constants (8-bit ASCII)
	* char ch = 'A', bell = '\la', formfeed = '\f';
* Numeric literals
	* integer:
		* int a = 10, b = 0x1CE, c = 0777;
		* unsigned int x = 0xfffU;
		* long int y = 2L;
	* Floating point
		* float x = 3.1415F;
		* double x = 1.5, y = 2.5E10, z = -2.5e-10

**String literals**

* String literals
	* char *s = "unable to open file\n";
* We will get to C strings in due course, but here is an early warning:
	* The variable "s" above might appear to act like a string
		* but it is actually a variable holding an address to a "static string table"

**Storage Classes**

* C provides the following four storage classes:
	* auto: applies only to variables declared at function scope
	* register: a hint to the complier to place a variable in a CPU register
	* static: internal linkage, and static storage allocation
	* extern: external linage, not a definition
* Storage classes are used to modify a veriable declaration or definition
* Current practice
	* avoid using "auto" or "register"
	* use "static" or "extern" to control variable visibility, and these will be the only storage classes we will use in this course.
* Defining variables with a storage class modifier:
	* <storage class> <type> <name>;
	* or <storage class> <type> <name> = <value>;



