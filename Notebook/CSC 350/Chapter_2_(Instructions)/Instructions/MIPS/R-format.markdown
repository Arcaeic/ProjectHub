# R-format
Created Monday 21 January 2019

Although MIPS conventions deidcate registers to specific purposes (temps, save registers, etc)
these are mapped to specific registers in the 32-bit register file
This mapping is shown on the MIPS reference data page
![](./R-format/pasted_image002.png)
![](./R-format/pasted_image003.png)
Instruction fields:
op: operation code (op code)
rs: first source register number
rt: second source register number
rd: destination register number
shamt: shift amount (0 for now)
funct: function code (extends opcode)
![](./R-format/pasted_image005.png)


