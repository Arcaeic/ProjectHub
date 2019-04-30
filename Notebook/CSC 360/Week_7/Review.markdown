# Review
Created Thursday 08 March 2018

Multithreaded Programming
=========================

A concurrent system is a system that run multiple tasks by allowing each to make progress one at a time.

A parallel system is able to run more than one process simultaneously.

### Benefits of multithreaded programming
Responsiveness
Especially in UI
Resource sharing

Economy
Context switching threads is quicker than managing individual processes.
This is because threads exist within a process's memory space
Scalability

### Multithreading Models
**User Thread: **Managed without kernel support, and supported above the kernel
**Kernel Thread:** Supported and managed directly by OS

Many to one: Thread management is done by a library so its efficient, but if the kernel thread gets blocked its all blocked

One to one: If one kernel thread gets blocked the rest dont.
the amount of threads available in a system varies, and this could eventually overburden the system with kernel threads

Many to Many: Has neither of these shortcomings. If a thread blocks, the kernel can create another kernel thread and keep going. Also only creates kernel threads as required so is less strenuous on the system.


CPU Scheduling
==============

**Uniprogramming: **A singular/linear path for a program to follow
**Multiprogramming: **Encapulates Multitasking and Multithreading
**Multitasking (Multiprocessing): **Assigning processes to different CPU cores in order to distribute work and increase program efficiency
**Multithreading**: A program that consists of one main process and one or more threads or processes.
	
How to handle different processes/threads?
States: Ready, running, blocked
Scheduling (PCB, TCB)
![](./Review/pasted_image.png)
Ready -> running: Scheduler dispatch, 
Running -> Ready  Interrupt
Running -> Waiting: I/O or event wait (blocking)
Waiting -> Ready: I/O or event complete

### Process Control Blocks (PCB)
![](./Review/pasted_image001.png)

State: Ready, running, etc.
CPU:
PC, registers, priority, etc
Memory:
Memory control information
I/O:
e.g., List of opened file

![](./Review/pasted_image002.png)

### CPU I/O Burst Cycle
- There are two components of CPU process execution, an "I/O waiting" burst and a CPU burst.

### CPU Scheduler
(Short Term scheduling)
Criteria: CPU, throughput, delay, etc
goal: max throughput. min delay


### CPU Scheduling Algorithms
**Definitions:**
**Preemptive: **The scheduler can interrupt a currently executing process in order to change the job.
**Non-preemptive:** The scheduler must complete the process it choses before choosing another one
**FCFS:** Simply executes processes in the order they are recieved with ignorance towards job duration
**SJF (Shortest Time First):** Is based on the length of next CPU burst
**Preemptive (Shortest Remaining Time First)**: The job with the smallest burst length is always scheduled first
**Non-preemptive**: Will complete a job once chosen
Is optimal in avg waiting time
Reduce the total waiting time for all jobs
Why is it optimal?
Reducing the amount of jobs at any given time will reduce the total average time.
Problems: No priority
The next shortest job may be hard to determine. Requires working with moving averages, working with assumptions, exponentially weighted moving averages
**Priority Scheduling: **
The job with highest priority scheduled first
SJF: Shortest CPU burst, higher priority
FCFS: Arrival earlier, higher priority
Static priority: Starvation
e.g., SJF
Dynamic priority: Aging
**Round-robin Scheduling**
Discrete processor sharing
CPU time quantum
usually 10ms - 100ms
for a process
Either yield after a CPU burst
or be preempted after using up a time quantum
A fifo queue
All ready processes


### Scheduling Criteria
"Who's next?"
CPU Utilization: Keep CPU as busy as possible
throughput #: # of processes done per unit time
turnaround time: from start to finish for all completed processes
waiting time: time spent in ready state in FIFO
response time: interactive, request-reply

### Multi-queue scheduling
the ready queue can be broken down into multiple queues, prioritizing background (system) tasks over foreground (user tasks). In addition, each queue can be assigned its own type of scheduling algorithm.
For example, Round robin is implemented often in the foreground due to process fairness, while FIFO or SJF are often used to deal with system calls or processes.
The CPU use can also be prioritized among the queues. For example, 80% can be given to the foreground queues, while 20% are on reserve for background tasks


### Algorithm Evaluation
Little's Law:
n = λ * W
where:
n = The queue length
λ = The average number of process enterring the queue per second (e.g. 3 processes per second)
W = Avg waiting time in a queue


Sync
====
Synchronization is needed to handle process communication as well as memory management
Done using smart CPU scheduling

### Producer/Consumer Problem
Solutions so far:
bounded buffer
In/Out variables; full, empty conditions
N buffer space, N-1 utilized at most
FIFO queue
A simpler solution
Use a counter variable to fuller utilize the circular buffer
in/out variables control the circular buffer and where to store next
In = (in+1) % BUFFER_SIZE
count determines the how many slots in the buffer are full
The queue is FIFO,  so In/Out should work

![](./Review/pasted_image003.png)

### Critical Section
Doing these types of tasks concurrently (count++, count--) can cause non-deterministic results, or race conditions
Code section accessing shared data
Only one thread executing in critical section
Choose the right size of critical section
Use mutual exclusion to lock, wait, unlock, etc.
Mutual Exclusion: No more than one thread in critical section
Making Progress: If no process in critical section, one can go in
Bounded waiting: Processes attempting to access critical sectoin have bounded wait time, that is, it isn't unknown

[+Critical Section Solutions](./Review/Critical_Section_Solutions.markdown)

### Semaphores
An integer value that can only be accessed by two atomic functions, wait() and signal()
wait(S) {
if(S <= 0) {
//wait
}
S--;
}
		
signal(S) {
S++;
}
Another way to prioritize actions is by inserting the process into a FIFO queue
	
wait(S) {
S.value--;
if(S < 0) {
S.list.add(Process)
//block
}
}
	
signal(S) {
S.value++
if (S < 0) {
P = S.list.pop()
//wakeup Process (P)
}
}
	
### Dining Philosophers problem:
5 philosophers
Each philospopher:
Semaphore chopstick[5];
do {
//think
//hungry
wait(chopstick[i])
wait(chopstick[(i+1) % 5)
//eat
signal(chopstick[i])
signal(chopstick[(i+1) % 5)
//Think
}
while (1)
Avoiding deadlocks/starvation:
Have 4 out of the 5 people at the table at all times
Asymmetrical solution
Even numbered philosopher picks up left chopstick , then right
Odd numbered philospher picks up right chopstick, then left
Have philosophers only pick up chopsticks during critical section


### Monitors
An ADT consisting of multiple functions.
This forces one functoin wihtin the monitor to be running at any given time
Local variables can only communicate amongst each other due to scope
	
Monitor {
f1 {...}
f2 {...}
...
fn {...}
initialization code(...) {...}
}
	
![](./Review/pasted_image004.png)
	
### Monitors w/ CV
No busy-waiting
![](./Review/pasted_image005.png)
	
monitor {
//If my neighbours are not eating and im hungry, then set myself to eat and signal to pickup
void test(int i) {
if (state[(i+1) %5] != EATING && state[(i+4) % 5] != EATING && state[i] == HUNGRY) {
state[i] = EATING;
self[i].signal()
}

Deadlocks
=========
Deadlocks can occur if all are true:
Mutual exclusion
wait(chopstick[i])
Hold-and-wait
wait(chopstick[i]), wait(chopstick[(i+1)%5]
No preemption
wait()
Circular wait
chopstick[(i+1)%5]
			
### Preventing Deadlocks
Mutual exlusion
Only when it is absolutely necessary
Hold-and-wait
all or none
Non-preemptipon
Give up on request
Circular wait
Strictly ordered
		
### Avoiding deadlocks
Avoidance
Decalre maximal resource usage ina dvanec
Claim edge
Check against currently admitted processes
admit if safe (e.g., no circular wait)
![](./Review/pasted_image006.png)

### Banker's Algorithm
n = # of processes
m = # of resource types
**Available: ** Vector of length m. if available [j] = k, there are k instances of resource type R~j~ available
**Max:** n x m matrix. If Max[i,j] = k, then process P~i~ may request at most k instances of resource type R~j~
**Allocation:** n x m matrix. If Allocation[i,j] = k then P~i~ is currently allocated k instanctes of R~j~
**Need:** n x m matrix. If Need[i,j] = k, then P~j~ may need k more instances of R~j~ to complete the task
Need[i,j] = Max[i,j] - Allocation[i,j]
		
### Safety Algorithm
(Basically check safety by completing tasks and freeing up allocated resources to solve other tasks. If you can solve all the tasks, it is safe.)
Let **Work** and **finish ** be vectors of length m and n respectively.

1. Work = Available

 Finish[i] = false for i = 0, ..., n

2. Find an i such that both
	a. Finish[i] = false
	b. Need~i~ <= Work

If no such i exists, go to 4

3. Work = Work + Allocation

  Finish [i] = true

4. If Finish[i] == True for all i, then the system is safe


### Resource-request algorithm
Request = request vector for Process P
If Request~i~[j] = k, then process P~i~ wants k instances of resource type R~j~

1. If Request~i~ <= Need~i~, go to step 2. Raise error otherwise
2. If Request <= Available, go to step 3. Otherwise wait since resources are not available
3. Allocated resources to P~i~
	a. Available = Available - Request
	b. Allocation = Allocation~i~ + Request~iB~
	c. Need = Need~i~ - Request~i~ 


