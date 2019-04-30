# Constants, strings, to be used in all part of the
# CSC 350 (Spring 2019) A#2 submissions

# These are similar to #define statements in a C program.
# However, the .eqv directions *cannot* include arithmetic.

.eqv  MAX_WORD_LEN 32
.eqv  MAX_WORD_LEN_SHIFT 5
.eqv  MAX_NUM_WORDS 100
.eqv  WORD_ARRAY_SIZE 3200  # MAX_WORD_LEN * MAX_NUM_WORDS
.eqv NEW_LINE_ASCII 10


# Global data

.data
WORD_ARRAY: 	.space WORD_ARRAY_SIZE
NUM_WORDS: 	.space 4
MESSAGE1:	.asciiz "Number of words in string array: "
MESSAGE2:	.asciiz "Contents of string arrayr:\n"
MESSAGE3:	.asciiz "Enter strings (blank string indicates end):\n"
SPACE:		.asciiz " "
NEW_LINE:	.asciiz "\n"
EMPTY_LINE:	.asciiz ""

# For strcmp testing...
MESSAGE_A:	.asciiz "Enter first word: "
MESSAGE_B:	.asciiz "Enter second word: "
BUFFER_A:	.space MAX_WORD_LEN
BUFFER_B:	.space MAX_WORD_LEN


#
# Driver code.
#

.text

START:	
	# Read in the first word...
	la $a0, MESSAGE_A
	li $v0, 4
	syscall
	la $a0, BUFFER_A
	li $a1, MAX_WORD_LEN
	li $v0, 8
	syscall
	
	# Read in the second word...
	la $a0, MESSAGE_B
	li $v0, 4
	syscall
	la $a0, BUFFER_B
	li $a1, MAX_WORD_LEN
	li $v0, 8
	syscall
	
	la $a0, BUFFER_A
	la $a1, BUFFER_B
	jal FUNCTION_STRCMP
	
	add $a0, $zero, $v0
	li $v0, 1
	syscall
	
EXIT:
	li $v0, 10
	syscall

##########################################################
#
# YOUR SOLUTION MAY NOT ADD MORE GLOBAL DATA OR CONSTANTS.
# ALL OF THE CODE FOR YOUR FUNCTION(S) MUST APPEAR AFTER
# THIS POINT. SUBMISSIONS THAT IGNORE THIS REQUIREMENT
# MAY NOT BE ACCEPTED FOR EVALUATION.
#
##########################################################

#
# Solution for FUNCTION_STRCMP must appear below.
#
# $a0 contains the address of the first string
# $a1 contains the address of the second string
# $v0 will contain the result of the function.
#

FUNCTION_STRCMP:
# Registers Used:
# 	$a0, $a1: Array addresses
# 	$t0, $t1: Character registers
# 	$s0: Comparer register

#	Save Registers
	addiu $sp, $sp, -24
	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $t0, 8($sp)
	sw $t1, 12($sp)
	sw $s0, 16($sp)
	sw $ra, 20($sp)
	
#	Start assuming both strings are equal
	add $v0, $zero, $zero
	
#Load Characters
LOAD:
	lbu $t0, 0($a0)
	lbu $t1, 0($a1)
	
#Compare characters
COMPARE:
	slt $s0, $t0, $t1
	bne $s0, $zero, LESS
	sgt $s0, $t0, $t1
	bne $s0, $zero, GREATER

#Check for end of string
NULLCHECK:
	add $s0, $t0, $t1
	beq $s0, $zero, EXIT_STRCMP 
	beq $t0, $zero, LESS
	beq $t1, $zero, GREATER
	
#	Increment iterators and loop
	addi $a0, $a0, 1
	addi $a1, $a1, 1
	j LOAD

# $t0 comes before $t1
LESS:
	li $v0, -1
	j EXIT_STRCMP
	
# $t1 comes before $t0
GREATER:
	li $v0, 1
	j EXIT_STRCMP

EXIT_STRCMP:
	lw $ra, 20($sp)
	lw $s0, 16($sp)
	lw $t1, 12($sp)
	lw $t0, 8($sp)
	lw $a1, 4($sp)
	lw $a0, 0($sp)
	addiu $sp, $sp, 24
	
   	jr $ra
