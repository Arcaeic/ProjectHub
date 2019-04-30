# Defining Performance
Created Friday 11 January 2019


* What constitutes good performance or bad performance can vary with time and context:
	* E.g. Raw processing speed may conflict with system responsiveness (See 'mv' and 'cp' commands being faster in terminal than nautilius)
	* E.g. Throughput may conflict with raw processing speed



* We may explicitly choose the most important performance attributs for any given design problem, and nortmally performance attributes we choose can be measured and quantified



* For the first part of the course we will focus largely on **response time**
	* Sometimes called **execution time**
	* "How long does it take for machine for A to do a task T?"
* We will also look at **throughput.**
	* Total work done per units time
	* e.g., tasks or transactions per hour
* We should **always** be asking the following questions:
	1. How are response time and throughput affected by replacing the processor with a faster version?
	2. Ditto, but adding more processors instead?


### Relative Performance


* Define **Performance**: 
	* "X is *n* faster than Y"
	* __PerformanceX / PerformanceY = n__
* Example: 10s on X, 15s on Y
	* Performance~X~ / Performance~Y~ = 10/15 = 0.66.
	* therefore, X is 0.66 times slower than Y, or Y is 1.5 times faster than X


### Measuring Execution Time

* There are **two flavours** of time that are relevant.
	* **Elapsed Time:**
		* Total response time, including all aspects of processing, I/O, OS overhead, idle time
		* Resembles wall-clock time
	* **CPU Time**:
		* Time spent by CPU when processing a given job
		* Ignores I/O time, impact of other tasks' share of CPU
		* Sum of user CPU time and system CPU time
	* Different programs are affected differently by CPU and system performance


