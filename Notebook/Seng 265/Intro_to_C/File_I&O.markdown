# File I&O
Created Monday 03 October 2016


* C, like most languages, provides facilities for reading and writing files
* files are accessed as streams using FILE objects
* the fopen() function is used to open a file; it returns a pointer to info about the file being opened

FILE *data = fopen("input.txt", "r");

* Streams FILE *stdin, FILE *stdout, and FILE *stderr are automatically opened by the O/S when a program starts.
* open modes (text): "r" for reading, "w" for writing, and "a" for appending
* open modes (binary): "rb" for reading, "wb" for writing, and "ab" for appending
* the fclose() function is used to cose a file and flush any associated buffers
* use fgetc() to read a single char from an open file (file was opened in "r" mode)
* similarly, fputc() will output a single character to the open file (file was opened in "w" mode)



* FILE *fopen(char *filename, char *mode)
	* open file corresponding to filename
	* mode can be "r" or "rw" or "rw+" (depending on flavour of Unix)
	* if an error occurs when opening file, function returns 0 (NULL)
* char *fgets(char *buf, int n, FILE *stream)
	* read at most n-1 characters from stream and copy to location buf;
	* input terminates when newline encountered or n-1 characters input.
	* Appends a null character to end of buffer.
	* returns NULL if error or end-of-file encountered
	* set stream to stdin to accept input from standard input
* int scanf( char *format, […] )
	* read formatted data from standard input
	* returns EOF when end-of-file encountered, otherwise it returns the
	* number of fields successfully converted
	* format specifiers encoded in format (variable # of arguments)



