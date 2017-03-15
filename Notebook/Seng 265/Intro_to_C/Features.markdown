# Features
Created Monday 26 September 2016


* A "general purpose" language
	* equally usable for applications programming and systems programming.
		* develop a protocol for a network
		* develop a dtb management system
		* Write a compiler for another language
* Its ubiquitous: where you find UNIX you usually find C
* it provides the basis for understanding other languages, notably C++



* Most C toolchains have a small footprint
	* Popular choice for developing **embedded systems**
	* OS research and development
	* Good choice for systems programs that one expects to port
* Compile-time features
	* ANSI-compliant compilers provide extensive compile-time diagnostics
	* ANSI-compliant compilers provide a continum of optimizations, from **none** to **conservative ** to **aggressive**
* Run-time features (i.e pluses)
	* Easy to adapt a C compiler's output (exe) to the exe environment on a platform:
		* Windows, Mac, Linux
* Run-time features missing (i.e, also could be considered efficency pluses)
	* no native array access bounds checking
	* No native null-pointer checks (use a custom library for this)
	* no native checks on uninitialized varibles (some scenarios can be checked at compile-time)


