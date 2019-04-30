# More Scheduling
Created Saturday 10 February 2018

### Multi-queue scheduling

"No one fits all"
Multi-queue approach
foreground queue
e.g., RR; better fairness
background queue
e.g., FCFS; more efficient
Inter-queue scheduling
Priority, time sharing (e.g., 80% rule)
	
#### Feedback
Multiqueue
number of queue
queueing algorithm for each queue
Multi-queue with feedback
promote jobs
demote jobs
example
![](./More_Scheduling/pasted_image.png)

### Multi-processor queueing
Load balance b/w processors
cooperation and communication
Assymetric multiprocessing
one master scheduler
Symmetric multiprocessing
cooperative schedulers
processor affinity: try to stick with one
load balancing: push or poll migration
Thread scheduling
local: user -> kernel thread
e.g., within a process
global: kernel thread -> CPU
e.g., across the system
Algorithm evaluation
queuing analysis
Little'ls law: n = \lamda *w
		
### Linux Scheduling
Scheduler
O(n): gloal run queue (Linux 2.4)
n: number of processes
O(1): double linked list (Linux 2.6)
active/expired set
140 priority levels
realtime (0-99), user (100-139, nice/quantum sizes)
Interactive vs CPU-bound: interactivity

### pthread scheduling
pthread_attr_setschedpolicy();
regular, non-realtime (nice-able)
realtime, round-robin (preemptive, privileged)
realtime, FCFS (non-preemptive, privileged)
pthread_attr_setschedparam();
pthread_attr_setscope()
scheduling within a process
scheduling for the entire system
	
	

