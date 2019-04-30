# Procedure Calling
Created Monday 21 January 2019

**Caller:** code **invoking **the procedure
**Callee**: Code within an invoked procedure

Caller vs callee is relative to the procedure call instruction itself
The wrkflow steps are similar to that for many ISAs

a. **[caller] **Place function parameters (if any) into registers; in some cases perhaps push parameters onto stack
b. **[caller] **Transfer control to procedure
c. **[callee] **Acquire storage for procedure (ie., reserve room on stack for procedure's local data)
d. **[callee] Perform procedure's operations**
e. **[callee]** Place result in register for caller
f. **[callee] **Release storage acquired for the procedure
g. **[caller] **Return to place of the procedure call; if necessary, pop off any parameters that were placed on the stack


### Register Use
![](./Procedure_Calling/pasted_image.png)

### Procedure Call instructions
Procedure call is a **jump** and **link**
__jal PROCEDURE_LABEL__
Address of isntruction following the jal is put into $ra
Procedure return is a **jump** to an address stored in **register**
__jr $ra__
Copies $ra to Program Counter (PC)
Can also be used for computed jumps (case/switch in C)
![](./Procedure_Calling/pasted_image001.png)



