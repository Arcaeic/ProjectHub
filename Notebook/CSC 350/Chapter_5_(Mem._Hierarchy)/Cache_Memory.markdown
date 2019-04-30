# Cache Memory
Created Monday 11 March 2019

Cache memory normally considered to be level of memory hierarchy closest to the CPU
Given memory accesses of X1, X2,.., Xn
**How** do we determine **if the data is present**?
**Where in the cache** do we look
	
### Direct Mapped Cache
![](./Cache_Memory/pasted_image.png)

Location in of memory block in the cache (i.e., cache line) is determined by memory-reference address
**Direct mapped:** only one possible mapping of **memory block** to **cache line** (i.e., directly determined by memory address)
(Block address) % (num blocks in cache)


### Tag and Valid Bits
We know where a particular block is to be stored in the cache
but how do we determine what block is stored in a particular cache line?
Therefore:
Store **some part of the block address** in the cache along with block data
Specifically we need high-order bits of the address (i.e., low-order bits had been used to determine cache line)
This part of the block address become the **tag**
What if there is no data in a cache line?
Use a **valid bit: **1 -> data is present, 0 -> no data is present
At reset/startup, all cache lines have their valid bits set to 0
Therefore: a cache stores **data, tags **and **valid bits**



