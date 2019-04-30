# Logical operators
Created Monday 21 January 2019

Instruction for bitwise manipulation:
![](./Logical_operators/pasted_image.png)
__Where is bitwise NOT???__
A *not *pseudoinstruction exists, where we take advantage of the $0 register and the nor operator.
nor d, s, t
d = destination
s = source
t = second source
ex: *nor $s1, $t2, $0	*will perform not on $t2.


### Shift Operations
![](./Logical_operators/pasted_image001.png)
Recall:
shamt: shift amount (0 for now)
funct: function code (extends opcode)
**shift left logical**
shift bits left...
and fill incoming right bits with zeros
sll by i bits multiples by 2^i
**shift right logical**
shift right and fill with 0 bits
srl by i bits divides by 2^i (unsigned only)
	

