# Storage Hierarchy
Created Thursday 08 March 2018

![](./Storage_Hierarchy/pasted_image.png)

### Memory access
Access by address
for both code and data
Address binding
compiler time: absolute code
load time: relocatable code
execution time
![](./Storage_Hierarchy/pasted_image001.png)

### Memory Space
Logical memory
seen by CPU
virt mem
Physical memory
seen by memory unit
Address binding
Compile/load time: logical/physical addr same
execution time: logical/physical address differ
	
### Memory Management
MMU: memory management unit
logical/physical memory mapping
Reloaction register
Physical address = logical address + relocation base
Dynamic loading 
Dynamic linking

### Memory Protection
With base and limit registers
![](./Storage_Hierarchy/pasted_image002.png)

Without limit and reloaction registers
![](./Storage_Hierarchy/pasted_image003.png)

### Swapping
Swap out
e.g., low priority
Reduce the degree of multiprogramming
Swap in
address binding
Swapping overhead
on-demand
	

