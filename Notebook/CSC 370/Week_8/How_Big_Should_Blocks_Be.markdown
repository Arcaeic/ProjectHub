# How Big Should Blocks Be
Created Thursday 02 November 2017


* We have assumed a 16K block in our analysis.
* Would a large block size be advantageous?



* If we doubted the size of blocks, we would halve the number of disk I/Os.
* But, how much a disk I/O would cost in such a case?



* Recall that it takes abou .13ms for transfer time of a 16K block and 10.63ms for avg seek time and rotational latency



* Now, the only change in the time to access a bloc would be that the transfer time increases to 0.13*2 = 0.26, i.e only slightly more than before.



* We would thus approximately halve the time the sort takes.


#### Ex: Block Size = 512K

* For a block size of 512k the transfer time is 0.13*32 = 4.16ms



* AVG block access time would be 10.63 + 4.16 approx 15ms (opposed to the 11ms we had)



* However, now a block can hold 100*32 = 3200 tuples and the whole table will be 10,000,000 / 3200 = 3125 blocks (as opposed to 100,000 blocks we had before)



* Thus, we would need only 3125 * 2 * 2 disk I/Os for 2PMMS for a toal of time of 3125* 2* 2* 15 = 182,500ms or about 3.12 minutes


### Reasons to Limit the Block Size

1. First, we cannot use blocks that cover several trakcs effectively
2. Second, small tables woul occupy only a fraction of a bloc,so large blocks would waste space on the disk.
3. Thirs, the large the blocks are, thhe fewer records we can sort by 2PMMS (see next slide)


Nevertheless, as machines get more mem and disks more capaious, there is a tendency for block sizes to grow.

