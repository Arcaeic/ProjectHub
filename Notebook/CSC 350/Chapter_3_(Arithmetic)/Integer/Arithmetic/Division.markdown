# Division
Created Monday 28 January 2019

We can assume division is less frequent than multiplication
[Terms:](./Terms.markdown)
Dividend: What is being divided
Divisor: By what
Quotient: Result
Again:
Grade-school long division is an algorithm lending itself to paper-and-pencil work
	
### Procedure

1. Check for zero divisor
2. Then:
	a. If divisor <= dividend bits, place 1 bit in quotient and subtract
	b. otherwise 0 bit in quotient, and bring down next bit

Special Case:
We **restore the division** if subtraction is performed and there is a negative result
in which case the divisor is added back
	
**Signed Division:**
Divide using absolute values
Adjust sign of quotient and remainder as required
![](./Division/pasted_image.png)


