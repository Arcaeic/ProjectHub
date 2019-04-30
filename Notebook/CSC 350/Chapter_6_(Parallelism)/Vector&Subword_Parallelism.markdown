# Vector&Subword Parallelism
Created Monday 18 March 2019

Example: **Streaming SIMD Extension 2 (SSE2)**
Adds 4 x 128-bit registers
Extended to 8 registers in AMD64/EM64T
Can also be used for multiple FP operands
2x 64-bit double precision
4x 32-bit double precision
An instruction may operate on these special registers s.t. **multiple** **operations** occur via  a **single instruction**
**Single-instruction Multiple Data (SIMD)**
![](./Vector&Subword_Parallelism/pasted_image001.png)
![](./Vector&Subword_Parallelism/pasted_image.png)

### Example of SSE benefits
Image processing example
Removing all blue from the pixels of an image
In essesnce: zeroing out the blue bits in a 24-bit color image
Image size is 1024x768
Executed on a core 17-930 processor (Stock)
Remove blue **without SSE:** 6.7082 milliseconds per call
**With SSE2:** 1.2512 ms per call
**With SS5** (more acurately called **AVX**): 0.2430 ms per call
	

