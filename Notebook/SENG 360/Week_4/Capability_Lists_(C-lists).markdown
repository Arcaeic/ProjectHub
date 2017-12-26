# Capability Lists (C-lists)
Created Monday 16 October 2017

![](./Capability_Lists_(C-lists)/pasted_image.png)

### Differences b/w ACLs and Capabilities

* Where are access rights stored
	* ACL: Each resource (file_ has an access list
	* Capabilities: Each process has a capability list (C-list)
* Capabilities act as a ticket
	* Possession of a capability implies access rights
* Tickets must be unforgeable an uncopyable
	* Otherwise access control fails


### Implementation of Capabilities

* Tags / descriptors
	* Each process has a list of tickets
		* Tickets stored in a protected segment
		* Programs refer to tikets by pointers / indices
		* OS can add [/](file:///) remove tickets
	* E.g UNIX file descriptors
		* UNIX access control a hybrid system: use ACLs to open a file and get a file descriptor, then use fd as a capability
		* More efficient since only have to check permissions on open
* Cryptographic tickets
	* Associate with each capability a cryptographic checksum enciphered using a key known to OS
	* When process presents capability, OS validates checksum
	* Prevents forging of capabilities


### Advantage of Capabilities
More secure (Principle of least privilege)
Ex:
Assume you want to make a particular file available only to specific subjects
With ACLs: Subjects will know that a file ecists, even if they cannot look inside
With CLs: Subjects not given the capability to 'see' a file may not even know of its existence


