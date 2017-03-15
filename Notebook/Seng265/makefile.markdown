# makefile
Created Wednesday 23 November 2016

[+A simple compilation](./makefile/A_simple_compilation.markdown)

[+Implicit Compilation](./makefile/Implicit_Compilation.markdown)

[+Additional Features](./makefile/Additional_Features.markdown)


* Large software projects usually consist of dozens (or even hundreds) of files
* Files consist of:
	* source code
	* object code
	* interface descriptions
	* config information
	* automatically-generated documentation
* A software build:
	* Constructed executable version of the program
* How do we build software efficiently?
	* A small change to one part of the program should not require the reprocessing of every other file.



* make is an important utility
* It uses a makefile
	* Which describes dependencies
* A dependecy represents/encodes the relationship between files in a project
	* if file A uses the information in file B
	* and if file C does not use the information in file B
	* Then any change in B should result in only A being reprocessing.
	* and any change in file A should result in only C being reprocessed.

	

