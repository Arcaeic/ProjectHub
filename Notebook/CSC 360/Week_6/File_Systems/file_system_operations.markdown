# file system operations
Created Wednesday 28 February 2018
touch
write
read
seek
delete
truncate
cat
...etc

Mount file systems
Mount point
directory
directory with files?
Multi mount
	

### Share and Protection
File attributes
owner: userID, groupID
Type of access
read, write, execute, etc
Share over the network
host ID
NFS: network file system
remote procedure calls
		

### Failure Mode
Local file systems
Hardware failure
corrupted information
Remote file systems
Network failure
Fault-tolerrant systems
Keep state at both sides, or
stateless but less secure
	

### Consistency Control
Multiple users acccess a file simultaneously
lock!
entire file, or
certain records
complexity and (network) overhead
Consistency semantics
Unix semantics: write immediately visible
Session (open()-closE()) semantics: e.g., AFS
connex

