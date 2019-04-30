# OS Services
Created Tuesday 09 January 2018

User/programmer interfaces
CLI, GUI, API, system calls
OS in a (web) browser?
Program Execution
I/O Operation
File Manipulation
Process communication
Error handling: software/hardware error

Resource allocation and arbitration
CPU, mem, storage, I/O
Resource sharing and protection
among processes, users, computers
authentication, authorization, accounting
different insterfaces to these services
regular user, application programmer, system programmer, system designer


### CLI (Command Line Interface)
e.g Microsoft DOS: \command.com
Linux: [/bin/bash](file:///bin/bash)

Interactivity: interpreter
Implementation
Internal: dir (DOS), cd (DOS/UNIX)
External: ls (UNIX)
Programmability: shell script

### GUI (Graphical User interface)
e.g
Microsoft Windows
K Desktop Evironment (KDE)
Interactivity: point-and-click, drag-and-drop
Implenentation
integrated with OS
or OS front-end
Programmability: e.g., [AutoIt](./AutoIt.markdown)

### API (Application Programming Interface)
e.g Win32 API, POSIX Api, Java API
API: Another layer of abstraction
mostly OS-independant
higher level of functionality
examples:
Copy the content of file A to file B
Using C lib:
fopen("/path/to/a", "r");
fopen("/path/to/b", "w");
fread() & fwrite()
formatted I/O: element size, # of elements
buffered I/O: streams
fclose()
		
#### API Flows
![](./OS_Services/pasted_image003.png)



### System Calls
Primitive interfaces to OS services
System call categories
Process control
fork, exec*, wait, kill, signal, exit, etc
file/device manipulation
creat[e], open, read, write, lseek, close, etc
socket, bind, listen, accept, connect, etc
Information manipulation
time, getpid, getgid, gethostname, etc
	

#### System Call Examples
![](./OS_Services/pasted_image.png)


#### System Call Inplementation
![](./OS_Services/pasted_image001.png)

#### System Call Flows
![](./OS_Services/pasted_image002.png)





