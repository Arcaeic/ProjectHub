# Process Termination
Created Friday 19 January 2018


* Terminate itself: exit()
	* report status to parent process
	* release allocated resources
* Terminate child processes: kill(pid, signal)
	* Actually send signal to the child
		* child resource exceeded, child process no longer need, etc
	* Parent is exiting
		* cascading termination, or find another parent



