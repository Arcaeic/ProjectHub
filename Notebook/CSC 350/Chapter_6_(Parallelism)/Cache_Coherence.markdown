# Cache Coherence
Created Monday 18 March 2019

Suppose two CPU cores **share a physical address space**
Assume also each has their own write-through cache
![](./Cache_Coherence/pasted_image.png)

### Coherence
Informal Definiton: A **memory read**returns the **most recently written value**
**Formally:**
P writes X; P reads x **(no intervening writes) -> read of X returns written value**
P1 writes X; P2 reads X (with read occuring "sufficiently" later)
**Read of X returns wirrten value**
P1 writes X, P2 writes X
all processors see writes **in the same order** (called **write serialization)**
![](./Cache_Coherence/pasted_image001.png)



