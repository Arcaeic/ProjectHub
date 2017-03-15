# • Compiler Structure

* Created Wednesday 08 March 2017



* A processor understands its own machine language
* A compiler is typically a large piece of software to translate a high level language to machine language
* ![](./Compiler_Structure/pasted_image.png)
* **The front end is dependant on:**
	* The high level language
	* The grammar
	* The front end should not need to know anything about the properties of the target architecture
* **The back end is dependant on the target architecture**
	* It knows the intermediate code
	* It knows the target's machine code
	* It does not know the original language and grammar
* **Structure:**
	1. Lexical Analysis (scanning)
	2. Syntatic Analysis (parsing)
	3. Semantic Analysis (checking and attaching meaning)
	4. Optimization
	5. Code Generation 


