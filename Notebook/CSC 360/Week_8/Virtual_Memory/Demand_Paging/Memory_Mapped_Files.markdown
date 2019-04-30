# Memory Mapped Files
Created Friday 23 March 2018

Memory-mapped file I/O allows file I/O to be treated as a routine memory access by mapping a disk block to a page in memory

A file is initially read using demand paging. A page-sized portion of the file is read from the file system into a physical page. Subsequent reads/writes to/from the file are treated as ordinary memory accesses.

Simplifies file access by treating file I/O through memory rather than read() and write() system calls

Allows several processes to map the same file allowing the pages in memory to be shared

### Mem-Mapped File Sharing
![](./Memory_Mapped_Files/pasted_image.png)

