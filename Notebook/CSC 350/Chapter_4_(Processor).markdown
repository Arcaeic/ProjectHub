# Chapter 4 (Processor)
Created Monday 04 February 2019
The Processor

### Recall
CPU performance factors
**Instruction Count**: determined by ISA, compiler
**CPI** and **Cycle Time**: determined by CPU hardware
Two MIPS implementations considered
**Simplified version **(i.e., one instruction performed in one cycle)
More relalistic **pipelined version** (i.e., one instruction requiring multiple cycles
To keep the discussion feasible, will examine a simple **ISA subset**
Memory references: *lw, sw*
Arithmetic logical: *add, sub, AND, OR, slt*
Control transfer: *beq, j*

### Instruction

1. Content of PC register determines location of instruction

** Instruction is fetched from instruction memory**

2. Register numbers are decoded from instruction

**Register file used to read register contents**

3. Depending on instruction calss, specific actions must occur

**Use ALU** to calc arithmetic result OR memory address for load/store OR branch target address
**Access data memory** to perform load/store action
**PC **is incremented by 4 OR set to a target-instruction address
![](./Chapter_4_(Processor)/pasted_image.png)

