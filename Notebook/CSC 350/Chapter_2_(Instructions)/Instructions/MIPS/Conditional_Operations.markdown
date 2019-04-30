# Conditional Operations
Created Monday 21 January 2019

Branch (i.e., move control flow to a specific instruction address) if a condition is true
Instruction indicates the operations
__beq rs, rt L1__
if rs == rt, branch to instruction labeled L1
__bne rs, rt, L1__
if rs != rt branch to instruction labled L1
__j L1__
unconditional jump to instruction labeled L1

![](./Conditional_Operations/pasted_image.png)

MIPS Code
__bne $s3, $s4, ELSE__
__add $s0, $s1, $s2__
__j EXIT__
__ELSE: sub $s0, $s1, $s2__
__EXIT: ...__
	


### Compiling Loop Statements
![](./Conditional_Operations/pasted_image001.png)
__LOOP: sll $t1, $s3, 2		__temp = i * 4 (due to alignment)
 __add $t1, $t1, $s6	__temp = (i * 4) + address of save (to get save[i]&)
 __lw $t0, 0($t1)	__load $t1*  into $t0
 __bne $t0, $s5, EXIT__
 __addi, $s3, $s3, 1__
 __j LOOP__
__EXIT: ...__

### More Conditionals
**Set **result to 1 if condition is true, **set** result to 0 otherwise
__slt rd, rs, rt__
if rs < rt, then rd = 1 else rd = 0
__slti rd, rs, C__
if rd < c, then rd = 1 else rt = 0
Use in combination with beq, bne
__slt $t0, $s1, $s2__		if ($s1 < $s2)
__beq $t0, $zero, L__	then branch to L...
	
These variants also exist:
__slt, slti __ (signed)
__sltu, sltui__ (unsigned)
	
Why the split b/w branch conditions and set instructions?
That is, why not blt, bge, etc. as exists in other ISAs (such as Microtel AVR?)
Hardware for <, <=, >=, etc is slower than =, ≠
Combining conditions with branch logic involved more work per instruction
Which therefore requires a slower clock
Which therefore penalizes all instructions
beq and bne are the common case
Therefore the split between these instructions and the "set" variants is a good compromise
	

