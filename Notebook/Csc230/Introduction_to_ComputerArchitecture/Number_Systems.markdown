# • Number Systems

* Created Thursday 05 January 2017


### • Decimal

* Base: 10
* Digits: 0,1,2,3,4,5,6,7,8,9
* **Ex: **7423~10~ = 7000 + 400 + 20 + 3 = 7 * 10^3 + 4 *10^2 + 2* 10^1 + 3 * 10^0
* **Ex:** 14.43~10~ = 1 * 10^1 + 4 * 10^0 + 4 * 10^-1 + 3 * 10^-2


### • Binary

* Base: 2
* Digits: 0,1
* **Ex: **1010~2~ = 1 * 2^3 + 1 * 2^1 = 10 ~10~



#### • Hexadecimal

* Base: 16
* Digits: 0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F
* **Ex:  **F4A~16~ = 15 * 16^2 + 4 * 16^1 + 10 * 16^0 = 3914~10~



### • Octal

* Base: 8
* Digits:0,1,2,3,4,5,6,7
* **Ex: **74~8~ = 60~10 ~= 3C~16~

	

• Going from Decimal to Other Bases:
------------------------------------

* Ex: 35~10 ~ to base 2:
	* 32/2 = 17 + Remainder 1
	* 17/2 = 8 + Remainder 1
	* 8/2 = 4 + Remainder 0
	* 4/2 = 2 + Remainder 0
	* 2/2 = 1 + Remainder 0
	* 1/2 = 0 + Remainder 1
* = 100011~2~

	

* Ex: 35~10~ to base 16"
	* 35/16 = 2 + Remainder 3
	* 2/16 = 0 + Remainder 2
* = 23~16~

	

### • Direct Conversion: Binary to Octal and Hex:

* (Recall that binary is base 2)
* since 8 = 2^3, it takes 3 bits to represent the 8 octal digits
* since 16 = 2^4, it takes 4 bits to represent the 16 hex digits

	

* From binary to hex:
	* Form groups of 4 bits from right to left
	* Append leading 0s if needed
	* Encode each group into hex
* From binary to octal
	* Form groups of 3 bits from right to left
	* Append leading 0s if needed
	* encode each group into octal



### • Signed Magnitude + One's Compliment = 2's Compliment

* **Signed Magnitude:**
	* Leading bit is the sign:
		* 0 for +
		* 1 for -
	* The other bits are magnitude
	* Ex: 00001100~2~ = +12~10~
	* Ex: 10001100~2~ = -12~10~
	* **Simple for humans, bad for computers**
		* There are 2 representations for 0: +0 and -0
	* Ex: -10~10  ~to 2s compliment
* Find absolue value of -10
	a. = 0000 1010
* invert (1's compliment)
	a. 1111 0101 
* Add 1
	a. 1111 0110
		* = 1111 0110
	b. From negative 2s compliment to decimal:
		* Invert and add. If leading bit is 1, it is negative, otherwise positive


### • Coding Letters & Numbers for Computers

* There are many codes used in computing, some are:
	* BCD - Binary coded decimal, a direct representation of decimal digits
	* ASCII - American Standard Code for Information Interchange: characters
	* Parity - A simple error detection system



### • Parity - The extra bits of error



