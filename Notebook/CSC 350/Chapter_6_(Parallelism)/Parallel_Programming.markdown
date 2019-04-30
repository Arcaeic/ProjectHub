# Parallel Programming
Created Monday 18 March 2019

Parallel **Software** is the problem!
Coding effort is required in order to get significant performance improvement with parallelism
Otherwise, just use a faster uniprocessor, since its easier!
Difficulties:
Partitioning (of data amongst processors)
Coordination (of code among processors)
Communications overhead (to acheive partitioning and coordination)


### Amdahl's Law
Assuming a program consists of clearly **sequential** and **parallelizable** (albiet still sequential) portions of code..
it is possible that the **sequential code can itself limit speedup** resulting from the parallelization
Example: Assuming **100 processors** are available, and a **90x speedup is desired**
What is the **consequence for the sequential part** of the code?



