# Lab 7
Created Friday 04 November 2016

Object Oriented Programming (OOP)
---------------------------------
What:
Programming style where there are objects that contain data called attri	butes that can be accessed using methods

Java is an example of OOL.
Python Classes:
Python can be programmed as a scripting language (what we've been doing		 thus far) and as object oriented
Function variables (attributes) and functions(method) are accessible by 	outside programs
Namespacing:
Your function names, variables, etc only exist in your class namespace
Careful! Global variables are shared between instances


CLASSES
-------
Python class is defined as:
class objectName (BaseClass)
If you dont have a base class you want to inherit, then this will be obj	ect

A class will contain:
self
This variable represents the object itself (instance)
You can access methods and attributes internally
The first argument for EVERY method in a class is self
__init__(self, args_
Thing of this like the main() function, and is called when you create an object using your class
Any other function you would like

ARGUMENTS
---------
Required arguments:
These are arguments required by the class/method
def f(self, arg_one, arg_two):
NOTICE: Self is a required argument passed
Optional arguments:
These are argumentsthat have default values that are instead use			def f(self, default_str="some_string")
This function can be called as class.f()
You can override the default values
class.f(default_str ="some other string")
You can also pass arguments as a list 
def f(self, *args):
Or you can also pass arguments as a dictionary
def f(self, **kwargs)


IMPORTING & NAMING
------------------
To import a class you made, you need to import the filename(AKA namespace)
You can import a folder of classes!
Create an empty file called __init__.py in the folder
Ex: If i had a folder called lib with class file called util.py, 		it can be imported by:
import lib.util
from lib import util
Private variables do not officially exist in Python
Private variables are denoted using a single whitespace
_stuff

