# Page Fault Penalty
Created Saturday 16 March 2019

On page fault (i.e., cache miss) the page must be fetched from disk
Takes **millions** of clock cycles
Handled by OS code
In essence, the time taken to service the page fault is not counted as CPU time, as the OS usually suspends the faulting process until the page fault is fully serviced
Design goal: **Try to minimize page fault rate**
**Fully associative placement**
**Smart replacement algorithms**



