# Cache Misses Consequences
Created Monday 11 March 2019

On a **cache hit:**
The CPU proceeds normally (after retrieving needed value from cache)
On a **cache miss:**
CPU pipeline must be stalled
Block is fetched from the next level down in the memory hierarchy. If **instruction-cache miss:** restart instruction fetch
If **data-cache miss: **complete the data access as needed for the operation
The situation is far messier, though, **when writes are involved**

[+Write Through](./Cache_Misses_Consequences/Write_Through.markdown)

