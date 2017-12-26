# Infection
Created Monday 16 October 2017

Note: % escape chars replaced for readability
![](./Infection/pasted_image.png)
Downloaded script 'listen' executes under same permission as Web server process
SE Linux systems with MAC were immune to attack

### Regular UNIX
In linux: a process run under a certain uid can do anything that this uid is allowed to do
Therefore, if a user runs a game, this game may also delete fiels, disclose data over the network etc.
Default: allow

### SE Linux with MAC
In SE Linux: privileges to processes must be granted explicitly
Makes for large access policies
Default: Deny

#### SE Linux Modes
SE Linux has three modes
Enforcing (prevent anything not allowed)
Permissive (allow but log violations)
Disabled
	
![](./Infection/pasted_image001.png)

![](./Infection/pasted_image002.png)

![](./Infection/pasted_image003.png)

![](./Infection/pasted_image004.png)




