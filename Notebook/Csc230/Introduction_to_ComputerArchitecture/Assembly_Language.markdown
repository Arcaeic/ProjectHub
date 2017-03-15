# Assembly Language
Created Thursday 12 January 2017

We will use **Arduino Mega2560 board**

* This board uses AVR ATmega2560 processor
* Flash memory 256KB (8KB used by bootloader)
* SRAM 8KB
* EEPROM 4KB
* Clock Speed 16MHz
* 54 Digital I/O Pins
* 16 Analog Pins


Harvard Architecture
RISC Microcontroller

### AVR General Features

* Enhanced RISC architecture
* Two-stage instruction pipeline
* Majority of instructions take one clock cycle
* Wide variety of on-chip periphs.
* Internal program and data mem.

	

* Most instrucs are 2 bytes (16bits) in length
* AVR has 32 general purpose registers R0-R31
* Data that is being manipulated **must **be in the registers
* PC always holds the address of next instruct to be fetched, a word addresss of 15/16/17 bit value dpending upon size of memory.

	

Commands
--------

* Rd is destination register and Rr is source register.


### • Move

* mov Rd, Rr copies whatever is in Rr and puts it in Rd. 
		* Retains original data.
		* Similar to copy


### • Add

* add R2, R1 will add R1 + R2 -> 


### • Load Immediate

* ldi Rd, K 
	* Loads an 8 bit constant directly to register 16 to 31.
	* Rd 	<- K 16<=d<=31, 0<=K<=255


### • Store

* Copies the content of a register into a location in memory (more permanent storage)
	* st X, Rr		Rr -> X



Decoding
========

![](./Assembly_Language/pasted_image.png)


