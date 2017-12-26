# Program Scope
Created Thursday 06 October 2016


* The scope of a variable determines the region over whihc you cana ccess the variable by its name
* C provides four types of scopes
	* program scope
	* file scope
	* function scope
	* block scope
* The variable exists for the program's lifetime and can be access from any file comprising the program
	* To define a global keyword, omit the extern keywordl, and include an initalizer
* Example:
	* Variable with program scope is declared and referenced in file 1
	* Variable with program scope is referenced in file 2.
* ![](./Program_Scope/pasted_image.png)
* the variable is visible from its point of declaration to the end of the source file
* to give a variable scope, define it outside a function with the **static** keyword.
* The name is visible from the beginning to the end of a function
* According to the ANSI stand, the scope of function arguments is the same as the scope of variables defined at the outmost scope of a function
* shadowing of function arguments is illegal (initalizing a var with the same name as a parameter), but shadowing of global variables is permitted (naming a variable the same as a global var)

**Block Scope**

* the variable is visible from its point of declaration to the end of the block.
	* A block is anything surrounded by curly braces, can be called anywhere to indicate scope, not just at function definitions or loops.


