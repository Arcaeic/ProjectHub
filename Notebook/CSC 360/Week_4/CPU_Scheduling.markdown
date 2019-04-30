# CPU Scheduling
Created Tuesday 30 January 2018

![](./CPU_Scheduling/pasted_image.png)

### CPU scheduler
short-term scheduling
CPU scheduling
switch from running to waiting (blocked)
switch from urnning to ready
switch from waiting to ready
terminate (i.e, leave)
Preemptive vs none-preemptive

### CPU Dispatcher
(give control to the one selected by scheduler)

Procedures
context switching (save old, load new, etc)
mode switching (e.g., switch to user  mode)
start to execute from the newly loaded PC
Performance measure
dispatch latency/overhead
	
	
### Scheduling Criteria
"Whos next?"
CPU utilization: keep CPU as busy as possible
throughput: # of processes done per unit time
turnaround time: from start to finish
waiting time: time spent in ready state
response time: interactive, request-reply
Goal
max {...}, min {...}


### Scheduling Algorithms
First come, first serve (FCFS)
served by the order of arrival
Example
P1 (24), P2(3), P3(3)
schedule A:
P1, P2, P3
schedule B (if they arrive at the same time)
P2, P3, P1



