# Page Buffering
Created Friday 23 March 2018

Keep a pool of free pages
Speed up swapping in desired pages
No need to wait a page becomes free
	
Keep a list of modified pages
Synch with disk when paging is idle
Reduce overhead when swapping out
	
Reuse "clean" pages from the pool
On page fault, check free pool first
	

