# Origin & Master
Created Thursday 22 September 2016

**What is with "origin" and "master"?**

* A working copy / local repo may be associated with
	* No remote repo, or
	* **one remote repo** (UVic SENG 265), or
	* Several remote repos
	* A working copy / local repo may have
		* No code branches tracked by git,
		* **one code branch (**Uvic SENG 265) tracked by git, or
		* Several code branches tracked by git
	* By git convention, the default remote repo is named **origin.**
	* By convention, the main branch of code development is named **master** (i.e, the the main branch of code development)
* When using "git pull" and "git push" for repos already cloned
	* origin and master are usually default values
	* "git pull" == "git pull origin master"
	* "git push" = "git push origin master"
* Branching workflows will be discussed later

![](./pasted_image.png)


* This diagram we saw earlier of our commits / snapshot is an example of a branch
* This git convention is that every repo has at least one branch, which is the main branch
	* usually referred to as the master branch
* Above is the master branch


