# CHAR and VARCHAR
Created Thursday 14 September 2017

#### CHAR and VARCHAR
**CHAR(n)** allocates a fixed space, and if the string that we store is shortly, it is padded with blanks.
Differently, VARCHAR(n) denotes a string of up to n characters
VARCHAR(n) allows for compression to save space
Use CHAR(n) for frequently used fields, and use VARCHAR(n) otherwise

