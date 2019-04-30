# Increasing Performance
Created Saturday 12 January 2019


* With Moore's Law failing and the power wall before us, how can progress be made?
* Answer: **Multiprocessing**
	* **Multiple cores per chip** (symmetric)
	* System-on-a-chip, or **SoC** (assymetric)
* Multiprocessing requires **explicit parallel programming**
	* This is different to **Instruction Level parallelism**, where parallelism is hidden from programmer
* Such explicit programming has historically been hard to do
	* Programming for performance
	* Load balancing
	* Optimizing communication and synchronization


### Real World Benchmarks

* Apples-to-apples comparisons between architectures is difficult
* **Standard Performance Evaluation Corp **(SPEC)
	* Industry-sponsored entity
	* Must purchase the benchmark code (not super pricey)
	* Very large set of benchmarks now exist
* The idea
	* A reference machine (chosed by SPEC) is used to run the benchmark
	* Results are posted
	* The system to be test5ed then has its results reported as a performance number compared to the reference machine
* Elapsed time to execution as a selection of programs is the sole measure
	* I/O is neglible, therefore CPU performance is key
	* Times are normalized with respect to reference machine
	* All of the suite's tests are summarized as a **geometric mean**

![](./Increasing_Performance/pasted_image.png)
![](./Increasing_Performance/pasted_image001.png)
![](./Increasing_Performance/pasted_image002.png)


* Given concerns with power, SPEC has prepared relevant benchmarks
	* Power consumption of server at **different workload levels**
	* Performance is measure as ssj_ops / sec
		* Server Side Java Operations per second
	* power is in watts (joules / sec)



### Three Pitfalls

* Focusing on improving performance but w/o considering overall system
* Assuming power-consumption behaviour for lightly loaded systems
* Using MIPS as a performance 


#### Pitfall: Amdahl's Law

* Pitfall: Improving one aspect of a computer and expecting a proportional improvement in overall performance
	* ``Timproved=(Taffected/improvement) = Tunaffected``



