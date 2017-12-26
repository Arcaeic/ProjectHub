# Privileges
Created Thursday 16 November 2017

#### role-based authorization
Privileges can be assigned to roles
Roles can then be granted to users and other roles
Roles relfect how real organizations work

#### Types of Privileges:
**Select** (can read all columns)
**Insert(<column-name>)** can insert tuples with non-null or nondefault values into column
**Insert **can insert tuples into all columns in the table
**Delete: **can delete tuples
**References: **Can define foreign keys that refer to this column
If you want the recipients to be able to grant these rights to others, use **WITH GRANT OPTION**

![](./Intro_to_DB_Sec/pasted_image002.png)

