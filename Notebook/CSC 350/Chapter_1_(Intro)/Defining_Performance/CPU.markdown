# CPU
Created Friday 11 January 2019

### Recall: CPU Clocking
Nearly all digital hardware is governed by a constant-rate clock (i.e., synchronous)
![](./CPU/pasted_image.png)
Clock period: Duration of a clock cycle
Clock frequency (or rate): Cycles per second

### CPU Time
__CPU Time = CPU Clock Cycles * Clock Cycle Time__
__     = CPU Clock Cycles / Clock Rate__

Therefore, to improve performance, we can:
**Reduce** the number of **clock cycles **needed for task
**Increase **the **clock rate **of the device
**A hardware designer must often trade off the clock rate against cycle count**
nearly impossible to reduce clock cycles and increase clock rate simultaneously, hence importance of compilers

__Clock Cycles = Instruction Count * Cycles per Instruction__
Another way to think about execution time:
Equals the **number of instruction executed**...
...multipled by the **average time (cycles) per instruction**
**Instruction Count:**
Determined by the program, the ISA, and the compiler that generated machine code for the program
Cycler per instruction (CPI)
Determined by the CPU hardware
Note: In the real word, different instructions have different CPI values
Therefore, average CPI may be affected by the **instruction mix**


### Classic CPU Performance Equation
Basic performance can now be expressed in an equation involving the instruction count:
__CPU Time = Instruction Count x CPI x Clock Cycle Time__
And because the **clock rate is simply the inverse of clock cycle time:**
__CPU Time = (Instruction Count X CPI) / Clock Rate__
	

### More Granular CPI Information
We can denote an **instruction class** as denoting a set of ISA instructions where eveyr member of a set requires the same number of cycles to execute:
__Clock Cycles = [Sum of i=1 to n] (CPI__~i~__ * Instruction Count__~i~__)__
As a corollary, we can calculate the **weighted average CPI**:
__CPI = Clock Cycles / Instruction Count __
__= [sum of i=1 to n] (CPI__~i~__ * Instruction Count ) / Instruction Count__


Summary:
========
**CPU Execution time for a program**: Seconds for the program
**Instruction count**: # instructions executed for program
**Clock cycles per instruction (CPI)**: Average number of clock cycles per instruction
**Clock cycle time**: Seconds per clock cycle

