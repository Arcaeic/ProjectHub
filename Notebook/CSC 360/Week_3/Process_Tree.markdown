# Process Tree
Created Tuesday 16 January 2018

Sched(0)
init(1)
All user processes
pageout
memory
fsflush
file system

![](./Process_Tree/pasted_image.png)![](./Process_Tree/pasted_image001.png)

### Parent vs Child process
Proecss; running program + resources
Resource sharing: possible approaches
all shared, or
some shard, or
nothing shared
Proces execuion: possible approaches
Parent waits until child finishes, or
Parent and child run concurrently
	

#### fork(), exec*(), wait()
Create a child process: fork()
return code < 0: error (in "parent" process)
return code =  0: You're in child process
return code > 0: You're in parent process
Child process: load a new program:
exec*(): front-end for execve(file, arg, environ)
Parent process: wait() and waitpid()


