# Threading Issues
Created Tuesday 23 January 2018

### Threading Issues

* When a new process is created
	* fork(), and usually then exec()
		* duplicate all threads or just the calling thread?
	* When a signal to be delivered
		* signal: event notification to be handled
			* to all, some, or a specific thread?
	* Thread pool
		* Keep a pool of threads to be used
			* and reused


