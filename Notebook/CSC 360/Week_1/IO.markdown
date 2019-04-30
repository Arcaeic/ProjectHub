# IO
Created Friday 05 January 2018


* A large variety of i/o devices
	* keyboard/mouse, video, audio, network, etc
* Access
	* Address
		* port numbers
		* I/O vs mem space
	* Interrupt
	* Direct memory access (**DMA**)
* Synchronous vs Asynchronous
	* Synchronous: One interrupt handler, cant do things simultaneously
	* Asynchronous: Processes are run concurrently, things don't wait on eachother


### Interrupts

* Asynchronous operation
* Nonmaskable interrupts
	* e.g., hardware fault
* Hardware interrupts
	* hardware events: e.g i/O completion
	* interrupt controller: priority & arbitration
		* The microcontroller/CPU needs to determine which process is more important
* Software interrupts
	* trap, system call


#### Interrupt Handling

* Save current state
	* CPU counters, registers, flags at system stack
* Update program counter
	* interrupt controller; interrupt vectors
* Execute interrupt routine
* Restore previous state
* Multiple interrupts
	* priority, masking, reentry



* Interrupts take up time of the CPU
* they work by asking for the use of the CPU by sending the interrupt to which the CPU responds
	* Note: In order to save time the CPU does not check if it has to respond
* Interrupts are used when a task has to be performed immediately


#### DMA
Works concurrent to the CPU
Therefore freeing up resources
Can move larger files than what can be stored in a CPU
![](./IO/pasted_image.png)
High-speed I/O, bulk data transfer
DMA controller
source/destination addr
counter: the amount of data to be moved
DMA handling
program DMA controller
execute DMA concurrently
issue an interrupt on DMA completion
	

