# • Stack

* Created Monday 23 January 2017



* Stack Pointer: Points to the next available space in the stack
* Push: Places data at Stack Pointer (SP), increments SP.
	* Push Rr	;Pushes byte in Rr to address at SP, inc SP.
* Pop: Decrements SP and performs AND SP, 0x00
	* Pop   Rr	; Dec SP, pops value from stack but Rr <- ld(SP)



* To set up the stack: Set the stack pointer to an address that we will most likely not modify.



* .def RAMEND = 0x21FF



* ldi r16, low(RAMEND)
* out SPL, r16
* ldi r16, high(RAMEND)
* out SPH, r16



