# Implementing AC Policy
Created Monday 16 October 2017

Matrix (ACM) is possible, but not good (would be wasting memory)

### List Based Implementations of AC Policies

* Access Control Lists (ACL)
* Capability-based Access Control


### ACL

* Instead of using ACM, ACL essentially store each column of ACM with the object it represents.
* Definition: Let S be set of subjects and R the set of rights of a system. An ACL I is a set of pairs
	* I = {(s,r): s∈S, r ⊆ R}
* ACLs can be long ... so combine users
	* UNIX: 3 classes of users: owner, group, other
		* rwx rwx rwx
	* Ownership assigned based on file creation
	* Group set to current group of process
	* Can change it to any other group the user belongs to
	* Windows ACLs are hierarchically inherited

![](./Implementing_AC_Policy/pasted_image.png)

### Advantage of ACLs:
Ease opf revoking privileges
ACLs: Just change ACL that is attached to object
CLs: Scan all C-lists, remove relevant capabilities
Expensive! (potentially impossible for crypto-tickets)
Solution: Use indirection (Locky & Key)
Each object has entry (alias, lock) in a global object table
Names in capabilities name an alias, not the actual object
To revoke, remove the entry in the table ("remove lock")


