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

	
	# Perform the swap
	la $a0, BUFFER_A
	la $a1, BUFFER_B
	li $a2, MAX_WORD_LEN
	jal FUNCTION_SWAP
	
	# Print string in BUFFER_A, BUFFER_B
	la $a0, BUFFER_A
	li $v0, 4
	syscall
	la $a0, NEW_LINE
	li $v0, 4
	syscall
	la $a0, BUFFER_B
	li $v0, 4
	syscall
	la $a0, NEW_LINE
	li $v0, 4
	syscall
	
	# Get outta here!	
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
# $a0 contains the address of the first string array
# $a1 contains the address of the second string array
# $a2 contains the maximum length of the arrays
# 
	
FUNCTION_SWAP:
# Registers Used:
# 	$s0: Current location in first array
# 	$s1: Current location in second array
# 	$s2: Base location of temp array
# 	$s3: Current location in temp array
# 	$t0: Char register (for loading/storing)

#	Save registers
	addiu $sp, $sp, -24
	sw $s0, 0($sp)
	sw $s1, 4($sp)
	sw $s2, 8($sp)
	sw $s3, 12($sp)
	sw $t0, 16($sp)
	sw $ra, 20($sp)
#	Move base array addresses to saved registers
	move $s0, $a0
	move $s1, $a1
#	Make space on stack for temp array
	neg $a2, $a2
	addu $sp, $sp, $a2
	neg $a2, $a2
	move $s2, $sp
	move $s3, $s2
	
# Temp = First Array	
SWAP1:
	lbu $t0, 0($s0)
	sb $t0, 0($s3)
	beqz $t0, END_SWAP1
	addi $s0, $s0, 1
	addi $s3, $s3, 1
	j SWAP1
END_SWAP1:
#	Restore iterator ($s0) to base address of first array
	move $s0, $a0

# First Array = Second Array
SWAP2:
	lbu $t0, 0($s1)
	sb $t0, 0($s0)
	beqz $t0, END_SWAP2
	addi $s1, $s1, 1
	addi $s0, $s0, 1
	j SWAP2
END_SWAP2:
#	Restore iterator ($s1) to base address of second array
#	Restore temp iterator ($s3) to base address of temp array 
	move $s1, $a1
	move $s3, $s2

# Second Array = Temp
SWAP3:
	lbu $t0, 0($s3)
	sb $t0, 0($s1)
	beqz $t0, END_SWAP3
	addi $s3, $s3, 1
	addi $s1, $s1, 1
	j SWAP3
END_SWAP3:

# Cleanup
#	Free  memory used for temp location
	addu $sp $sp, $a2
#	Restore Registers and $sp
	lw $ra, 20($sp)
	lw $t0, 16($sp)
	lw $s3, 12($sp)
	lw $s2, 8($sp)
	lw $s1, 4($sp)
	lw $s0, 0($sp)
	addiu $sp, $sp, 24
	
   	jr $ra