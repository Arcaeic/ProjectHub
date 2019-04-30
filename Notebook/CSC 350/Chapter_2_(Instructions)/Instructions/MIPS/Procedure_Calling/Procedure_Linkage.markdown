# Procedure Linkage
Created Monday 21 January 2019

**Local data is allocated by callee**
Typical uses for local-data storage
C local variables (ie., "automatic" variables)
Space needed to spill registers temporarily
**Procedure frame**
This is the part of the stack set aside for procedure call
Normally is is the callee that sets up the frame
Also known as the **activation record**
![](./Procedure_Linkage/pasted_image.png)

**Text: **Program Code
**Static Data**: Global variables
Static variables in C, constant arrays, char arrays used as strings
$gp is initialized to allow +/- offsets into this segment
**Dynamic Data**: Usually called the heap
Example: malloc() in C, new() in Java
**Stack**: Also called automatic storage


