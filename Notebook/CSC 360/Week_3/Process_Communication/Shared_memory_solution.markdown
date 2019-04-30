# Shared memory solution
Created Friday 19 January 2018

### Shared memory solution
**Memory mapping**
Allocated in the calling prcess's address space
attached to other processes' address space
Data structure: bounded, circular
#define BUFFER_SIZE 10
Typedef struct {...} item;
item buffer [BUFFER_SIZE];
int in=0; int out =0;
empty, full, # of items

**Producer:**
wait for an available space
update in
item nextProduced;
while (true) {
/* produce an item in nextProduced */
while (((in + 1) % BUFFER_SIZE) == out)
; /* do nothing (busy loop, will be improved later) */
buffer[in] = nextProduced;
in = (in + 1) % BUFFER_SIZE; }
		
**Consumer:**
wait for available item
update out
item nextConsumed;
while (1) {
while (in == out)
; /* do nothing */
nextConsumed = buffer[out];
out = (out + 1) % BUFFER_SIZE;
/* consume the item in nextConsumed */ }

