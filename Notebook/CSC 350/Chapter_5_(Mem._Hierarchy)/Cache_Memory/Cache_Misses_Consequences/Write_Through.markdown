# Write Through
Created Monday 11 March 2019

### Write Through
if a cache line/block is modified as a result of a data write
How is the memory lower in the hierarchy updated?
Just updating the cache is not enough
Memory would have contents **inconsistent** with the cache
One solution: **Writethrough**
Updating the cache for the write also updates memory

However
Writes take longer
For example: if base CPI = 1
and 10% of instructions are stores
and writes to memory take 100 cycles
then effective CPI is 1 + 0.1 x 100 = 11 CPI
(Normal CPI + cost of write)
Solution: a **write buffer**
Holds data modified in cache that is waiting to be writting to memory
CPU continues immediately
and only stalls on a write if the write buffer is already full
![](./Write_Through/pasted_image.png)


