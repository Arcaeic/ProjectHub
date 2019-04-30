# Software-based (Semaphores)
Created Wednesday 21 February 2018

Semaphore API
Semaphore S - integer variable
binary semaphore
counting semaphore
two indivisible (atomic) operations
also known as P() and V()

wait (S):
while S<=0:
S--;
signal(S)
S++


