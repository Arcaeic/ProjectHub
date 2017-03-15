# UNIX
Created Monday 17 October 2016

### Shell

* Responsible for communication between the user and kernel.
* "shell" refers to its position in a diagram of UNIX structure
* Reads and interprets user commands at the terminal
	* or from within a shell script
* Implements job control
* Many shells exist
	* sh, csh, ksh, tcsh, zsh, bash
	* We use bash
		* bash extends sh "the bourne shell"
* The shell is the intermediary between you and the UNIX OS kernel
* it interprets your typed commands in order to do shit
	* The shell reads the next input line
	* it interprets the line (expand argument, substitutes aliases, etc)
	* performs action
* There are two families of shells:
	* "sh", and "csh" based shells
		* bash uses sh
	* they vary slightly in their syntax and functionality
	* You can see what shell you're using by typing echo $SHELL in terminal.


### Kernel

* The kernel is the core of the OS
	* large and complex, program
* To access a computer's hardware, a user's commands must go through the kernel
	* user must request the kernel to perform work
	* this is mediated by a command shell (e.g bash) or a system library
* Responsibilities:
	* memory allocation
	* file system(s)
	* loads and executs program (assumes a process model)
	* communication with internal/external devices (I/O)
	* bootstraps the system

AND MORE


### UNIX filesystem

* "file", "filesystem": **are key abstractions** of the UNIX computation model
* Practically anything can be abstracted as a file (devices, programs, data, memory, IPC, etc)
* Mainly responsible for providing abstracting blocks of information on phsycial storage device (HDD, flash) into logical blocks that user can manipulate
	* maps filenames to block numbers
	* handles block allocations; chains units together
	* provides methods to access data
* facilitates the "multiuser" view of the OS
* arranged as a hierarchy (tree-like)
	* organized as a collection of directrories, or folders
	* a forward slash (/) is used to seperate directory and file componenets (contrast  to Windows which uses backslash(\))


Properties of directories
-------------------------

* directories are just ordinary files
* information in directories simply has a special format
* every directory contains two special directory entries
	* ".." refers to parent directory in Working directory
	* "." refers to current directory
* '~' is used to denote a home directory
	* cd [/home/user](file:///home/user) = cd ~user
	* cd = cd ~


"working" vs "home" directory
-----------------------------

* "Working" directory is the directory you are in at any given time
	* Eliminates the need to specifiy full pathnames for files/directories
	* "Relative pathnames" are locations worked out in relation to the current working dir
* "Home" dir is usually made to be the working dir upon log in.
	* [/home/arcaeic](file:///home/arcaeic) is your home directory
* "pwd" prints working directory
* when changing directory, you can specify the a dir inside the wd, or you can start from the root.


File attributes
---------------
Every plain file and dir has a set of attributes, including:
user name (owner of file)
group name (for sharing)
file size (bytes)
creation time, mod time
file type (file, dir, device, etc)
permissions
![](./UNIX/pasted_image.png)

* to unix.text:
	* joe has full access
	* groups can only read/execute file
	* others can only read/execute file
	* dmg can only read and write
	* group/others can only read.



* Permissions can be set for:
	* user ("u") [-rwx------]
		* the file owner
		* should have full control over permissions
	* group ("g") [----rwx---] 
		* group for sharing
		* groups of users can be given shared access to files
	* other("u") [-------rwx]
		* any other
		* Any user who is not the owner and is not in a shared group
	* all ("a") user+group+other
* There are several ways to use "chmod"
	* use letters to represent "who" and "what"
		* chmod o+rx [file]
			* adds other users to read and execute permissions
		* chmod u+x [file]
			* adds user to execute permissions
		* chmod go-rwx [file]
			* removes read/write/execute permissions from groups/others
			* Leaves user with all control
		* chmod u = rwx, g=rx, o=x [file]
			* gives user r/w/x
			* gives group r/x
			* gives others x
	* Can also use octal (base 8) notation representing each three-bit field with an octal digit.

![](./UNIX/pasted_image001.png)

Random shit
-----------

* to change owner or group for the file:
	* chown, chgrp
* Pager is a command which displays a text file one page at a time (the size of the terminal)
* Touch quickly creates a file.


Command syntax
--------------

* cmd [options] [arguments]
* when specifying options:
	* What follows a dash is the option shorthand
	* what follows two dashes is the option's name
	* ex: -v, --verbose. they work the same, but one denotes the shorthand and the other is the full option name
	* Can be used interchangebly
* an option can range from A-Z, 0-9.


### Command types

* Commands can be:
	* built into the shell (cd, alias, bg, set,...)
	* aliases created by the user or on behalf of the user
* Use the type command to determine if a command is builtin, an alias, or an executable


### Simple Commands

* cat [file1 file2 ...]
	* Will concatenate the files to stdout, in order, separated by \n
* less [file]
	* Will browse through a file, one screen at a time.
* date
	* Will display the current date and time
* wc [file]
	* Will count the number of lines, words, and chars in input and will print them.
* clear
* help [cmd]
	* will display a description of a command
* man [cmd]
	* Will display a long description of a command
* whatis [cmd]
	* Will display a one line description of a command

**Use info, much more useful.**

File I/O
--------
Each UNIX program has access to three I/O streams when ran
stdin, defaults to the console keyboard
stdout, defaults to the console screen
stderr, defaults to the console screen
Shell provides a mechanism for overriding this behaviour (stream redirected)

### Stream Redirection
Redirection allows you to
Take input from a file
Save command output to a file
Redirecting from/to files using bash:
stdin:
cmd < file
less <ls.1
stdout:
cmd > file 		#write
ls -la > dir.listing
cmd >> file   	#append
ls -la [/home](file:///home) >> dir.listing
stderr
cmd 2> file
cmd 2>> file
Redirecting stdin and stdout simultaneously
cmd < infile > outfile
sort < unsorted.data > sorted.data
Redirecting stdout and stderr simul.
cmd >& file
grep 'hello' program.c >& hello.txt
cmd 1 > out.log 2>err.log
UNIX gotchas
symbols used for redirection depend on shell you are using
our work will be with the bash shell
slight differences from C-shells


### Pipes
Pipes are considered by many to be one of the major UNIX-shell innovations
excellent tool for creating power commands from simpler componenents
Pipes route stdout of one command into the stdin of another comm
Without pipes, alot of temp files will be created
Pipes can save time by elim the need for intermediate files
pipes can be arbitrarilt long and complex
All comms are executed concurrently
If any process in the pipe fails, the whole pjpeline fails

### Command sequencing
Multiple commands can be executed sequentially, that is:
date; who; pwd
May group sequenced commands together and redirect
(date; who; pwd) > logfile
note that the last line does not have the same effect as
date; who ;pwd > logfile
	

Console
-------
UNIX needs to know your terminal type
Can be displayed using
echo $TERM
Re-set it to another terminal type using a command like:
export TERM  = vt100
Several keyboard chars have special functions; the command displays a list of special control chars
stty -a
You can use stty to change shit
stty erase ^X
If a command expects input from stdin and no redirection is done, the program will take input from a keyboard
A command that expects input on stdin may appear to "hang", typing ^D signals an end to input
^C will stop the current command
XON/XOFF implement flow control to the console
^S (XOFF)
^Q (XON)


Filename expansion
------------------
shorthand for referencing multiple existing files on a command line

* is wildcard

? exactly one of any char
[abc] any char in set [abc]
[!abc] any char not in set [abc]

Quoting
-------

controls bash's interpretation of certain chars
what if you wanted to pass '>' as an argument to a command
**strong quotes - **All chars inside a pair of single quotes are preserved
**weak quotes - **Some chars ($,`) inside a pair of double quotes are interpreted by shell
**backquotes - **substitute result of eval as a command

Command Aliases
---------------
Allow a string to be substituted for a word when it is used as the first word of a command
Syntax
alias mtcmd = 'cmd [opt] [arg]'
To see a list of existing aliases:
alias
to undo / remove an alias
unalias mycmd


shell variables
---------------
A running shell carries a dictionary variables with values
Environment variables
Can be user defined, mostly built in.
Can be checked using env
use set to display the values of env + shll varaibles
var = value to assign a variable
unset var to delete
You can use the value of a shell variable using the $ prefix
echo $PATH


### Shell scripting
Why write a shell script?
Sometimes it makes sense to wrap a repeated task into a command
Sequences of operations can be placed in a script and exe like a command
	
![](./UNIX/pasted_image002.png)


Path
----
Helps the shell find commands you want to exe
its value is a list of dirs separated by ':'
When we want to run a program, the exe should be in one of those dirs






 



