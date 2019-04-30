# Critical Section Solutions
Created Thursday 08 March 2018

### Petersons Solution:
as Pi:
do {
flag[i] = true;
turn = j
while (flag[j] && turn == j) { wait }
 //do shit
flag[i] = false
while(1)

Assumes a second process is there to exchange turns with

### Dekker's Solution
while(true)
flag[i] = true;
while (flag[j] 
if turn == j
flag[i] = false
while turn == j //wait
flag[i] = true
//critical section
turn = j
flag[i] = false
//remainder section

