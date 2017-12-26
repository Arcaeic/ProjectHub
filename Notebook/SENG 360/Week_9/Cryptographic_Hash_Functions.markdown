# Cryptographic Hash Functions
Created Saturday 11 November 2017

MD5 Digest always hashes to 128 bits
(one way loss encryption -> digest)

Using CBC (Cipher Block Chain) we can create something called **Avalance Effect**, which is the when plaintext barely differs from another piece of plaintext, yet when encrypted the output is radically different

![](./Cryptographic_Hash_Functions/pasted_image.png)

### Desireable Properties: Hash Function
**Pre-image reistance**
**Given a hash value h ** it should be difficult to find any message **m such that **has **h = hash(m) **(pre-image attack)

**Second pre-image resistance:**
Given an input **m1** it should be difficult to find different input **m2** such that **hash(m1) = hash(m2) **(second pre-image attack)

**Collision resistance**
it should be difficult to find two different messages **m1 ** and **m2 ** such that **hash(m1) = hash(m2) **(birthday attack)

#### The birthday Attack
![](./Cryptographic_Hash_Functions/pasted_image001.png)


