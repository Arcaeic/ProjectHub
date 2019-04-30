# OS
Created Friday 05 January 2018

### OS: Historical view

* Requirements evolve



* Was
	* comps were more expensive than users
	* goals: make computers more efficiently used
	* results: share compuers
* Now
	* users become mroe "expensive" than comps
	* goals: make comps more effectively used
	* results: share users (among other comps)

	

### OS: Generations

* Uniprogramming
	* "One program at a time"
	* start, execute, {wait, execute}*, finish
	* wait for I/O, other programs
	* CPU may be idle some of the time
* Multiprogramming
	* "Many programs at a time"
	* try to always keep CPU busy
	* handles many programs concurrently
	* "share" CPU


### Batch processing

* Load a pool of jobs
* Execute jobs one at a time until it is blocked
* Pick another job


### Time sharing

* Execute one job up to a certain time
	* e.g hardware timer w/ counter
* Switch to anothet to execute
	* job scheduling, memory swapping
* Seem to execute many jobs at the same time



* **Batch processing vs Time sharing**
	* job resposniveness
	* switching overhead

{

### OS: Operations

interrupt the current job
yield: system call trap (e.g I/O)
yank: hardware timer interrupt
Dual-mode operation
user mode for regular applications
kernel mode with privileged instr
trap: user to kernel entry
	

#### Process management
process: A running program
vs. thread
Create, delete, suspend, resume process
resource allocation: CPU, mem, I/O, etc
Schedule processes/threads
Synchronize processes
Communicate b/w processes
Handle deadlocks


#### Memory management
Main memory
Store instructions for exxecution
store data for processing
Keep track available mem
Allocate and reclaim memory
provide protected access
trap 
	

#### Storage and I/O management
"In Unix, everything is a file"
a logitcal interface: r,w,x
Create and delete files/directories

}

