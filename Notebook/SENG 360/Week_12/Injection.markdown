# Injection
Created Wednesday 29 November 2017

Injection vulnerabilities are present if the application may accept data input and interpret it as program code (fragments)

#### What is SQL Injection
The ability to inject SQL commands into the DB engine through an existing applicaiton

#### What is SQL:
**Structured Query Language**

* Allows us to access a DB
* ANSI and ISO standard computer language
	* Currently SQL99
* Can execute queries, retrieve data, insert delete and updates records


#### SQL (Data Manipulation Language) (DML)
SQL includes a syntax to update, insert, and deleting records

* SELECT
* UPDATE
* INSERT INTO
* DELETE


#### SQL (Data Definition Language) (DDL)
The DDL part of SQL includes

* Creating and deleting tables
	* CREATE TABLE
	* ALTER TABLE
	* DROP TABLE
* Defining indexes
* Specify links between tables
* Constraints


#### MetaData
Almost all SQL DBs are based on RDBMS
Important fact for SQL Injection:
Amongst Codd's 12 rules for a Truly RDBM:
Metadata (data about the DB) must be stored in the DB just as regular data is
Therefore, DB structure can also be read and altered with queries

![](./Injection/pasted_image001.png)

#### 1) Input Validation
Vulnerabilities can be everywhere, we check all entry points
Fields in web forms
Script parameters in URL query strings
Values stored in cookies or hidden fields
By "fuzzing" we insert into every one:
Character sequence: ' " ) # || + >
SQL reserved words with white space delimiters
	
#### 2) Information Gathering
Attackers try to find out:
a) Output mechanism
b) Understanding the query
c) Determining DB type
d) Find out user privilege level
e) Determine OS interaction level


##### a) Output Mechanism

1. Using query result sets in the web app
2. Error messages
	a. Craft SQL queries that generate specific error messages with valuable info in them
3. Blind SQL injection
	a. Does not rely on output channel for query results or error messages
	b. Use time delays or conditional behaviour to extract information
	c. Slower, more difficult
4. Other mechanisms:
	a. email, SMB, FTP, TFTP

 

