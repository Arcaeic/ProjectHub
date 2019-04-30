# Paging performance
Created Friday 23 March 2018

Page Fault Rate 0 <= p <= 1.0
if p = 0, no page faults
if p = 1, every reference is a fault

Effective Access Time (EAT)
EAT = (1-p) x memory access + p * (page fault overhead + swap page in + swap page out + restart overhead)
	

