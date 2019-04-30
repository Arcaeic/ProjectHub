# Virtual Memory
Created Saturday 16 March 2019

Use main memory as a **cache for secondary (disk) storage**
Managed jointly byu CPU hardware and the operating system (OS)
Processes share physical main memory
Each has a **private virtual address space** holding its frequently used code and data
Protected from other processes
CPU and OS translate **virtual addresses** to **physical addresses	**
VM **block **is called a **page**
VM pages are mapped into main memory **frames**
VM **translation miss** is called a **page fault**
	
![](./Virtual_Memory/pasted_image.png)


![](./Virtual_Memory/pasted_image001.png)

