# Process Sync
Created Saturday 10 February 2018

Multi-programming
multi-process
process communication
shared memory or message passing
multi-thread

CPU scheduling
Cooperating processes/threads
e.g., the "producer-consumer" problem
cannot consume the thigns not produced yet


### Producer-Consumer problem
Solutions so far
bounded buffer
in, out variables; full, empty conditions
N buffer space, N-1 utulized at most
FIFO queue
FIFO variable
A simpler solution
to fully utilize the circular buffer
use a "counter" variable
	
##### The "counter" variable

![](./Process_Sync/pasted_image.png)

### Race condition
E.g., increment a counter (shared variable)
read the counter (from memory)
increment by at (at CPU)
write the counter
How about two threads?
sharing only one counter
non-deterministic result: R~1~W~1~R~2~W~2~, R~1~R~2~W~1~W~2~
"There is something not to be (always) shared


### Critical section
code section accessing shared data
only one thread executing in critical section
only one thread accessing the shared data: serialize
choose the right (size of) critical section!
Approach: exclusion (lock)
If locked, wait!
If not locked, lock & access (and later unlock)
	
#### Properties of "solutions"
Mutual exclusion
No more than one process in the critical section
Making process
If no process in the critical section, one can in
Bounded waiting
for processes that want to get in the critical section, their waiting time is bounded
	
##### Problem formulation
Only two processes, P~0~ and P~1~
General structure of process P~i~ (other process P~j~)
![](./Process_Sync/pasted_image001.png)
processes may share some common variables to sync their actions
do not get into loop!

**Algorithm 1:**
Shared variables:
int turn; // initially turn = 0
turn == i: P~i~ can enter its critical section
		
Process P~i~
![](./Process_Sync/pasted_image002.png)
		
**Algorithm 2:**
Shared variables
boolean flag[2]
initially flag[0] = flag[1] = false
flag[i] = true	(P~i~ is ready to enter its critical section)
Process P~i~
![](./Process_Sync/pasted_image003.png)
		
**Dekker's solution**
Combined shared varaibles of Algorithms 1 and 2
Process P~i~
![](./Process_Sync/pasted_image004.png)

Meet all three requirements; solve the critical-section problem for two processes

**Peterson's solution**
A simpler solution
combined shared variables of Algorithms 1 and 2
Process P~i~
![](./Process_Sync/pasted_image005.png)
	

