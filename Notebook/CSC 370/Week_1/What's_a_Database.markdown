# What's a Database
Created Thursday 07 September 2017


* Database (DB): A collection of information that exists over a long period of time.
* Database Management Systen (DBMS): A complex software for handling
	* Large data efficiently and safety



#### The Database Management System

1. Allows users to easily create new databases and specify their scema
2. Enables users to easily query and modify the data using a high level language.
3. Supports **intelligent storage** of large amounts of data
	* Protects data from **accidental** or **not proper use.**
		* Ex: We can require from the DBMS to not allow the insertion of two different employees with the same SIn
	* Allows **efficient access** to the data for queries and modifications
		* Ex: Indexes over specified fields
4. Controls **access to data** from **many users at once** (concurrency), without "bad" interactions that can corrupt the data accidentally
5. Recovers from **software failures** and **crashes**


#### Early DB systems and file system

* First commerical db systems evolved from **file systems**
	* File systems allow the soitrage of big amounts of data
* But, **no** query language for the data in files.
	* Needed to write programs for extracting even the most elementary infomration from a set of files
	* Ex: Suppose we have stored in a file called *Employees* records having the field:

(emp_code, name, dept_code)
and in a nother file called *Departments" *having the fields
	
In the absense of SQL we have to wrtie a program which will:

1. Open the file *EMployees*
2. Declare a variable of the same type as the crecords stored in the file
3. Scan the file:
	a. while the end of the file is not yet encountered, assign the current record to above variable.
4. If the value of the name firled is "Smith" then get the value of the dept_code. Suppose its "100"
5. Search in a similar way for a record with "100" fdor the deot)code in the *Department* file.
6. Print the dept_name when successfully finding the dept_code


It's painful.

In SQL you can write:
	SELECT dept_name
	FROM Employees, Department
	WHERE dept_code = 100
`


