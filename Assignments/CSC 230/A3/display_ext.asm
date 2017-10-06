#define LCD_LIBONLY				; NOTE: Tested on DFRobot V1.0
.include "m2560def.inc"			; For best results, HOLD UP/DOWN(or LEFT) to print messages
.def counter = r16				; Bug: Down works more often than not. Workaround: Use LEFT, or hold DOWN for longer
.def row = r16
.def column = r17
.def temp = r16
.def first = r16
.def second = r17
.def third = r18
.def temp2 = r17

.cseg

.macro goto 
	push @0
	push @1
	call lcd_gotoxy
	pop @1
	pop @0
.endmacro
	

main:
	call lcd_init				; Initialize LCD
	call lcd_clr				; Clear LCD
	call initializeLines		; Move strings from program memory to data memory
	call ADCInit				; Initialize Buttons
	call forLoop				; Display scrolling messages 3 times
	call theEnd					; Prints * and . for a bit.

done:
	rjmp done

;---------------;
;----forLoop----;
;---------------;
forLoop:						; for(int counter = 3; counter > 0; counter--)
	push counter
	ldi counter, 3				; counter = i
loop:
	call display_messages		; Send strings to display
	dec counter					; dec and check
	brne loop					; Break from the loop once the counter reaches 0 (after 3 iterations)
	call lcd_clr				
	pop counter
	ret							; End of forLoop

display_messages:
	call lcd_clr
	push row				
	push column						
display:
	ldi row, 0			;Assign pointer to (0,0) from origin on display
	ldi column, 0
	goto row, column
	call display_msg1	; Display "Jory Anderson"
	call delay
	ldi row, 1			; Assign pointer to (0,-1) from origin
	goto row, column
	call display_msg2	; Display "UVIC: Computer Sci" @ (0, -1)
	call delay
	call lcd_clr		; Reset
	ldi row, 0			
	goto row, column
	call display_msg2	; Display "UVIC: Computer Sci" @ (0, 0)
	call delay
	ldi row, 1			
	goto row, column
	call display_msg3	; Display "nosrednA yroJ" @ (0, -1)
	call delay
	ldi row, 0			; Reset
	ldi column, 0
	call lcd_clr
	goto row, column
	call display_msg3	; Display "nosrednA yroJ" @ (0, 0)
	call delay
	ldi row, 1			
	goto row, column
	call display_msg4	; Display "icS retupmoC :CIVU" @ (0, -1)
	call delay
	call lcd_clr		; Reset
	ldi row, 0			
	goto row, column
	call display_msg4	; Display "icS retupmoC : CIVU" @ (0, 0)
	call delay
	ldi row, 1	
	goto row, column
	call display_msg1	; Display "Jory Anderson" @ (0, -1)
	call delay
	rjmp skip			; If able, reiterate
skip:
	pop column
	pop row
	ret

;--------------;
;----theEnd----;
;--------------;
	
theEnd:
	push row
	push column
	push r18
	ldi r18, 0xFF			; Loop counter
	ldi column, 0
theLoop:
	ldi row, 0
	goto row, column
	call display_msg5		; Display * message
	ldi row, 1
	goto row, column
	call display_msg5		
	call delay				; Delay
	call lcd_clr
	ldi row, 0
	goto row, column
	call display_msg6		; Display "." Message
	ldi row, 1
	goto row, column
	call display_msg6
	call delay				; Delay
	
	dec r18					; Repeat / End
	breq endLoop
	rjmp theLoop
endLoop:
	pop r18
	pop column
	pop row
	ret						; End of theEnd

;--------------------;
;----display_msg#----;
;--------------------;
display_msg1:				; Display "Jory Anderson" on the LCD
	push temp				
	ldi temp, high(line_1)
	push temp
	ldi temp, low(line_1)
	push temp
	call lcd_puts
	pop temp
	pop temp

	pop temp
	ret

display_msg2:				; Display "UVIC: Computer Sci" on the LCD
	push temp				

	ldi temp, high(line_2)
	push temp
	ldi temp, low(line_2)
	push temp
	call lcd_puts
	pop temp
	pop temp

	pop temp
	ret

display_msg3:				; Display "Jory Anderson" in reverse
	push temp
	
	ldi temp, high(line_3)
	push temp
	ldi temp, low(line_3)
	push temp
	call lcd_puts
	pop temp
	pop temp
	
	pop temp
	ret
	
display_msg4:				; Display "UVIC: Computer Sci" in reverse
	push temp
	
	ldi temp, high(line_4)
	push temp
	ldi temp, low(line_4)
	push temp
	call lcd_puts
	pop temp
	pop temp
	
	pop temp
	ret
	
display_msg5:				; Display "***************"
	push temp
	
	ldi temp, high(line_5)
	push temp
	ldi temp, low(line_5)
	push temp
	call lcd_puts
	pop temp
	pop temp
	
	pop temp
	ret

display_msg6:				; Display "..............."
	push temp
	
	ldi temp, high(line_6)
	push temp
	ldi temp, low(line_6)
	push temp
	call lcd_puts
	pop temp
	pop temp
	
	pop temp
	ret
	
;-------------;
;----delay----;
;-------------;
delay:						; Delay using nested loops. ~16200000 cycles ~ 1 second
	push first				; Protect registers in use
	push second
	push third
	push r18
	push r19
	push r17
	push r16

	ldi first, 0x3A
del1:
	nop
	ldi second,0xFF
del2:
	nop
	ldi third, 0xFF
del3:
	nop
	dec third
	brne del3
	dec second
	brne del2
	dec first
	brne del1

;--------------------;
;----checkButtons----;
;--------------------;
checkButtons:					; Check Buttons after Delay. This makes the button slightly finnicky
	lds r16, ADCSRA				; They seem to work best when the LCD is just about to scroll, or about to print msg.
	ori r16, 0x40				; TESTED ON DFRobot V1.1 WILL NOT WORK ON V1.1!, DOWN may take a couple of holds
	sts ADCSRA, r16				; For best results: HOLD BUTTON DOWN
wait:							; Bug: DOWN works more often than not. Workaround: Hold DOWN for longer, or USE LEFT.
	lds r16, ADCSRA			
	andi r16, 0x40
	brne wait					; Wait until conversion is finished

	lds r16, ADCL				; Grab resulting ADCH:ADCL
	lds r17, ADCH
	cpi r17, 2					; If ADCH > 2, then no button / button we dont care about has been pressed
	brpl exitDelay
	cpi r17, 1					; If ADCH = 1, then Down or Left was pressed
	breq Down
	cpi r16, 0x32				; ADCH isolated to 0, if ADCL < 0x32, then Right was pressed
	brmi exitDelay
	cpi r16, 0xC3				; Else, if ADCL > 0xC3, then Down was pressed, else up
	brpl Down
	jmp Up
exitDelay:
	pop r16
	pop r17
	pop r19
	pop r18
	pop third
	pop second
	pop first
	ret
Up:								; When Up is pressed
	push row
	push column	
	call lcd_clr
	call delay
	ldi row, 0					
	ldi column, 0				; Set pointer to (0, 0)
	goto row, column			; Display "Jory Anderson"
	call display_msg1
	ldi row, 1
	goto row, column			; Set pointer to (0, -1)
	call display_msg2			; Display "UVIC: Computer Sci" @ (0, -1)
	call delay					; Let user see messages for a bit
	call delay
	call delay
	pop column					; Go back to end of delay loop, return
	pop row
	jmp exitDelay
Down:							; When Down is pressed				
	push row
	push column
	call lcd_clr
	call delay
	ldi row, 0
	ldi column, 0
	goto row, column			; Set pointer to (0, 0)
	call display_msg3			; Display "Jory Anderson" in reverse
	ldi row, 1
	goto row, column			; Set pointer to (0, -1)
	call display_msg4			; Display "UVIC: Computer Sci" in reverse
	call delay					; Let user see messages for a bit
	call delay
	call delay
	pop column
	pop row
	jmp exitDelay				; Go back and end delay, return

;-----------------------;
;----initializeLines----;
;-----------------------;
								
initializeLines:				; Copy from Program to Data
	push temp					
	push temp2
								; Line_1
	ldi temp, high(line_1)		; Push destination address
	push temp
	ldi temp, low(line_1)
	push temp
	ldi temp, high(msg1 << 1) 	; Push source address
	push temp
	ldi temp, low(msg1 << 1)
	push temp
	call str_init
	pop temp
	pop temp
	pop temp
	pop temp
								; Line_2
	ldi temp, high(line_2)		; Push destination address
	push temp
	ldi temp, low(line_2)
	push temp
	ldi temp, high(msg2 << 1) 	; Push source address
	push temp
	ldi temp, low(msg2 << 1)
	push temp
	call str_init
	pop temp
	pop temp
	pop temp
	pop temp
								; Line_3
	push zH
	push zL
	push temp					;Iterates through each char
	push temp2					;Keeps track of string length
	push r18					; Helper reg
	ldi temp2, 0
	ldi zH, high(msg1 << 1)		; Assign pointers to source and destination
	ldi zL, low(msg1 << 1)		; Z = Source
	ldi xH, high(line_3)		; X = Destination
	ldi xL, low(line_3)
Forward:						; Count characters in Name
	lpm temp, Z+
	cpi temp, 0x00				; Check for null
	breq beginReverse
	inc temp2					; inc length
	brne Forward
beginReverse:
	sbiw zH:zL, 1				; Point to last char in name		
	sbiw zH:zL, 1
Reverse:						; Feed to line_3 using X pointer
	lpm r18, Z
	sbiw zH:zL, 1
	st X+, r18
	dec temp2
	brne Reverse
	ldi r18, 0x00
	st x, r18	
	pop r18
	pop temp2
	pop temp
	pop zL
	pop zH
								; Line_4
	push zH
	push zL
	push temp					; Analogous to technique used for line_3
	push temp2
	push r18
	ldi temp2, 0
	ldi zH, high(msg2 << 1)
	ldi zL, low(msg2 << 1)
	ldi xH, high(line_4)
	ldi xL, low(line_4)
Forward2:	
	lpm temp, Z+
	cpi temp, 0x00
	breq beginReverse2
	inc temp2
	brne Forward2
beginReverse2:
	sbiw zH:zL, 1
	sbiw zH:zL, 1
Reverse2:
	lpm r18, Z
	sbiw zH:zL, 1
	st X+, r18
	dec temp2
	brne Reverse2
	ldi r18, 0x00
	st x, r18	
	pop r18
	pop temp2
	pop temp
	pop zL
	pop zH
								; Asterisks (Line_5)
	ldi temp, high(line_5)		; Push destination address
	push temp
	ldi temp, low(line_5)
	push temp
	ldi temp, high(msg3 << 1) 	; Push source address
	push temp
	ldi temp, low(msg3 << 1)
	push temp
	call str_init
	pop temp
	pop temp
	pop temp
	pop temp
								; Dots (Line_6)
	ldi temp, high(line_6)		; Push destination address
	push temp
	ldi temp, low(line_6)
	push temp
	ldi temp, high(msg4 << 1) 	; Push source address
	push temp
	ldi temp, low(msg4 << 1)
	push temp
	call str_init
	pop temp
	pop temp
	pop temp
	pop temp
	
	pop temp2
	pop temp
	ret							; End of initialize_strings

;---------------;
;----ADCInit----;
;---------------;
ADCInit:						; Initializes Analog to Digital Conv.
	push r16
	ldi r16, 0x87
	sts ADCSRA, r16
	ldi r16, 0x40
	sts ADMUX, r16
	pop r16
	ret
	
; The strings are initially stored in program memory
msg1: 	.db "Jory Anderson", 0
msg2:	.db "UVIC: Computer Sci", 0
msg3:	.db "****************", 0
msg4:	.db "................", 0

.dseg
.include "lcd.asm"

; The strings are subsequently copied into data memory
line_1: .byte 17
line_2: .byte 17
line_3: .byte 17
line_4:	.byte 17
line_5:	.byte 17
line_6:	.byte 17

.undef counter
.undef row
.undef column
.undef temp
.undef first
.undef second
.undef third
.undef temp2
