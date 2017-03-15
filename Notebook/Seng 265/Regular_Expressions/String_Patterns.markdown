# • String Patterns

* Created Thursday 27 October 2016



* We all use searches where we provide strings or substring to some module or mechanism
	* Google search terms
	* Filename completion
	* command-line wildcards
	* Browser URL completion
	* Python string routines, find(), index(), etc
* Quite often these searches are simply expressed as a particular pattern
	* An individual word
	* Several words where some are strictly required while some are not
	* The start or end if particular words — or perhaps just the string appearing with a larger string
* This works well if strings follow the format we expect
* Sometimes, we want to express a complex pattern
	* The set of all files ending with either ".c" or ".h"
	* The set of all files starting with "ical"
	* The set of all strginfs in which "FREQ" appears as a string (but not "FREUENCY", "INFREQUENT", but "fReQ" is fine
	* The set of all strings containing dates in MM/DD/YYYY format
* Such a variety of patterns used to require language-specific operations:
	* SNOBOL
	* Pascal
* More troubling was that most non-trivial patterns required several lines of code to express (i.e a series of if-then-else statements)
	* This is a problem as the resulting code can obscure the patterns we are searching
	* Changing the pattern is tedious and error-prone

	

