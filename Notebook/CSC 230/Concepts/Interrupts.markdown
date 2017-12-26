# • Interrupts

* Created Thursday 02 February 2017



* Allows programs to respond immediately to external events.
* An event (a signal) that interrupts the flow of the current program
* Can be triggered by exteranl or internal signals



* The interrupt system:
	* A mechanism that suspends temporaily and lets CPU run a high-priority task
	* When complete, the original program continues
* ISR (Interrupt Service Routine)
* RETI - Return from interrupt



* When an interrupt occurs, the CPU:
	* Saves its current state
	* Jumps to a pre-defined location depending on the interrupt
	* completes routine
	* Goes back to where it was before the interrupt

	

