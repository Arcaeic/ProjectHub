# How do I use C
Created Monday 26 September 2016


1. Write an application


$ vim hello.c
#include <stdio.h>
	
int main() {
printf("Hello, World!\n");
return (0);
}
	

2. Compile the source file into an object file

$ gcc  –ansi  –Wall  -c hello.c
	

3. Link the object file to the "Standard C Runtime Library" to produce an executable
	a. gcc -o hello hello.c
	b. gcc -o hello -lm hello.c               # use floating point math. 



4. Run the executable:

$ [./hello](./How_do_I_use_C/hello)
Hello, World!


* Basic rules:
	* All C standalone programs must have a function called "main()"
	* Keywords are always lowercase; you cannot use a keyword as an identifier
	* Statements must be terminated with a semicolon
	* Comments are delimited by [/*...*/](file:///*...*)
	* Single line comments are not ANSI C (//)
* Upcoming labs:
	* introduce the GNC toolchain
	* aspects of the C exe model


