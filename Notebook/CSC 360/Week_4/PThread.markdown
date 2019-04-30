# PThread
Created Tuesday 23 January 2018

Create a thread
int pthread_create (thread, attributes, start_routine, args)
PC: start_routine(arguments)
default attributes: joinable and non-realtime
Exit from a (created) thread
void pthread_exit(return_value);
cleanup handles by pthread_cleanup_push();
	
Wait for a target thread to exit: *synchronize*
int pthread_join(thread, return_value)
release resource allocated to the target thread
Put a target thread in detached state
int pthread_detach(thread)
No other threads can "join" this one
no "pthread_attach"
Resources released once the thread exits
Thread can be created in detached state

Cancel another thread
int pthread_cancel(thread);
calling a thread: send a request
target thread: pthread_setcancelstate();
ignore the request
terminate immediately
asynchronous cancellation
check whether it should be cancelled periodically
		


