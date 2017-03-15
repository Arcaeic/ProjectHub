# • A simple compilation

* Created Wednesday 23 November 2016



* Your program consists of file.h, file.c
* You compile file.c: **gcc file.c**
	* Generates first file.o (then a.out if we dont specify an executable)



* ![](./A_simple_compilation/pasted_image.png)
* Good programming practice suggests we break program into smaller modules


#### • Example:

* compiling two source C files with a common include file (red.c, yellow.c, common.h)
* **gcc red.c yellow.c**
* Compiler translates red.c and yellow.c into object files, and creates an executable "a.out".
* (or we can compile them one at a time)


* ![](./A_simple_compilation/pasted_image001.png)
	* Each generated file depends on others to be creates
		* (red.o depends on red.c, and common.h)
		* (yellow.o depends on yellow.c, and common.h)
	* In  general, each created file depends on at least one input file
	* This dependency relationship can be drawn as a dependency graph, as above.
* ![](./A_simple_compilation/pasted_image002.png)
	* Using makefile from above:
* ![](./A_simple_compilation/pasted_image003.png)
	* typing **make** with no arguments means **use first rule in the makefile.**
* rules: consist of three parts:
	* **target**: some name
		* Could be name of program
		* could be a name we give to a set of programs
	* **dependencies:** list of files (could be empty)
	* **command**: UNIX command needed to perform work on target
	* comments are python-style (lines beginning with '#')
* variables:
	* clear up redundancy
	* eases modification of makefiles
	* defined on their own line
	* used with a combination of $ and ()
	* If you wish to refer to "$" in a makefile, use $$



#### • Example

* ![](./A_simple_compilation/pasted_image004.png)




