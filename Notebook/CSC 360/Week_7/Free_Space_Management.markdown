# Free Space Management
Created Thursday 08 March 2018

Keep track of free blocks
Remove from the free block list when a file is created and stored in that block
Add block to free block list when a file in that block is deleted and block is freed
Bitmap/Bitvector
1 bit indicates whether a block is free or not
00010110101110101011111010101101010111111010101011111
(most significant bit is block 0)
Linked-list
A big free file
Grouping: list of free blocks
Counting: contiguous blocks

![](./Free_Space_Management/pasted_image.png)

### Efficiency and Performance
Efficiency: Tradeoff
e.g., FDT/FAT/block size, # of blocks/entries
static vs dynamic data structure
Performance: further tradeoff
buffer vs page cache
unified buffer cache
free-behind, read-ahead: sequential access
RAM disk
	

### Consistency Check
Inconsistency
B/w files and their meta-information
due to hardware/software failure
Consistency Check
e.g., fsck: file systems consistency check
superblock consistency
wrong counters
inode consistency
unreferenced inodes, missing free blocks, bad free blocks


### backup and restore
Backup to another medium
magnetic, optical ,etc
Full backup
Incremental backup
last backup time
last modify time
Backup cycle
full backup, {incremental backup*}


### Log-structured file system
Transactions: updates to file system
considered commited when written to a log
file system may not be updated yet
asynchronously update file system
performance improvement as well
after system crashes
transactions still to be completed
file system remains consistency

### NFS
(Networked FS)
Operations:
Searching for a file within a dir
Reading a set of dir entries
manipulating dirs and links
accessing file attributes
reading/writing files


### SunOS NFS
Access remote file system
Initially on UDP
With RPC/XDR
Transparency
Mount Protocol
Server: exportfs
access control
client: mount server: dir dir 
	

### Linux File Systems
MINIX
Up to 64MB system size, 14B file name
Journal file system (JFS), XFS, etc
Extended file system (ext)
ext, 1992, up to 2GB system, 255b name
ext2, 1993, up to 4TB system, 1KB block
ext3, 1998, ext2 + journalling
journal (low risk), hordered, writeback (high)
ext4, 2008, 1EB (system), 16TB file
	
	

