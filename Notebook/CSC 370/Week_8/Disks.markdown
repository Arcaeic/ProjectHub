# Disks
Created Thursday 02 November 2017

### Disk Controller

![](./Disks/pasted_image.png)

1. Schedule the disk heads
2. Buffer data in and out of disk
3. Manage the "bad blocks" so they are not used.


### Disk access time
Latency of the disk (Access time): The time to bring a block to main memory from disk
Main components of access time are:

* **Seek time** = time to move heads to proper cylinder
* **Rotational delay** = time for desired block to come under head
* **Transfer time** = time during which the block passes under head.


### Cause of rotational delay

![](./Disks/pasted_image001.png)

#### Megatron Timing Example
To move the head assembly between cylinders takes:
**1ms** to start and stop, plus 
**1ms **for every **4000** **cylinders** travelled
Therefore, moving from the innermost to the outermost track, a distance of 65,536 tracks is about 17.38ms.

#### Best Case Time to Read a 16,384-byte block
The minimum time is just the transfer time

Since there are 4096 bytes per sector on the disk,the block occupies 4 sectors.
The heads must pass over 4 sectors and 3 gaps.

Recall that:
the gaps represent 10% of the circle and sectors the remaining 90%
i.e 36 degress are occupied by gaps, 324 degrees by sectors.
So
There are 256 gaps and 256 sectors per circle.
Therefore, a gap is 36/256 = 0.14 degrees, and
a sector is 324/256 = 1.265
The total degrees covered by 3 gaps and 4 sectors is: (3*0.14) + (4*1.265) = 5.48 degrees
The transfer time, is therefore (5.48/360) * 8.33 (1 rotation) = 0.13
	
#### Worst Case Time to Read a 16Bit block
Worst-case latency = 17.38 (seek time) +8.33 (1 rotation) +0.13 (transfer time) = 25.84ms.
	
#### Avg Case Time to Read 16bit block
Transfer time is always 0.13ms
Average rotational latency is half way around the disk, or 8.33/2 = 4.17ms.

Average seek time?
Some integration math bs tells us it's n/3, therefore the seek time is 1 + (65536/3)*(1/4000) = 6.46ms

Total: 6.36 + 4.17 + 0.13 = 10.76ms

