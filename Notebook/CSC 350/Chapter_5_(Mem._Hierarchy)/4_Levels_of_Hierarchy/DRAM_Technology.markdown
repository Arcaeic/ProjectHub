# DRAM Technology
Created Monday 11 March 2019

### DRAM Technology
DRAM requires far fewer transistors than SRAM
Therefore less expensive and takes up less space
Data is stored as a charge in a capacitor
Single transistor is used to access this charge
However...
This cahrge must be periodically refreshed
In essense: read contents..., then write them back again
In practice: performed on a DRAM row at one time


### DRAM Access
Bits in a DRAM are organized as a rectangular array
DRAM access an entire row at once
**Burst mode:** Supply successive words from a row (doing so with reduced latency per word)
**Double data rate** (DDR) DRAM: Transfer on **rising and falling** clock edges
**Quad data rate **(QDR) DRAM: Separate DDR I/Os
![](./DRAM_Technology/pasted_image.png)

### Factors affecting DRAM performance
**Row Buffer**
Allows several words to be read/refreshed in parallel
**Synchronous DRAM**
Allows consecutive accesses in bursts **without needing to send each address**
Improves bandwidth by reducing latency per memory access
**DRAM Banking**
Enables simultaneous access to multiple DRAMs (i.e., **access in parallel)**
Improves bandwidth
![](./DRAM_Technology/pasted_image001.png)



