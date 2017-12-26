# How many records can we sort
Created Thursday 02 November 2017


1. Block size is B bytes
2. Main mem availbale for buffering blocks is M bytes
3. Record is R bytes


# of main mem buffers = M/B blocks
We need out output buffer, so we should use (M/B)-1 input buffers

**How many sorted sublists can we merge?**
**(M/B)-1**

**Whats the total number of records we can sort?**
Each time we fill in the mem with M/R records
hence, we are able to sort (M/R)*[(M/B)-1] or approx M^2/RB

If we use the params in the example about 2PMMS we have 
M = 100MB = 10^8 bytes.
B = 16K bytes
R = 160 bytes
So, M^2/Rb = (10^8)^2 / (160 * 16,384) = 4.2 billion, or 2/3TB

### Sorting Larger Relations

* If our relation is bigger, then we can use 2PMS to create sorted sublists of M^2/RB records
* Then, in a third pass we can merge (M/B)-1 of these sroted sublists
* Thus, the third phase let's us sort
	* [(M/B-1]*[M^2/RB] ~ M^3/RB^2 records
* For our ex, the third phase lets us sort 75 trillion records, or about 7500PB.


