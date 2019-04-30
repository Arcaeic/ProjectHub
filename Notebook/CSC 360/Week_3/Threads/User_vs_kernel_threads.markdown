# User vs kernel threads
Created Tuesday 23 January 2018

User threads: e.g., pthread library
each process schedules its own threads
no context switching b/w thjese threads
a blocking call blocks the entire process
Kernel threads: in almost all modern OS
kernel maneges all threads
Can pickup another thread if one blocks
Hybrid approaches


