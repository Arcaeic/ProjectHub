# Instructions
Created Monday 04 February 2019


### Instruction Fetch
![](./Instructions/pasted_image.png)
file, program counter (32-bit), and combinatorial adder respectively
![](./Instructions/pasted_image001.png)
Program Counter: Either increments by 4 or reads address from instruction memory

### R-Format Instructions

1. These instructions cause two registers to be read (possibly the same register)
2. An arithmetical/logical operation is performed
3. Result is written to a register

Datapath elements needed:
**Register file (state)**
**Arithmetic/Logic Unit (combinatorial)**
![](./Instructions/pasted_image002.png)![](./Instructions/pasted_image003.png)

### Load/Store Instructions
**Read register operand** (either src or dst) from register file
Calculate **effective address using 16-bit offset**
(Use ALU, but sign-extend the offset!)
Load: **Read data memory** and update register
Store: **Write register's value **to data memory
Datapath elements needed:
**Data-memory unit (state)**
**Sign-extension unit (combinatorial)**
![](./Instructions/pasted_image004.png)

### Branch Instructions
Read register operands from register file
Compare value of operands
That is, use the ALU to **subtract them, check for Zero output**
Calculate target address
**Must sign-extend **the 16-bit displacement
**also shift the offset left by two places **(i.e., offset within instruction indicates word displacement!)
**Add to PC +4 if Zero output **(where PC+4 already calculated by instruction fetch portion of datapath), and overwrite PC register
	

