# Write back
Created Monday 11 March 2019

There is an alternative.
On a data-write hit, we update the block in cache
and **keep track of the fact that this block/line is dirty**
When a dirty block is to be replaced
**only then** is the block ocntent written back to memory
We can also use a write buffer here to allow the new block coming in to be read first, and progress maintaned in the program.
Both schemes so far (Write through and write back) are assuming a write hit
What if there is a write miss? (What does this mean??)


