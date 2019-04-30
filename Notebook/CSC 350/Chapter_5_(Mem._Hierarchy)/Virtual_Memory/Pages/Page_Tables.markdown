# Page Tables
Created Saturday 16 March 2019

Store **page-to-frame placement information**
Array of **page table entries (**PTE) indexed by virtual page number
CPU's page table register points to page table in physical memory
If page **is present **in memory
PTE stores the physical page number
Plus other status bits (referenced, dirty)
If page **is not present**
PTE can refer to location in swap space on disk
A seemingly fata flaw with this scheme:
Every memory access requires an additional memory access (One for PTE, one for r/w)
Appears as if the scheme would be so slow as to invite ridicule!
	
![](./Page_Tables/pasted_image.png)

Because the cost of a page fault is so high...
There is a benefit to using more complex schemes to reduce page-fault rates
One approach to reduce page fault rate: **least-recently used **(LRU) replacement
Approximate this with a **reference bit** (aka **use bit**) in PTE set to 1 on page access
Periodically cleared to 0
A page with reference bit = 0 has not been used recently
Disk writes take millions of cycles, therefore
Writethrough is completely impactical
Use write-back...
Which means dirty bit in PTE is set when page is written
	
### Recall fatal flaw
Address translation would appear to require extra memory references
One to accesss PTE
Then the actual memory access
but **Access to page tables has good locality**
So use **a fast cache of PTEs** within the CPU
This is called a **translation look-aside buffer **(TLB)
Typical TLB:
16-512 PTEs
0.5-1 cycle for hit
10-100 cycles for miss, 0.01%-1% miss rate
Misses could be handled by software/hardware
![](./Page_Tables/pasted_image001.png)

### Consequences of a TLB miss
**If page is in memory**
Load the PTE from memory and retry
Could be handled in hardware: this can get complex for more complicated page table structures
Or in software: raise a special exception, with optimized handler
**If page is not in memory **(i.e., an actual page fault)
OS handles fetching the page and updating the page table
Then retsrat the faulting instruction
	
### TLB Miss Handler
TLB miss indicates that either:

A. **Page present**, but PTE not in TLB, or
B. **Page is not present** in page table

In a pipeline architecture, control must recognize TLB miss before destination register overwirtten
raise a hardware exception
Handler updates TLB by copying PTE from memory into TLB
and then restarts the instruction
But if page is not present, page fault
	
### Page Fault Handler

1. Use faulting virtual address **to find PTE**
2. **Locate page **on disk
3. Choose physical frame to replace
	a. If chosen frame is free, use it
	b. If frame chosen is dirty, write to disk first (i.e., write back_)
4. Read page into physcial frame (memory) and update page table
5. Make process runnable again
	a. Assuming here that step 1 has blocked / suspended the faulting procesd
	b. Onr page fault is handled, process is re-enabled for execvtuion
	c. With the assumption is will fetchthe original instruction once again

![](./Page_Tables/pasted_image002.png)

### Memory Protection
Different processes can share parts of their virtual address spaces
But need to protect againsr errant access
Requires OS assistance
Hardware support for OS protection
**Privileged supervisor mode** (aka **Kernel mode)**
**Privileged instructions**
Page tables and other state information **only accessible in supervisor mode**
**System call exception** (e.g., syscall in MIPS)



