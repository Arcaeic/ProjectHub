# Registers vs Main Mem
Created Thursday 17 January 2019

Registers are MUCH faster to access than main memory
Operating on memory data requires loads and stores
and therefore more instructions must be executed
Therefore:
Compiler must attempt to use registers for variables as much as possiblr
Only if a variable is less frequently used should it be **spilled** to memory (i.e. temporal locality)
**Register optimization is important!**
Constant data can be specified in some instructions
__addi $s3, s3, 4 __        (add immediate to $s3, using s3 and 4)
Interestingly, there is no immediate substract, though addi is capable of using two's complement and can take negative integers
__addi $s2, $s1, -1__

**Design Principle 3: Make the common sense fast**
Small constants are common
Immediate operand avoids a load instruction
	
MIPS register 0 ($zero) is the constant 0
It cannot be overwritten
Useful for some common operations
e.g. moving b/w integers	__add $t2, $s1, $zero__


### Unsigned Binary integers
Range: 0 to +2^n^-1
[0, 4294967295] for 32-bit integers


### Two's Complement (2s-Complement)
Range: -2^n-1^ to +2^n-1^-1
Using 32 bits: [-2147483648, 2147483647] due to the MSB being reserved as a sign flag
1 = Negative, 0 = Positive
	

### Converting to two's complement
We can use a modification of the one's complement conversio nhelp us convert a number to two's complement
This is done using the **change-sign rule**
If the **original number is positive**, **find the rightmost bit set to 1**, and then **flip all bits to the left of that bit.**
	
![](./Registers_vs_Main_Mem/pasted_image.png)
	





