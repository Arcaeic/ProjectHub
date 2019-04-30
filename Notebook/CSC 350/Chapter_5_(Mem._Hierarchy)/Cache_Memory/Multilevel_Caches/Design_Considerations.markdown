# Design Considerations
Created Saturday 16 March 2019

L1 Cache: Focus on **minimal hit time**
L2 cache: **focus on low miss rate to avoid main memory access**
Hit times at this level have less overall impact
Resulting in:
L1 cache usually smaller than a single cache
L1 block size smaller than L2 block size
L2 often has a higher degree of associativity
	
### Dynamic Multiple Issue
Also known as **superscalar processors**
CPU logic decides whether to issue 0, 1, 2 each cycle
Must do so while avoiding structural and data hazards
Major benefit: Eliminates the need for the compiler to be invovled with instruction scheduling
Such scheduling may still help
but corrct code semantics are ensured by the CPU


### Dynamic Pipeline Scheduling
Allow the CPU to execute instructions out of order to avoid stalls
**But CPU commits result to registers in order!**
Example:
![](./Design_Considerations/pasted_image.png)
Can start **sub **while **addu** is waiting for **lw**

### Dyanmically Scheduled CPU
![](./Design_Considerations/pasted_image001.png)

### Interactions with Advanced CPU
Out of order CPUs can execute instructions during cache miss
Pending store stays in load/store unit
Dependent instructions wait in reservation stations
Independent instructions continue
Effect of miss depends on program data flow
Much, much harder to analyse
Designers instead use system simulation (with load traces driving the simulation)

### Cache interactions with software
Cache misses can also depend upon memory-acces patterns
this is clearly influenced by algorithm behaviour
Example:
Quicksort at O(nlg(n))
Radix sort at O(dn)
Compiler omptimizations may be able to change emitted code to better match cache structures

### Software Optimization via Blocking
Goal: maximize accesses to **data placed in cache before that data is replaced**
Consider inner loops of Double-precision General Matrix Multiple (**DGEMM**)


