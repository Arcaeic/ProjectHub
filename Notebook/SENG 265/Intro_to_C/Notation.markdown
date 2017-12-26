# Notation
Created Thursday 29 September 2016


* &x gets the address of variable x (**referencing**)
* *xf
* gets the contents of the memory location whose address is stored in variable x (**dereferencing**)



* note that * can appear in a variable declaration, but it will have a different meaning
	* double f = 30.0;
	* double *g = &f; 
	* printf("%lf	%lf\n", f, *g);
* **Pointer variables **are declared in terms of other types (scalar, or otherwise)
* More accurate to read the simpler variable declarations right-to-left
	* int *a;
	* double *f;
	* char *st[10];
* Note: in declaration the * is right beside and logically attached to the variable name
	* Declaration syntax is meant to remind programmer of the result of dereferencing the variable



