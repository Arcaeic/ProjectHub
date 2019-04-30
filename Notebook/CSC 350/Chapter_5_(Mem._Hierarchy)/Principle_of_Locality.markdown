# Principle of Locality
Created Monday 11 March 2019

One way forward is to exploit the actual pattern of memory references made by software
Programs access a small **proportion** of their total address space (also called locality)
**Temporal Locality**
Items access recently are more likely to be accessed again in the near future
Ex. instructions in a loop, induction variables for a loop
**Spatial Locality**
Items in memory located near those accessed recently are also likely to be accessed soon
Ex. Sequential access of instructions, array data

![](./Principle_of_Locality/pasted_image.png)

### Exploiting Locality
We cannot get away from the reality of the memory hierarchy
The faster the memory, the more expensive in space and price
The larger the memory, the slower it is, but cheaper
Therefore:

a. **Store everything** on disk
b. **Copy recently accessed** (time) **and nearby** (space) items from disk to smaller/faster DRAM memory (i.e., main memory)
c. **Copy more recently addressed** (and nearby) items from DRAM to smaller SRAM memory (i.e., cache)


