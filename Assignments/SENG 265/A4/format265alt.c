#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "formatter.h"
/*---------------------------------------------------------------------------*/
/*===========================================================================*/


/*===CHECK_ARGS==============================================================*/
void check_args(int argnum, char** input){
	if(argnum !=2){
		fprintf(stderr,"Invalid inital parameters\n");
		exit(1);
	}
}
/*===MAIN====================================================================*/
int main(int argc, char *argv[]) {
	/*===DECLARE INITIAL STRINGS=====*/
	char *lines_in[512];
	char **lines;
	char **formatted_lines;
	FILE *input_file;	
	if(argc==1){ /* read from stdin */
		fgets(*lines_in, 512, stdin);
		formatted_lines = format_lines(lines_in, 1);
	}else{
		check_args(argc,argv); /* CHECK ARG INPUT */

		/*===OPEN INPUT FILE=============*/
		input_file = fopen(argv[1],"r");
		if(input_file == NULL){
			fprintf(stderr,"Cannot open file @ %s\n", argv[1]);
			exit(1);
		}

		/*===FORMAT INPUT LINES FROM FILE*/
		formatted_lines = format_file(input_file);
		if(formatted_lines == NULL){
			fprintf(stderr,"Error: File \"%s\" Returned no lines \n",argv[1]);
			exit(1);
		}
	}

	/*===PRINT FORMATTED RESULT======*/
	for(lines = formatted_lines; *lines != NULL; lines++){
		fprintf(stdout,"%s",*lines);
	}
	fclose(input_file);
	free(formatted_lines);
	free(lines);
	exit(0);
}