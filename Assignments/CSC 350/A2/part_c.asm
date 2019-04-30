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

	.text
#####
#####	
INIT:
	# Save $s0, $s1 and $s2 on stack.
	addi $t0, $sp, 12
	sub $sp, $sp, $t0
	sw $s0, 0($sp)
	sw $s1, 4($sp)
	sw $s2, 8($sp)
	
	
	la $a0, MESSAGE3
	li $v0, 4
	syscall
	
	# Initialize NUM_WORDS to zero.
	#
	# Load start of word array into $s0; we'll directly read
	# input words into this array/buffer. 
	la $t0, NUM_WORDS
	sw $zero, 0($t0)
	la $s0, WORD_ARRAY
		
READ_WORD:
	add $a0, $s0, $zero
	li $a1, MAX_WORD_LEN
	li $v0, 8
	syscall
	
	# Empty string? If so, finish. An emtpy string
	# consists of the single newline character.
	lbu $t0, 0($s0)
	li $t1, NEW_LINE_ASCII
	beq $t0, $t1, CALL_QUICKSORT
	
	# Increment # of words; at the maximum??
	la $t0, NUM_WORDS
	lw $t1, 0($t0)
	addi $t1, $t1, 1
	sw $t1, 0($t0)
	addi $t2, $zero, MAX_NUM_WORDS
	beq $t1, $t2, CALL_QUICKSORT
	
	# Otherwise proceed to the next work
	addi $s0, $s0, MAX_WORD_LEN
	j READ_WORD
	

	
CALL_QUICKSORT:	
	# Before call to quicksort
	jal FUNCTION_PRINT_WORDS
	
	# Assemble arguments
	la $a0, WORD_ARRAY
	li $a1, 0
	la $t0, NUM_WORDS
	lw $a2, 0($t0)
	addi $a2, $a2, -1
	jal FUNCTION_HOARE_QUICKSORT
			
	# Restore from stack the callee-save registers used in this code
	lw $s0, 0($sp)
	lw $s1, 4($sp)
	lw $s2, 8($sp)
	addi $sp, $sp, 12
	
	# After call to quicksort
	jal FUNCTION_PRINT_WORDS

EXiT:
	li $v0, 10
	syscall
	
	
	
#####
#####	
FUNCTION_PRINT_WORDS:
	la $a0, MESSAGE1
	li $v0, 4
	syscall
	
	la $t0, NUM_WORDS
	lw $a0, 0($t0)
	li $v0, 1
	syscall
	
	la $a0, NEW_LINE
	li $v0, 4
	syscall
	
	la $a0, MESSAGE2
	li $v0, 4
	syscall
	
	li $t0, 0
	la $t1, WORD_ARRAY
	la $t2, NUM_WORDS
	lw $t2, 0($t2)
	
LOOP_FPW:
	beq $t0, $t2, EXIT_FPW
	add $a0, $t1, $zero
	li $v0, 4
	syscall
	addi $t0, $t0, 1
	addi $t1, $t1, MAX_WORD_LEN
	j LOOP_FPW
	
EXIT_FPW:
	jr $ra
	
	
	
	
##########################################################
#
# YOUR SOLUTION MAY NOT ADD MORE GLOBAL DATA OR CONSTANTS.
# ALL OF THE CODE FOR YOUR FUNCTION(S) MUST APPEAR AFTER
# THIS POINT. SUBMISSIONS THAT IGNORE THIS REQUIREMENT
# MAY NOT BE ACCEPTED FOR EVALUATION.
#
##########################################################

###FUNCTION_STRCMP
# $a0 contains the address of the first string
# $a1 contains the address of the second string
# $v0 will contain the result of the function.

FUNCTION_STRCMP:

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

###FUNCTION_SWAP
# $a0 contains the address of the first string array
# $a1 contains the address of the second string array
# $a2 contains the maximum length of the arrays
# 
	
FUNCTION_SWAP:

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


# FUNCTION_PARTITION
# $a0 contains the starting address of the array of strings,
#    where each string occupies up to MAX_WORD_LEN chars.
# $a1 contains the starting index for the partition
# $a2 contains the ending index for the partition
# $v0 contains the index that is to be returned by the
#    partition algorithm
#
FUNCTION_PARTITION:
# Registers Used:
# 	$s0: Array Address
# 	$s1, $s2: lo, hi
# 	$t0: (high + lo)/ 2
# 	$t1: A[(lo + hi)/2]
# 	$t2, $t4: A[i], A[j]
# 	$t3: conditionals

#	Save Registers
	addiu $sp, $sp, -36
	sw $s0, 0($sp)
	sw $s1, 4($sp)
	sw $s2, 8($sp)
	sw $t0, 12($sp)
	sw $t1, 16($sp)
	sw $t2, 20($sp)
	sw $t3, 24($sp)
	sw $t4, 28($sp)
	sw $ra, 32($sp)
	
#	Move data out of argument registers
	move $s0, $a0	#$s0 = Array address
	move $s1, $a1	#$s1 = lo
	move $s2, $a2	#$s2 = high
	
#	Calculate ((low + hi) / 2)
	add $t0, $s1, $s2
	sra $t0, $t0, 1
	sll $t0 $t0, MAX_WORD_LEN_SHIFT	# Each string is 32 bytes
	add $t1, $s0, $t0 		# $t1 = pivot

#	Convert lo and hi to proper 32-byte aligned offsets.
	sll $s1, $s1, MAX_WORD_LEN_SHIFT
	sll $s2, $s2, MAX_WORD_LEN_SHIFT

	addi $s1, $s1, -MAX_WORD_LEN	# $s1 = i - 1
	addi $s2, $s2, MAX_WORD_LEN	# $s2 = j + 1
	
LOOP_FOREVER:
DO_WHILE1:
	addi $s1, $s1, MAX_WORD_LEN	# $s1 = i + 1
	add $t2, $s0, $s1		# $t2 = A[i]
	move $a0, $t2
	move $a1, $t1
	jal FUNCTION_STRCMP		# FUNCTION_STRCMP(A[i], pivot)
	bltz $v0, DO_WHILE1
DO_WHILE2:
	addi $s2, $s2, -MAX_WORD_LEN	# $s2 = j - 1
	add $t4, $s0, $s2		# $t4 = A[j]
	move $a0, $t4
	move $a1, $t1
	jal FUNCTION_STRCMP		# FUNCTION_STRCMP(A[j], pivot)
	bgtz $v0, DO_WHILE2
	
	sge $t3, $s1, $s2		# $t3 = i >= j
	bnez $t3, EXIT_PARTITION
	
#	Swap A[i] and A[j]
	move $a0, $t2
	move $a1, $t4
	add $a2, $zero, MAX_WORD_LEN
	jal FUNCTION_SWAP		# FUNCTION_SWAP(A[i], A[j])
	
	j LOOP_FOREVER
	
EXIT_PARTITION:
#	Convert 32-byte aligned offset back to hi (hi << 5)
	sra $v0, $s2, 5
	
#	Restore Registers
	lw $ra, 32($sp)
	lw $t4, 28($sp)
	lw $t3, 24($sp)
	lw $t2, 20($sp)
	lw $t1, 16($sp)
	lw $t0, 12($sp)
	lw $s2, 8($sp)
	lw $s1, 4($sp)
	lw $s0, 0($sp)
	addiu $sp, $sp, 36
	
	jr $ra
		
# FUNCTION_HOARE_QUICKSORT
# $a0 contains the starting address of the array of strings,
#    where each string occupies up to MAX_WORD_LEN chars.
# $a1 contains the starting index for the quicksort
# $a2 contains the ending index for the quicksort
#
# THIS FUNCTION MUST BE WRITTEN IN A RECURSIVE STYLE.
#

FUNCTION_HOARE_QUICKSORT:
# Registers Used:
# 	$t0: Array Address
# 	$t1, $t2: lo, hi
# 	$t3: conditionals, $v0 offloader

#	Save Registers
	addiu $sp, $sp, -20
	sw $t0, 0($sp)
	sw $t1, 4($sp)
	sw $t2, 8($sp)
	sw $t3, 12($sp)
	sw $ra, 16($sp)

	move $t0, $a0		# $t0 = Array address
	move $t1, $a1		# $t1 = lo
	move $t2, $a2		# $t2 = hi
	
	slt $t3, $t1, $t2	# $t3 = lo < hi
	beqz $t3, EXIT_QUICKSORT
	
	jal FUNCTION_PARTITION	# p = FUNCTION_PARTITION(A, lo, hi)
	move $t3, $v0		# $v0 = p
	move $a0, $t0
	move $a1, $t1
	move $a2, $t3
	jal FUNCTION_HOARE_QUICKSORT	# FUNCTION_HOARE_QUICKSORT(A, lo, p)
	addi $t3, $t3, 1
	move $a0, $t0
	move $a1, $t3
	move $a2, $t2
	jal FUNCTION_HOARE_QUICKSORT	# FUNCTION_HOARE_QUICKSORT(A, p + 1, hi)
	
EXIT_QUICKSORT:
#	Restore Registers
	lw $ra, 16($sp)
	lw $t3, 12($sp)
	lw $t2, 8($sp)
	lw $t1, 4($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 20
	
	jr $ra
