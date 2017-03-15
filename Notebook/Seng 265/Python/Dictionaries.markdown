# Dictionaries
Created Friday 14 October 2016


* A mapping type
* Dictionaries store a mapping between a set of keys and a set of values
	* Keys can be any immutable type
	* Values can be ANY time
	* A single dictionary can store values of different types
* You can define, modify, view, lookup and delete key-value pairs in the dictionary

d = {'user' : 'bozo', 'pswd' : 1234}
keys: user & pswd
values: 'bozo' & '1234'
d['user'] = 'clown'
keys: user & pswd
values: 'clown' & '1234'

* Keys must be unique
* assigning to an existing key replaces its value.
	* Assigning to an undeclared keys creates the key and assigns the value to it.
* Removing dictionary entries:

![](./Dictionaries/pasted_image.png)

* d.keys() returns the list of keys in d
* d.values() returns list of values in d
* d.items() returns list of item tuples


