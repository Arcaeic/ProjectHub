# Multilevel Caches
Created Monday 11 March 2019

Constructing very large caches supporting low miss rates is difficult
Complex
Expensive (real-estate, power)
Alternative: **multilevel cache**
**Level 1 cache**: Attached to CPU
Small but very fast
**Level 2 cache: **attached to primary cache and main memory
Larger, slower
but still much faster than main memory
and therefore faster than having main memory service L-1 cache misses
Main memory serves L-2 cache misses
	
### Example (w/o multilevel)
given:
CPU base CPI:1
Clock rate= 4Ghz
Miss rate per instruction: 2%
Main memory access time = 100 ns
Miss penalty:
Equals time taken to access main memory
# cycles: 100ns / .25ns = 400 cycles
Effective CPI: 1 + 0.02 x 400 = 9
	
### Example (with multilevel)
Now add Level 2 cache:
Access time = 5ns
Global miss rate to main memory (i.e., % of accesses that cannot be served by L-2 cache): 0.5%
L-1 miss with an L-2 hit:
Penalty: 5ns/.25ns = 20 cycles
L-1 miss with an L-2 miss:
Penalty: additional access time to main memory (400 cycles)
CPI: 1 + 0.02 x 20 + 0.005 x 400 = 3.4
Performance ratio: 9 / 3.4 = 2.6


