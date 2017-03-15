# • Computer Logic

* Created Monday 09 January 2017



* A byte can represent many things. It coujld a binary numeal or just a bunch of bits in a container each with a different meaning.
* Processors commonly process a single bit or a collection of bits each individually.
* The fundamental Boolean operations are NOT, AND, and OR
* A few others are NAND, NOR, and XOR


### • An example of when to use XOR:

* Masking things:
	* Use a mask to clear the upper bute of a 2-byte word
	* Use a mask to set the upper byte of a 2-byte word
	* What about setting the middle 2 bits of a byte?
	* If we wanted to extract information from a byte, we can use an AND mask to see if that byte returns 0 (if 1 & 0 == 1) or nonzero (if 1 & 1 == 1)
	* XOR is also useful for toggling


### Execution Cycle: CPUs
**All CPUS follow Fetch - Decode - Execute - Store sequence**
	
**Fetch - Decode - Execute:**

1. Fetch instruction from memory
2. Update current pointer to instructions memory address
3. Decode instruction
4. Fetch further data, if required
5. Execute instruction,
6. Go to step 1

**Registers:**
PC	Program Counter
IR	Instruction register or Instruction decode reg.
MAR	Memory Address Register
MDR	Memory Data Register
AC	


