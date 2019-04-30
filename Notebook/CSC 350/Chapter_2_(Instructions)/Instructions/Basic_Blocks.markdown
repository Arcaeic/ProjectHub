# Basic Blocks
Created Monday 21 January 2019

You will have seen flowcharts in other courses
Visual representation of control flow
Shape of symbol indicates purpose
**Basic Block** diagrams represent structures used in compilers
**Basic block: sequence** of instructions with only **one entry point** (top) and only **one exit point** (bottom) and therefore **no branch targets within the block**
**Basic block diagram** consists of blocks control flow (but not edge labels!)
	
We as programmers do not normally find the basic blocks
Compilers are designed to analyze source code, detecting control flow
and from that building the basic-block diagram as part of the code generation step
Compilers can perform important code optimizations within basic blocks
Advanced processors can even find basic locks on the fly and apply on-chip optimizations
Basic block diagrams are covered more in CSC 435 (Compiler Construction)


