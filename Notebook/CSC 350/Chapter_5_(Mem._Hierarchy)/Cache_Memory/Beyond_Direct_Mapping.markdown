# Beyond Direct Mapping
Created Monday 11 March 2019

So far we have seen the direct-mapped cache approach
Location of block in cache is directly detemined by **block number** i.e., floor (block memory address / block size)
Another possibility provides flexibility for block repalcement
Blocks can be placed in cache where there is a free/available line
Flexibility comes with a porice: additional hardware complexity
Two further possibilites:
**Fully associative cache**
**N-way set associative cache**
	
[+Associative Mapping](./Beyond_Direct_Mapping/Associative_Mapping.markdown)


### Replacement Policy
That is:
When no blocks in the cache are free
How do we choose the block to be replaced?
Options for direct-mapped: **there is no choice!**
Prefer a non-valid entry, if there is one (i.e., an unused entry)
Otherwise: choose among entries in the set
**Least-recently-used** (LRU) replacement
Choose entry unused for longest time
Simple to track for 2-day, progressively harder for 4- or 8- way
**Random **replacement
Gives approximately the same performane as LRU for high associativity
	

