# Writing & Disk Access
Created Thursday 02 November 2017

Writing same as reading, unless we verify written blocks
Modifying a block requires

* Read a block into main memory
* Modify the block there
* Write the block back to disk.


### Using Secondary Storage Effectively
In most studies of algorithms, one assumes the "RAM model"
Data is in main memory
Access to any item of data takes as much time as any other.
When implementing a DBMS, one must assume that the data does not fit in main memory
Choosing an algorithm that requires few random disk accesses is the best, even if it isn't very efficient.

### I/O Model of Computation
Disk I/O = r/w of a block is very expensive compared with what is likely to be done wityh the block once it arrives in main memory
~1,000,000 machine instructions in the time to do one random disk I/O
	
##### Assumptions:
One processor
One disk controller. one disk
The database itself is much too large to fit in main memory
	
Many users, abd each user issues disk-I/O requests frequently
Disk controller serving on a first-come first-serve basis
Requests for a given user might appear random even if the table that a user is reading is store on a single cylinder of the disk.
	
The disk is a Megatron 747, with 16K blocks and the timing characteristics determined before
In particular, the average time to r/w a block is ~11ms.


#### Merge Sort
Common main memory sorting algorithms dont look so good when you take disk I/Os into account. Variants of Merge Sort are better.
(Make two sorted lists and repeatedly choose the smallest value in the two lists)
(Based on recursive alg, divide records into two parts, recursively mergesort the parts, and merge the resulting lists)

#### TwoPhase, Multiway Merge Sort
Merge Sort still not very good in disk I/O model.
Log~2~n passes
So each record is r/w from disk log~2~n times
Secondary memory variant (@PMMS) operates in a small number of passes
In one pass every record is read into main memory once and written out to disk once
2PMMS: 2 reads + 2 writes per block.
**Phase1:**

1. Fill main mem with records
2. Sort using favorite main memory sort
3. Write sorted sublist to disk
4. Repeat until all records have been put into one of the sorted lists

**Phase2:**

1. Input buffers, one for each sorted list
	a. Pointers to first unchosen records
2. Select smallest unchosen for output
3. Output buffer


OR (Same thing, but in english)


* Use one buffer for each of the sorted sublists and one buffer for an

output block.

* Initially load input buffers with the first blocks of their respective sorted

lists.

* Repeatedly run a competition among the first unchosen records of each

of the buffered blocks.

* Move the record with the least key to the output block; it is now “chosen.”


* Manage the buffers as needed:
	* If an input block is exhausted, get the next block from the same file.
	* If the output block is full, write it to disk.




