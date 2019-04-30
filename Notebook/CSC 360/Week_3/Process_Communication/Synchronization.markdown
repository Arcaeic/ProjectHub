# Synchronization
Created Friday 19 January 2018

Blocking vs non-blocking
blocking send
caller blocked until send is completed
blocking receive
caller block until receive is finished
non-blocking send
non-blocking receive
A means of synchronization

### Buffering
To hold message temporarily
Zero capacity
sender blocks until receiver is ready
otherwise, message is lost
bounded capacity
when buffer is full, sender blocks
when buffer is not full, no need to block sender
unbounded capacity
no need to block sender


