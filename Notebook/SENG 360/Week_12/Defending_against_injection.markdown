# Defending against injection
Created Wednesday 29 November 2017

Enforce strong design in new applications
You should audit your existing websites and source code
All programming languages and all SQL databases are potentially vulnerable
Protecting against it requires
strong design
correct input validation
hardening


#### Input Validation
Define data types for each field
Implemnent stringent "allow only good" filters
If the input is supposed to be numeric, use a numeric variable in your script to store it
Reject bad input rather than attempting to escape or modify it
IKmplement stringent "known bad" filters
e.g: Reject "selecT", "insert", "drop", etc


#### Strong Design
Define an easy "secure" path to querying data
Use stored procedures for interacting with the DB
Call stored procedures through a parameterized API
Validate all input through generic routines
Use principle of "least privilege"
Define several roles, one for each kind of query


#### Harden Server

1. Run DB as low-privilege
2. Remove unused stored procedures and functiona;lity, or restrict access to admins
3. change permissions and remove "public" access to system objects
4. audit password strength of all users
5. Remove pre-auth linked server
6. Remove unsused network protocols
7. Firewall the server


#### Detection and Dissuasion
React tpo SQL injection attacks:
Log the attempts
Sending email alerts
Block offending IP
Send back intimidating error messages
	
	

#### Evasion Techniques

**IDS Signature Evasion**
Evading ' OR 1=1 signature
everything similar to this as well
Some people use PHP addslashes function to escape characters 
single quote
double quote
backslash
NUL
IDS and input validation can be circumvented by encoding
URL
Unicode
Hex encoding
char() function
![](./Defending_against_injection/pasted_image.png)


![](./Defending_against_injection/pasted_image001.png)

![](./Defending_against_injection/pasted_image002.png)

![](./Defending_against_injection/pasted_image003.png)

![](./Defending_against_injection/pasted_image004.png)





