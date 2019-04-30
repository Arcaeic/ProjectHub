# Optimized
Created Monday 28 January 2019

Perform steps in parallel: **add/shift**
Idea:
Place multiplier in the right half of product register
Place result of add into the left half of product register
Shift to adjust
	
![](./Optimized/pasted_image.png)

### Faster Multiplier
Use multiple adders
Cost/performance tradeoff
Pipelining can improve performance even more
(We will introduce pipelines in the next chapter)


### MIPS: Integer Multiplication
HI: most-significant 32 bits
LO: least-significant 32 bits
Note that these registers are not directly accessible (i.e., we must use special instructions to move out results)
	
![](./Optimized/pasted_image001.png)




