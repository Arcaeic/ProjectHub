# Chapter 6 (Parallelism)
Created Monday 18 March 2019

Goal of **Parallelism:** connecting **multiple computers** to get **higher performance**
Multiprocessors
Scalability, availability, power efficieny
Variant 1: **Task level** (process-level) parallelism
High throughput for independant jobs
Variant 2: **Parallel processing** program
Single progam run on multiple processors
Variant 3: **Multicore** Microprocessors
Chips with multiple processors (cores)

Hardware
**Serial: **e.g., Pentium 4
**Parallel:** e.g., quad-core Xeon e5345
Software
**Sequential:** e.g., Matrix Multiplication
**Concurrent: **e.g., Operating System
**Sequential/Concurrent **software can run on **serial/parallel** hardware
Big challenge: **make effective use of parallel hardware**

### Some building blocks
Parallelism and advanced instrucvtion-level parallelism
Pipelining
Multiple instruction issue: either static (i.e., VLIW) or dynamic (i.e., superscalar)
Parallelism and instructions
ISA support for **Synchronization**
Instructions such as test-and-set, swap
Parallelism and computer arithmetic
**Subword parallelism in extensions such as SSE**
Parallelism and Memory hierarchies
**Cache coherence**
	

