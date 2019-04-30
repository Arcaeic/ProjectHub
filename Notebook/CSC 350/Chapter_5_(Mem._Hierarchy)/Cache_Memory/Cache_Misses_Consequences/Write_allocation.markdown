# Write allocation
Created Monday 11 March 2019

On a write miss, the situatoin is more complex
Possible strategies for write through:
Allocate on a miss (i.e., fetch the block)
Write around on a miss (i.e., dont fetch the block))
(Since programs often write a whole block before reading it, as with initialization, there may be no need to fetch at all)
Possible strategy for write-back
Usually fetch the block
An elaboration in the textbook (page 394) describes some of the subtle gotchas involving misses with write-back schemes
	

