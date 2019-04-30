# Character Data
Created Monday 21 January 2019

Two main byte-encoded character sets:
**ASCII**: 128 characters (95 graphic, 33 control)
**LATIN-1**: 256 characters (ASCII plus 96 more graphic characters)
	
More modern character data is in **Unicode**
32-bit character set
Used in Java, C++, "wide chars"
Is able to encode most of the world's alphabets, plus symbols
Two main flavours: UTF-8, UTF-8 (variable-length encoding)
	
### Copying a String
Recall in C, **strings are interpretations of char arrays**
In production code we would use strncpy, not strcpy,
For simplicity, codier the much-more risky function
![](./Character_Data/pasted_image.png)

	
![](./Character_Data/pasted_image001.png)
	
### 32-bit Data
Most constants are small..
therefore 16-bit immediates will work
However, sometimes we do have need for a 32-bit constant (i.e. memory locations)
Load one 16-bit constant into the left bits of some destination register
bitwise OR the lower 16-bits
lhi: copies 16-bit constant to left 16-bits of rt, then clears right 16-bits of rt.
![](./Character_Data/pasted_image002.png)



