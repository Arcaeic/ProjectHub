# Structure
Created Tuesday 03 April 2018

### I/O Access
Port Number
Interrupt #
DMA
Program Counter (PC)
	
Polling: Busy, Ready, Error
"Busy-wait"
	
Interrupt: Event-driven
Asynchronous operation
Interrupt 

DMA: Bulk transfer
High-speed I/O
DMA Controller
	
The kernel has an attached I/O subsystem to interact with the device drivers.

Kernel	-> Kernel I/O Subsystem
  Kernel I/O Subsystem -> Device Drivers
Device Drivers -> Device Controller
Device Controller -> Device

### Characteristics
(Aspect : Variation : Example)
**Data-Transfer Mode** : character block : terminal, disk
**Access method**: Sequential/Random : modern CD-ROM
**Transfer Sched**: Sync/Async : Tape, Keyboard
**Sharing** : Dedicated/Sharable : Tape, Keyboard
**Device Speed** : Latency/seek time/transfer rate/op delay
**I/O Direction**: Read/Write/RDWR : CD-ROM, Gfx Controller, Disk


### Common Devices
**Block Device**
e.g., Disk drive
Random access: Random, write, seek
**Character Device**:
e.g., Keyboard, serial port
Sequential Access: getc, putc
**Network Device**
e.g., Socket
	

