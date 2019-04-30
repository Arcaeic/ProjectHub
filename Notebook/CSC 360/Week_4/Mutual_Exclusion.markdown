# Mutual Exclusion
Created Thursday 25 January 2018
**(mutex)**

Only two states
unlocked: there is no thread in critical section
locked; there is one thread in critical section
state change is  atomic
if it is unlocked, it can be locked by at most one thread when entering critical section
if it is locked, it can only be unlocked by locking thread when leaving the critical section
	
#### Mutex procedures
Create a mutex variable (initially unlocked)
(some threads) attempt to lock mutex
only one can lock
others may be waiting/blocked
the one with mutex
execute critical section
unlock the mutex variable eventually
destroy mutex variable


#### Mutex with pthread
Create mutex 
int pthread_mutex_init(mutex, attr)
Attempt to lock
int pthread_mutex_lock(mutex)
if unlocked, lock and return immediately
if locked
"fast" lock: block until unlocked
"test" lock: return immediately with error
"recursive" lock: "over"-lock
multiple pthread_mutex_unlock() to unlock
Try to lock
pthread_mutex_trylock(mutex)
if locked, return immediately with error code
Unlock
int pthread_mutex_unlock(mutex)
if "recursive" lock, multiple pthread_mutex_unlock necessary to fully unlock mutex
Destroy Mutex
int pthread_mutex_destory(mutex)


#### Condition Variable
used together with mutex
mutex: control access to shared data
condition: synchronize by condition "predict"
Wait for condition
pthread_cond_wait(condition, mutex)
auto unlock and wait for signal
on signal, wake up and auto lock
Signal or broadcast
pthread_cond_signal(condition)
		


#### Shared or not shareD?
E.g, increment a **counter** (shared variable)
read the counter from mem
increment by one
write the counter

#### Critical Section
Critical Section
Code section accessing shared data
only one thread executing in critical section
only one thread accessing the shared data: serialize
choose the **right** (size of) critical section
Approach: exclusion (lock)
If locked, wait!
if not locked, lock, and later unlock
	

