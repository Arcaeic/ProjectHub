# Jory Anderson
# Nov 7th, 2016
# Assign3: Python Formatting | Object-Oriented
#! /usr/bin/python3

#============ Importing Packages
import sys
#=============== Class Declaration
class Formatter:
	"For SENG265, a class for formatting string lists, or other text"

#=========================================== Initializing Formatter
	def __init__(self, filename, inputList):

		#Initializing instance variables
		self.width = 0
		self.margin = 0
		self.linespace = 0
		self.ft = False
		self.formats = [".LW", ".LM", ".LS", ".FT"]
		self.newLineCheck = {'firstline':False, 'second':False}

		#Temp string, to be appended to output list.
		self.tempstr = ""

		#The output line(s).
		self.output = []

		#The input. Can be from file, list, or stdin.
		if (filename and filename != "stdin" and not inputList):

			try:
				self.inputLines = open(sys.argv[1], "r")
			except IOError: #filename does not exist
				print("Caught IOError: Specified file name does not exist.")
				print("Check spelling, or use 'stdin' to format input from console.")
				sys.exit()
			except IndexError: #Parameter length mismatch
				sys.stdin

		elif (inputList and not filename): #Use supplied list.
			self.inputLines = inputList
		elif (not filename and not inputList): #Neither exist, Formatter(None, None).
			self.inputLines = sys.stdin
		elif (filename and inputList):		#Conflict
			self.inputLines = inputList
		else:						#Any other case, including filename = "stdin"
			self.inputLines = sys.stdin

	#Begin formatting
		self.start()

#======================== Core Formatting Piece
	def start(self):			
		lastLineCounter = 0
		for (line) in self.inputLines:
			lastLineCounter += 1
		#Check for formatting specifier
			if (line[0:3] in self.formats):
				self.formatChange(line)
		#else format the line
			else:
				self.formatLine(line)
		#When printing the last line
		if (lastLineCounter != 1):
			if (self.ft == True and self.newLineCheck['firstline'] == False):
				self.output.extend([self.tempstr])

#================================= Reassigning Format Values
	def formatChange(self, line):
		#Strip newline char to process values.
		line = line.rstrip("\n")
	#FT Option
		if (line[0:3] == ".FT"):
		#On
			if (line[4:] == "on" or line[4:] == "On"):
				self.ft = True
		#Off
			if (line[4:] == "off" or line[4:] == "Off"):
				self.ft = False
	#Width Option
		if (line[0:3] == ".LW"):
			self.width = int(line[4:])
			self.ft = True
	#Margin option
		if (line[0:3] == ".LM"):
		#If "+" is present, add to current value
			if (line[4] == "+"):
				self.margin += int(line[5:])
			#Margin cannot be greater than width - 20.
				if self.margin > (self.width - 20):
					self.margin = self.width - 20
		#If "-" is present, subtract from current value.
			elif (line[4] == "-"):
				self.margin -= int(line[5:])
			#Margin cannot be smaller than 0.
				if (self.margin < 0):
					self.margin = 0
			#Set margin to supplied number.
			else:
				self.margin = int(line[4:])
	#Linespacing Option
		if (line[0:3] == ".LS"):
			self.linespace = int(line[4:])
			if (self.linespace > 2):
				self.linespace = 2

#================================ Auxillary Formatting Piece
	def formatLine(self, line):		#To format each line passed by self.start()
		saveline = line[:]			#line is saved if .FT = False
		words = line.split()
	#If formatting is off, print as-is
		if (self.ft == False): 
			saveline = saveline.strip('\n')
			self.output.append(saveline)
	#If formatting is on
		elif (words):	#If the line contains words
		#reset newLineCheck
			self.newLineCheck['firstline'] = False
			self.newLineCheck['second'] = False
			for (word) in words:
			#Add margin if empty
				if (not self.tempstr):
					self.tempstr += " " * self.margin
			#If possible, add next word
				if (len(word) + len(self.tempstr) < self.width):
				#Adding spaces
					if (self.margin != len(self.tempstr)):
						self.tempstr += " "
					self.tempstr += word
			#Line is full. Add existing line to output, apply linespace, clear temp.
				else:
					self.output.extend([self.tempstr])
					self.lineSpace(self.linespace)
					self.tempstr = ""
					self.tempstr += " " * self.margin
					self.tempstr += word
		else:
		#Check for double \n
			if (self.newLineCheck['second'] == True):
				self.output.append('')
		#If newline encountered and words still on line, print before \n
			if self.tempstr:
				self.output.extend([self.tempstr])
				self.tempstr = ""
				self.output.extend([self.tempstr])
				self.lineSpace(self.linespace)
				self.lineSpace(self.linespace)
				self.newLineCheck['firstline'] = True
				self.newLineCheck['second'] = True

#================================= Recursive function. Appends empty elements
	def lineSpace(self, space):	
		if (space > 0):
			self.output.append("")
			self.lineSpace(space - 1)

#========================= Returns the output list
	def get_lines(self):
		return self.output