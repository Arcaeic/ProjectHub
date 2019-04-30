# Disk Storage
Created Monday 11 March 2019

Old school nonvolatile, rotating magnetic storage
![](./Disk_Storage/pasted_image.png)

### Disk Sectors and Access
Each sector records
Sector identifier
Data (512 bytes, 4096 bytes proposed)
Error correcting code (ECC) (used to hide/compensate for defects and recording errors)
Synchronization fields and gaps
Access to a sector involves
**Queueing delay** if other accesses are pending
**Seek: **Move the heads
**Rotational latency**
**Data transfer**
**Controller overhead**
	



