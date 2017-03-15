# • Lab4 Notes

* Created Tuesday 04 October 2016



* *How C handles strings
* *Learn to read arguments from the command line
* *String function/practice



* ARRAYS IN C
* -----------------
* C arrays are basically a pointer to a continuous block of memory



* Declaring an array:
	* int my_array[SIZE];	//An int array.
	* int* my_array;		//Means you are allocating the memory yourself.
				* dont try until November.
* Storing things into the array:
	* my_array[i] = 12;
	* my_array = {1,2,3,4,5,6,7,8,9,0};
* Beware: C will let you store past the end of the array without complaining, but will give you a weird output.



* STRINGS IN C
* -----------------
* C Represents strings an array of single characters. The type of the array is       "char *"



* in order to store "hello":
	* char* my_Array [6] = "Hello";
* The size of the array must be one character more than the string, in order to store the null char.



* You can also declare strings this way:
	* char* my_other_string = "I'm not.";
* Where it will the size of the array is created during compile time. (+1 for null character)



* The strlen() function gets the length of a string by measuring how many characters are before the NULL character at the end
* Usage: int i = strlen(my_string);
* In order to use strlen and other string functions, you must include string.h



* COMMAND-LINE ARGUMENTS
* --------------------------------
* argv[] is an array of strings, one for each word or quoted phrase that was typed. argv[0] always has the program's name! WHICH MAKES SENSE. This allows you to keep track of what the user typed, and USE IT IN YOUR ASSIGNMENT FOR EXAMPLE.



* argc keeps track of argv[]'s length



* ex:
	* ./my_program ninjas moreninjas
	* argv[0] will be "./my_program"
	* argv[1] will be "ninjas"
	* argv[2] will be "moreninjas"
	* argc will be: 3



* COPYING STRINGS
* ------------------
* The strncpy() functio ncopies part of one string into another string variable
* Usage: strncpy(char* destination_string, char* source_string, int num_chars_to_copy



* The thid argument, the number of chars to copy, is so you can keep form copying more chars than the destination string can fit.



* Keeping track of whether the destination has enough storage space to receive the copy is up to you.



* If the number of characters to copy isnt enough to copy the entire source string, INCLUDING THE NULL, then a null wont be copied. The null char acts as a end cap.



* If you try to print a string without the NULL end cap, the computer will print whatever comes after the string, in memory, until it finds something else.



* CONCATENATING STRINGS
* --------------------------------



* strncat() is a function that copies part of one string onto the end of another.
* Usage: strncat(char* destination_string, char* source_string, int num_chars to copy)



* Unlike strncpy(), strncat() always properly caps the resulting string with a null.



* it's up to you to make sure you have enough space in the destination string's storage.



* STRING PRACTICE
* ------------------



* When you need to know how big a total string needs to be to concatenate it: consider strlen(string1) + strlen(string2), and vice versa.



* If you attempt to tokenize a string and try to concatenate it with another, it will only concatenate the first tok+string2.



* STRING TEXT PROCESSING
* ----------------------------



* The function strtok() splits a string into 'tokens'- substrings divided where one or more seperator characters apear in the original string.
* Usage: strtok(char* string_to_tokenize, string_with_separators);



* then,
* strtok(NULL, string_with_separators);



* If you dont make a second call that specifies NULL, then it will loop.


 

