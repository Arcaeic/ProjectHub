# Allocating Blocks
Created Thursday 08 March 2018

### Contiguous Allocation
Address of the first block, length
random access
difficult to expand
fragments
Modified contiguous allocation
e.g., extent-based allocation
![](./Allocating_Blocks/pasted_image.png)
Downsides: Determining the size of the new file
finding a suitable place to put the new file
Accommodating large files comes with overhead of moving data blocks and configuring start pointers

### Linked Allocation
Gets rid of the downsides of contiguous allocation
Data can be scattered everywhere
Address of the first block
each block has a next pointer
next == -1, end of block list
Easy to expand
No direct random access
Modified linked-list
e.g., cluster-based allocation

![](./Allocating_Blocks/pasted_image001.png)
Downsides: Only realistic for sequential-access file systems
The amount of space used for storing pointers

### File Allocation Table
FDT
First block address
FAT
linked list of addresses
Can be cached
random address
		
![](./Allocating_Blocks/pasted_image003.png)
Improves random access reads/writes due to knowing FAT storing the location of any block

### Indexed Allocation
Direct access is more realistic here, since the first block looked at contains pointers to any block of interest. Sequential access is still possible if the subsequent pointers are indexed in order.
Index block
A list of pointers
random access
easy to expand
limited by the size of index block
![](./Allocating_Blocks/pasted_image004.png)


### indirect Index
e.g., UNIX I-Node
12 direct idnex
1 single indirect
1 double indirect
1 triple indirect
- 4KB block


