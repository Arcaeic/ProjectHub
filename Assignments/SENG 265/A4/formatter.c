#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "formatter.h"
int fromfile = 0;
int isoff = 0;
int LSassigned = 0;
/*---------------------------------------------------------------------------*/
void mallocerr(){
	fprintf(stderr,"ERROR: Problem allocating memory!\n");
	exit(1);
}
/*---------------------------------------------------------------------------*/
char **format_file(FILE *infile) {
	char *line = NULL;
	int numlines = 0;
	size_t output_len = 0;
	ssize_t readLines;

	char **output = calloc(1,sizeof(char*)*40);
	if(output == NULL){mallocerr();}
	char **result = NULL;

	while((readLines = getline(&line, &output_len, infile)) != -1){
		output = (char**)realloc(output,(numlines+1) * sizeof(char*));
		if(output == NULL){mallocerr();}
		output[numlines] = calloc(readLines+1,sizeof(char));
		if(output[numlines] == NULL){mallocerr();}
		strcpy(output[numlines], line);
		numlines++;
	}
	
	fromfile = 1;
	result = format_lines(output,numlines);
	output = NULL;
	free(output);
	return result;
}
/*---------------------------------------------------------------------------*/
int assign_parameters(char *line_in, int *width, int *margin, int *fmt, int *linespace){
	char *line = (char*)calloc(strlen(line_in)+1,sizeof(char)*40);
	if(line == NULL){mallocerr();}
	strncpy(line,line_in,strlen(line_in)+1);
	char *tok = strtok(line,"\t ");
	char fmt_out[4];  /* holds fmt result */

	/* check width and assign */
	if(!strcmp(tok,".LW")){
		tok = strtok(NULL, " ");	/* get parameter */
		*width = atoi(tok);
		*fmt = 1;
		free(line);
		return 1;
	}

	/* check margin and assign */
	else if(!strcmp(tok,".LM") && *fmt==1){
		tok = strtok(NULL, " ");	/* get parameter */
		if(!strncmp(tok,"+",1)) {
			*margin += atoi(++tok);
		}else if(!strncmp(tok,"-",1)) {
			*margin -= atoi(++tok);
		}else{
			*margin = atoi(tok);
		}
		if (*margin >= *width - 20) {
			*margin = *width - 20;
		}
		free(line);
		return 1;
	}

	/* check format (fmt) and assign */
	else if(!strcmp(tok,".FT")){
		tok = strtok(NULL, " ");	/* get parameter */
		sscanf(tok, "%s", fmt_out);
		if(strcmp(fmt_out,"on")==0){
			*fmt = 1;
			isoff++;
		}
		if(strcmp(fmt_out,"off")==0){
			*fmt = 0;
			isoff++;
		}
		free(line);
		return 1;
	}
	/*Linespacing*/
	else if(!strcmp(tok, ".LS")){
		tok = strtok(NULL, " ");
		*linespace = atoi(tok);
		LSassigned = 1;
		free(line);
		return 1;
	}
	free(line);
	return 0;
}
/*---------------------------------------------------------------------------*/
char **format_lines(char **lines, int num_lines) {
	/*===Formatting Variables========*/
	static int width      = 0;
	static int margin     = 0;
	static int fmt 	      = 0;
	static int linespace  = 0;
	/*===============================*/
	char **result = (char**)calloc(1,sizeof(char*)*40);
	if(result == NULL){
		mallocerr();
	}
	int curr_line = 0;
	int curr_word_len=0;
	int curr_line_len=0;
	int i;
	int mar;
	int spacing;

	for(i = 0; i<num_lines; i++){
		char *token;
		if(!strncmp(lines[i],"\n",1) && fmt==1){
			//For empty lines
			curr_line++;
			result = (char**)realloc(result,(curr_line+1) * sizeof(char*)*40);
			if(result == NULL){mallocerr();}
			result[curr_line] = (char*)calloc(2,sizeof(char));
			if(result[curr_line] == NULL){mallocerr();}
			strcpy(result[curr_line],"\n");
			//Linespacing
			if(linespace != 0) {
				spacing = linespace;
				while(spacing != 0 && i != num_lines -1) {
					strcat(result[curr_line], "\n");
					spacing--;
				}
			}
			LSassigned = 0;
			curr_line_len = width;
			continue;
		}

		if(assign_parameters(lines[i], &width, &margin, &fmt, &linespace)){
			if(isoff==1){
				result = (char**)realloc(result,(curr_line+1) * sizeof(char*)*40);
				if(result == NULL){mallocerr();}
			
				result[curr_line] = (char*)calloc(strlen(lines[i])+1,sizeof(char)*40);
				if(result[curr_line] == NULL){mallocerr();}
				if(fmt==0){
					curr_line++;
					result = (char**)realloc(result,(curr_line+1) * sizeof(char*)*40);
					if(result == NULL){mallocerr();}
			
					result[curr_line] = (char*)calloc(strlen(lines[i])+1,sizeof(char)*40);
					if(result[curr_line] == NULL){mallocerr();}
				}
				isoff=0;
			}
			continue;

		}else if(fmt){
			isoff = 0;
			char *lines_tok = (char*)calloc(strlen(lines[i])+1, sizeof(char)*40);
			if(lines_tok == NULL){mallocerr();}
			//If fromfile/line.
			if(fromfile){
				strncpy(lines_tok,lines[i], strlen(lines[i])-1);
			}else{
				strncpy(lines_tok,lines[i], strlen(lines[i]));
				lines_tok[strlen(lines[i])] = '\0';
			}
			strcat(lines_tok,"\0");
			token=strtok(lines_tok, "\t \n");
			//Processing each word from input.
			while(token){
				curr_word_len = strlen(token);
				//If one word takes up entire line.
				if(width < curr_word_len + margin){
					fprintf(stderr,"ERROR word too big!");
					exit(1);
				}
				//If output_line_len + word_len > width.
				if(curr_line_len + curr_word_len + 1 > width){
					if(fromfile){
						strncat(result[curr_line],"\n",2);
					}
					if(linespace != 0) {
						spacing = linespace;
						while(spacing != 0 && i != num_lines -1) {
							strcat(result[curr_line], "\n");
							spacing--;
						}
					}
					LSassigned = 0;
					curr_line++;
					result = (char**)realloc(result,(curr_line+1)*sizeof(char*)*40);
					if(result == NULL){mallocerr();}
					curr_line_len = 0;

				}else if(curr_line_len !=0){
					strncat(result[curr_line]," ",1);
					curr_line_len++;
				}

				if(curr_line_len==0){
					result[curr_line] =(char*)calloc(width+2, sizeof(char)*40);
					if(result[curr_line] == NULL){mallocerr();}
					for(mar=0;mar<margin;mar++){
						strcat(result[curr_line], " ");
						curr_line_len++;
					}
				}
				strncat(result[curr_line], token,curr_word_len);
				curr_line_len += curr_word_len;
				token = strtok(NULL, " ");
			}

		}else{
			isoff=0;
			if(curr_line_len < width){
				strncat(result[curr_line],"\n",2);
				curr_line++;
			}
			
			result = (char**)realloc(result,(curr_line+1) * sizeof(char*)*40);
			if(result == NULL){mallocerr();}
			
			result[curr_line] = (char*)calloc(width+2,sizeof(char)*40);
			if(result[curr_line] == NULL){mallocerr();}
			
			strncpy(result[curr_line], lines[i],strlen(lines[i])+1);
			curr_line++;
			curr_line_len = width+1;
			continue;
		}
	}
	if(fmt){
		curr_line++;
		if(fromfile){
			result = (char**)realloc(result,(curr_line+1) * sizeof(char*)*40);
			if(result == NULL){mallocerr();}
			result[curr_line] = (char*)calloc(2,sizeof(char));
			if(result[curr_line] == NULL){mallocerr();}
			strcpy(result[curr_line],"\n");
			curr_line++;
			result[curr_line] = NULL;
		}
	}
	result[curr_line] = NULL;
	return result;
}