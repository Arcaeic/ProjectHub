# Control
Created Monday 04 February 2019

### Control
with the datapath now given, we need to specify how to control it
That is, given an instrucvtion, indicate what values the various control lines need for the instruction
...such that the datapath is properly configured for that instruction
There are roughly two parts to this:
ALU control (easier)
Main control (tougher)

### ALU Control
ALU is provided to us (textbook appendix B)
We use the ALU in the datapath of all operations
Load/Store: function = add
branch: function = subtract
R-Type: function = what the funct field indicates in the opcode
![](./Control/pasted_image.png)
Let us assume there is a two-bit ALUOp derived from MIPS opcode
From this, we will have combinational logic to implement ALU control
![](./Control/pasted_image001.png)

### Main Control
Note that some control signals can be derived directly from the 32-bit opcode!
This is one of the reasons for keeping opcode formats simple and few in number
![](./Control/pasted_image002.png)
![](./Control/pasted_image003.png)



