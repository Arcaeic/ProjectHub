# Software-based (Mutex)
Created Wednesday 21 February 2018

Mutual Exclusion (Mutex)
only two states:
unlocked: there is no thread in critical section
locked: there is one thread in critical section
state change is atomic
If unlocked, it can be locked by at most one thread when entering the critical section
if it is locked, it can only be unlocked by the locking thread when leaving the critical section


### Procedures
create a mutex variable (initially unlocked)
(some threads) attempt to lock mutex
only one can lock the mutex
others may be blocked and waiting
the one with the mutex
execute the critical section
unlock the mutex variable eventually
destroy the mutex variable
	

