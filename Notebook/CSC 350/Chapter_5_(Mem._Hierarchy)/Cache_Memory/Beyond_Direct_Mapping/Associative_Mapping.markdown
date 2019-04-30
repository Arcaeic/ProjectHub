# Associative Mapping
Created Monday 11 March 2019

### Associative Caches
**Fully Associative**
Allow a given block to go to any cache entry
Requires all cache entries to be searched simultaneously to determine cache hit/miss
One comparator per entry (i.e., expensive)
**I-Way Set Associative**
Each cache set contains I entries
Block number determine cache set for storage
**That is:** *block number * % |Sets in cache|
Search all entries in a given set simultaneously
*n* comparators (less expensive than full associative)
![](./Associative_Mapping/pasted_image.png)

### Degress of associativity
![](./Associative_Mapping/pasted_image001.png)
	

### Direct Mapped
![](./Associative_Mapping/pasted_image002.png)


### Two way set associative
![](./Associative_Mapping/pasted_image003.png)

### Fully associative
![](./Associative_Mapping/pasted_image004.png)

### If associativity is so good
Why not have more of it?
Increased associativity does decrease miss rate..
but **with diminishing returns**
along with **steadily increasing costs**
![](./Associative_Mapping/pasted_image005.png)


