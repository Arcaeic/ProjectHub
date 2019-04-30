# Arrays vs Pointers
Created Thursday 24 January 2019

Recall that:
Array indexing involve multiplying index by element size..
...and then add this value to the array base address
This differs from the use of pointers
Which are meant to correspond directly with memory addresses
such that using pointers where possible can help avoid any complexities introduced by indexing
	
![](./Arrays_vs_Pointers/pasted_image.png)

In both cases:
"Multiply by 4" **strength reduced** to "shift left by 2"
Array version require shift to be inside loop
This is part of index calculation for increment i
Compare this w/ what is involved to increment a pointer value....
Compiler via its translations and optimizations can achieve the same effect as manual use of pointers
**Induction variable elimination**
Could be considered a better technique to make program clearer and safer


