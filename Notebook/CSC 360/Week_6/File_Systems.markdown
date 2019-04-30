# File Systems
Created Wednesday 28 February 2018

### File Concepts
**File**: logical storage unit for information
normally on disk and tape: nonvolatile
**File type**: data (text, binary, ... ) program
**File attributes**: meta information
name, size, type, owner, protection, location
kept in directory (special file)
**File operations:**
create, wite, read, seek, append, truncate, etc
	

### File Structures
How information is organized within a file
unstructured
structured w/ records
Example
Binary executable
binary archive
modules
![](./File_Systems/pasted_image003.png)

### File Access
Sequentual access
variable length records ie. r~1~, r~2~, .., r~n~
to access r~i~, you must go through r~1 ~through r~i-1~
Direct Access
fixed length records of size s
i-th record: offset (i-1)*s
Indexed Access
Access index first
	

### Directory Concepts
- A special file
A collection of meta information of files
Normally structured
and allow direct access
**Directory Operations:**
"file" operations on drectory records
File systems on disk
partitions, file systems (FDT, FAT)
	

### Directory Structures
**Single-level**
One directory for all files, easy to understand
Significant issues. What if there are more than one user? What if a file is named the same as another?

**Two-level**
A master file directory exists, one directory belonging to each user
Faster than single-level, searches are cut down drastically and multiple users can have similarly named files
	
==== e.g., user1/bin/mount is different than user2/bin/mount ====
![](./File_Systems/pasted_image.png)
![](./File_Systems/pasted_image001.png)

![](./File_Systems/pasted_image002.png)




**Path**
absolute: Begins from the first directory (/) in the system and directs to a specified location
relative: contains a path using (~) or another environment variable



