# OS Design
Created Friday 12 January 2018

An art of balance
hardware vs software
efficiency vs flexibility
user vs system
convinience vs effectiveness
General design guidelines
Separation of mechanisms and policies
Best current practices

#### Simple Structure
e.g MS-DOS
Single user
Almost single process
direct access
almost flat memory
MZ linked list
execs
.COM: segment limit
.EXE: MZ file magic

##### MS-DOS
Load Program
"shrink" interpreter
make room for program
Execute program
Access to everything
incl. kernel/interpreter
Reload interpreter bad
otherwise, "cannot find command.com..."


### Layered Structure
![](./OS_Design/pasted_image.png)
OS Layers
L~0~: Hardware
L~N~: UI
L~I~:Anything b/w
use L~i-1~ service
offer service to L~u+1~
Divide and conquer, but some cross-layer issues


#### UNIX
![](./OS_Design/pasted_image001.png)




