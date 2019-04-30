# Synchronization
Created Monday 18 March 2019

**Load linked **plus **Store conditional**
__ll rt, offset(rs)__
__sc rt, offset(rs)__
Store conditional **succeeds** if location not changed since load-linked instruction
If no change, 1 is stored in rt
If there is a change, 0 is stored in rt
Example: **atomic swap **(in order to test/set locking variable)
![](./Synchronization/pasted_image.png)

