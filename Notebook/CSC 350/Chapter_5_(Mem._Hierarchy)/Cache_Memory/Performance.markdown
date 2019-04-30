# Performance
Created Monday 11 March 2019

Components of CPU time
**Program execution cycles:** which includes cache hit time
**Memory stall cycles:** mainly from cache misses
As a first approximation
Memory stall cycles = (memory accesses / program) x (misses / instruction) x miss penalty


![](./Performance/pasted_image.png)
As CPU performance is improved
miss penalty becomes more significant
that is:
A decreasing base CPI
means greater proportion of running time is spent on memory stalls
Increasing clock rate is not an obvious solutions
This simply means memory stalls will account for more CPU cycles
Conclusion:** Cannot neglect cache behaviour when evaluating computer system performance**




