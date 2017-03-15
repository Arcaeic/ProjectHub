# Commit
Created Thursday 22 September 2016

**Concept: Commit**


* Suppose you want to make a change to **button.c**
	* You edit the file using your normal workflow
	* Time and date on edited file will be more recent than time on remote repo
	* Changes are published by committing your changed file to your local repo
* We first stage our changes (**add)**
	* then make a permanent record of the change (**commit)**
* note that commits always take place in local repo
	* take place within our working copy
* We can have as many commits as we work through **sets of changes**



* **Obervations:**
	* Commits can be frequent (if needed)
	* Pushes are much rarer, could be reflected that our work has reached some staged where it can be usable by clients
	* Writing each assignments will require several commits
		* but submitting an assignment will require a push


* Each commit results in a new **snapshot** of our code in our working copy (such as the end of a day, or a huge change that heavily changes how the program works)
	* Snapshots are kept reverse-chronologically
	* "git log" produces this list
	* "git status" reports the relationship amongst files in our working dir with what is within git's local repo
		* What has changed in our working copy
* COMMIT != PUSH!
	* commits are made to local repo, versus push, which writes to remote
	* This separates the tracking of file/dir changes from storage on remote servers


