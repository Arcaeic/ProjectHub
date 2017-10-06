#	Jory Anderson
#	Seng 265, Zastre
#	Oct. 25th, 2016
#	Assignment #2 - Python Formatting

import sys
import fileinput
fmt     =  0
mgn  =  0
wth   =  0
spacing =  0
paramList = [".LM",".LW",".FT",".LS"]
outputString  = ""

def printLastLine(lastLineCounter):
	if lastLineCounter != 1:
		if fmt != 1:
			print(outputString, end="")
		else:
			print(outputString)

def printLine():
	global spacing
	global outputString
	print(outputString, end="")
	lineSpace(spacing)
	outputString = ""

def outputLine(line):
	global outputString

	for word in line:
		if not outputString:
			addMargin()
		if len(outputString) + len(word) < wth:
			if mgn != len(outputString):
				outputString += " "
			outputString += word
		else:
			printLine()
			print("")
			addMargin()
			outputString += word

def addMargin():
	global outputString
	outputString += " " * mgn
			
def lineSpace (space):			
	if space > 0 and space < 2:
		print("") 
		lineSpace(space-1)

def foundParameter(line):
	global mgn
	global wth
	global fmt
	global spacing

	if line[0] == ".FT":
		if line[1] == "on" or line[1] == "On":
			fmt = 1
		if line[1] == "off" or line[1] == "Off":
			fmt = 0

	if line[0] == ".LM":
		if line[1][0] == "-":
			sub = int(line[1][1:])
			mgn -= sub
			if mgn < 0:
				mgn = 0
		elif line[1][0] == "+":
			add = int(line[1][1:])
			mgn += add
			if mgn > (wth - 20):
				mgn = wth - 20
		else:
			mgn = int(line[1])

	if line[0] == ".LW":
		wth = int(line[1])
		fmt = 1
		
	if line[0] == ".LS":
		spacing = int(line[1])

def main():
	lastLineCounter = 0
	isNewLine = 0
	
	fileOpen = fileinput.input()
	for line in fileOpen:
		lastLineCounter += 1	# To assist with printing last line		
		words = line.split()	# Making line a list of words

		if not words:
			if outputString:    # If end of section
				printLine()
				print("")
				lineSpace(spacing)
			if isNewLine > 0 and fmt == 1:
				print("")
			isNewLine += 1
		if words and isNewLine > 0:
			print("")
			isNewLine = 0
		if words and words[0] in paramList:		#If param found
			foundParameter(words)
			words = words[2:]
		if words and fmt == 1:		#Format line
			outputLine(words)
		elif words:					#If .FT = 0
			print(line,end="")

	printLastLine(lastLineCounter)

if __name__ == "__main__":
	main()