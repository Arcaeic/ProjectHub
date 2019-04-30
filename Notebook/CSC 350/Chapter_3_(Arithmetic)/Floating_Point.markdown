# Floating Point
Created Monday 04 February 2019

Three representations:
single precision (32 bit)
double precision (64 bit)
half precision (16 bit)
	

![](./Floating_Point/pasted_image.png)
**S: **Sign bit. (0 for negative, 1 for positive)
**Significand:** the **mantissa** with the leading 1 restored
**Exponent:** actual exponent after normalization  + bias
why? To ensure exponent is unsigned!
Single precision: bias = 127
Double precision: bias = 1023
![](./Floating_Point/pasted_image001.png)

