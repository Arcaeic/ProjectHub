# MIPS
Created Monday 14 January 2019


* Add and subtract have **three operands**
	* Two sources
	* One destination
* All arithmetic operations have this form
	* __add a, b, c	#	a is assigned b + c__
* **Design Principle 1: Simplicity Favours Regularity**
	* Regularity makes implementations simpler
	* Simplicity enables higher performance at lower cost



* Arithmetic instructions use **register operands**
	* This means they do not use memory operands
	* However, some immediates are possible with arithmetic
* MIPS has a 32 by 32-bit register file
* Assembler names: These are alternative names for registers
	* $t0, $t1, ... ,  $t9: temporary values
	* $s0, $s1, ... , $s9: "save" variables
	* etc.



* **Design Principle 2: Smaller is faster**
	* Register file is small
	* Main memory is alrge (and used for other quantities)

![](./MIPS/pasted_image.png)


* Many memory is used for composite data
	* Arrays, structures, dynamic data
* To apply arithmetic operations on any data...
	* Load values from memory into registers
	* Perform arithmetic
	* Store result from register into memory
* Memory is byte-addressed
	* Therefore each address identifies an eight-bit byte
	* but words are aligned in memory
	* So that addresses in practice must be a multiple of 4
* MIPS is big-endian
	* Most significant byte is at the earliest address of a word
	* Whereas little-endian pputs least-significant bytes first
* A **word** stored in consecutive memory, starting at addr: 0xCAFEBABE

![](./MIPS/pasted_image001.png)

### Sign Extension
MIPS instructions can involve a mix of 8-bit, 16-bit, and 32-bit numbers
Regardless of what is used, numeric value must be preserved
Some instruction automatically perform sign extension by replicatoin sign bit to the left
__addi: extend immediate value__
__lb, lh: extended loaded byte / halfword__
Example: 8-bit to 16-bit:
__+2:__	**0**__000 0010__	__0000 0000 0000 0010__
__-2:__	**1**__111 1110__	__1111 1111 1111 1110__
Some instructionss either ignore sign extension or **suppress** it
An **unsigned load** simply fills the left of value with zeros
Reasoning here is that **the bit sequence** is needed rather than **the numeric value**
	
### Representing instructions
Instructions must be encoded
and by definitions this must be a **binary encoding**
This encoding is what we have already called machine code
MIPS instructions
Encoded as a 32-bit instruction words
There exist a small number of formats for encoding operation code (opcode), register numbers, etc.
**Remember design principle 1: Simplicity over Regularity**


### Register Numbers
[+R-format](./MIPS/R-format.markdown)


