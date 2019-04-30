#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <readline/readline.h>
#include <readline/history.h>
#include <fcntl.h>

#define TRUE 1
#define FALSE 0

/*	--	Linked List Data Structure	--	*/

typedef struct process {
	pid_t pid;
	char* args;
	struct process* next;
	struct process* prev;
} process;

/*	--	Global Variables	--	*/

process* listHead;
int bufferSize = 512;
char prompt[1025]; 
char cwd[1025];
int numArgs = 0;

/*	--	Linked-List Functions	--	*/

char* aggregateArgs(char** tokens) {

	char* args = malloc(bufferSize * sizeof(char));
	allocNullCheck(args);
	int i = 1;

	for(i; i < numArgs+1; i++) {

		if( i + 1 > bufferSize) {
			reallocArray(&args);
		}

		strcat(args, tokens[i]);
		if(i == numArgs) {
			continue;
		}

		strcat(args, " ");
	}

	strcat(args, "\0");

	return args;
}

void newProcessNode(pid_t pid, char** tokens) {

	process* node = (process*) malloc(3*sizeof(process));
	node -> pid = pid;
	node -> args = aggregateArgs(tokens);
	node -> next = NULL;
	node -> prev = NULL;

	if(listHead == NULL) {
		listHead = node;
	
	} else {
		process* curr = listHead;
		while(curr->next != NULL) {
			curr = curr -> next;
		}

		curr -> next = node;
		node -> prev = curr;


	}

}

int doesProcessExist(pid_t pid) {

	process* curr = listHead;
	while(curr != NULL) {

		if(curr->pid == pid) {
			return TRUE;
		}
		curr = curr->next;
	}

	return FALSE;

}

void removeProcessNode(pid_t pid) {

	if(!doesProcessExist(pid)) {
		return;
	}

	process* curr = listHead;
	while(curr != NULL) {

		if(curr->pid == pid) {

			if (curr == listHead) {
				listHead = listHead -> next;
			} else {
			
				if(curr->prev != NULL) {
					curr -> prev -> next = curr -> next;
				}
				if(curr->next != NULL) {
					curr -> next -> prev = curr -> prev;
				}
			}

			curr->args = NULL;
			free(curr->args);
			free(curr);
			return;
		}


		curr = curr->next;
	}

}

process* getProcessFromList(pid_t pid) {

	process* curr = listHead;
	while(curr != NULL) {
		if(curr->pid == pid) {
			return curr;
		}
		curr = curr->next;
	}

	return NULL;

}

/*	--	Helper Functions	--	*/

void allocNullCheck(char** arr) {

	if(arr == NULL) {
		printf("\tERROR: Could not allocate memory for array.\n");
		exit(1);
	}

}

void reallocArray(char** arr) {

	bufferSize = bufferSize * 2;
	arr = realloc(arr, bufferSize * sizeof(char*));
	allocNullCheck(arr);

}

int validSystemFunction(char* command, char* input) {
	if(strcmp(command, input) == 0) {
		return TRUE;
	}
	return FALSE;
}

/* --	Main Functions	-- */

void getPrompt() {

	strcpy(prompt, "SSI: ");

	if(getcwd(cwd, sizeof(cwd)) == NULL) {
		printf("\tError: Could not change directory.\n");
		exit(1);
	}

	strcat(prompt, cwd);
	strcat(prompt, " > ");

}

/* checkProcesses
* Determines if processes were started or have been terminated.
* Runs every time the user passes in an input.
*/
void checkProcesses() {
	pid_t pid;
	int status;
	while(TRUE) {

		pid = waitpid(-1, &status, WNOHANG);
		if (pid > 0) {
			
			process* proc = getProcessFromList(pid);
			if (WIFEXITED(status)) {
				printf("\tProcess %d (%s) was terminated.\n", pid, proc->args);
				removeProcessNode(pid);
			}
			if (WIFSIGNALED(status)) {
				printf("\tProcess %d (%s) was stopped.\n", pid, proc->args);
				removeProcessNode(pid);
			}

		} else {
			break;
		}

	}
}

/*	runCommand
* Attempts to run the command in a different process. This is done
* using fork() and execvp().
*
*@param command The command passed, or tokens[0]
*@param tokens The tokens from stdin.
*/
void runCommand(char* command, char** tokens) {

	int pid, endid;
	pid = fork();

	if(pid == 0) { 	//child
		if(execvp(command, tokens) < 0) {
			printf("\tUnrecognized command: [%s]\n", command);
		}
		exit(1);
	} else if (pid > 0) 	{	//parent
		endid = wait(NULL);
	} else {	//error
		printf("\tError: Failed to fork child process.\n");
	}

}

void dirChange(char** args) {

	if(numArgs > 1) { 
		printf("cd:\tUnknown symbol: %s\n", args[2]);
		return;
	}

	int status;

	char* homeDir = getenv("HOME");
	if(homeDir == NULL) {
		printf("\tError (getenv/chDir): Could not find home directory.\n");
		return;
	}
	if(numArgs == 0) {
		status = chdir(homeDir);
	}
	else if(strcmp(args[1], "~") == 0) {
		status = chdir(homeDir);
	} else {
		status = chdir(args[1]);
	}
	if(status < 0) {
		printf("\tError (chDir): Could not change directory.\n");
	}
	
	getPrompt();
}

void bglist(){

	int count = 0;
	process* curr = listHead;
	printf("\n");
	while(curr != NULL) {
		count++;
		printf("%d:\t %s\n", curr->pid, curr->args);
		curr = curr->next;
	}
	printf("Total background jobs: %d\n\n", count);

}	

/* bg
* Forks a child process, creates a linked list node for the process,
* which stores the processID, and the passed arguments.
*
* @param tokens	The tokens retrieved from stdin
*/
void bg(char** tokens) {

	char* command = tokens[1];
	int status;

	if(command == NULL) {
		printf("\tError (bg): No command specified.\n");
		return;
	} 

	pid_t pid = fork();

	if (pid == 0) {		//child
		if(execvp(command, &tokens[1]) < 0) {
			printf("\tUnrecognized command: [%s]\n", command);
			kill(pid, 0);
		}
		exit(0);

	} else if (pid > 0) {	//parent
		waitpid(pid, &status, WNOHANG);
		printf("\tStarted process %s (%d) in the background\n", command, (int) pid);
		newProcessNode(pid, tokens);
		sleep(1);	
		return;

	} else {	//error
		printf("Error (fork): Unable to fork new child process.\n");
		exit(1);
	}
}

/* parseCommand
* Looks at the first element in tokens, or the command requested.
* Compares it to "bg", "bglist", and "cd". If it doesn't match any,
* It will attempt to run it via runCommand.
*
* @param tokens	The tokens grabbed from stdin.
*/
void parseCommand(char** tokens) {

	if(numArgs == -1) {
		return;
	}

	char* command = tokens[0];

	if (validSystemFunction(command, "bg")) {
		bg(tokens);

	} else if (validSystemFunction(command, "bglist")) {
		bglist();

	} else if (validSystemFunction(command, "cd")) {
		dirChange(tokens);

	} else if (validSystemFunction(command, "exit")) {
		printf("Exiting...\n");
		exit(0);

	} else {
		runCommand(command, tokens);
	}
}

/*	tokenizeLine:
* Splits the input into individual tokens, then stores the array as a 
* global variable.
*
* @param reply 	The inputted text from stdin.
* @returns tokens	An array of tokens from reply. 
*/
char** tokenizeLine(char* reply) { 

	char **tokens = malloc(bufferSize * sizeof(char*));
	allocNullCheck(tokens);
	int i = 0;

	tokens[i] = strtok(reply, " ");

	while(tokens[i] != NULL) {
		i++;
		if (i + 1 >= bufferSize) {
			reallocArray(tokens);
		}

		tokens[i] = strtok(NULL, " ");
	}

	numArgs = i-1;
	return tokens;

}

void startCLI() {

	getPrompt();
	while (TRUE) {
		checkProcesses();
		char* reply = readline(prompt);	
		char** tokens = tokenizeLine(reply);
		parseCommand(tokens);
		
		free(reply);
		free(tokens);	

	}

}	

void main() {
	startCLI();
}