	.data
string1:	.asciiz	" This is a test! \n"

	.text
main: 
	li	$v0, 4
	la	$a0, string1
	syscall
	li	$v0, 10	syscall