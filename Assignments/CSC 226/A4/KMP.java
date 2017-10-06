/*
	Jory Anderson
	V00843894
	CSC 226, Spring 2017
	March 20th, 2017
	A4 - KMP
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;


public class  KMP 
{
   private static String pattern;

   
   public KMP(String pattern) {  				// Assign KMP.pattern to the passed in pattern

   		this.pattern = pattern;
   }

   public static String getPattern() {			// Getter: return pattern

   		return pattern;
   }

   public static int search(String txt) {  					// Returns starting index of pattern match in txt

   		String pattern = getPattern();						// Grab pattern from KMP class

   		int[] patternArray = new int[pattern.length()];		// Begin initializing patternArray
   		for(int i = 0; i < pattern.length(); i++) 			// Load each character from pattern into patternArray
   			patternArray[i] = pattern.charAt(i);

   		int[] txtArray = new int[txt.length()];				// Begin initalizing txtArray
   		for(int i = 0; i < txt.length(); i++) 				// Load each character from txt into txtArray
   			txtArray[i] = txt.charAt(i);

   		int[] table = new int[patternArray.length];			// Initialize table
   		table = createTable(table, patternArray);
		System.out.println(Arrays.toString(table));

   		int txtIndex = 0;									// Points to char in txt
   		int patternIndex = 0;								// Points to char in Pattern

   		while (txtIndex + patternIndex < txtArray.length) {		// While traversing txt

   			if (patternArray[patternIndex] == txtArray[txtIndex + patternIndex]) {		// Character match
   				
   				if(patternIndex == patternArray.length - 1)			// If patternIndex has reached the last char in the pattern
   					return txtIndex;								// Match, end run
   				patternIndex++;

   			} else {												// Character mismatch

   				if (table[patternIndex] > -1) {						// If we can backstep
   					txtIndex += patternIndex - table[patternIndex];
   					patternIndex = table[patternIndex];

   				} else {											// No backstep, reset
   					txtIndex++;
   					patternIndex = 0;

   				}
   			}
   		}
   		return txtArray.length;										// No match, end run
   }

   public static int[] createTable(int[] table, int[] pattern) {

   		Arrays.fill(table, 0);								// Ensure table isn't storing garbage data
   		int pos = 2;										// Initialize values
   		int finalIndex = 0;
   		table[0] = -1;
   		if(table.length > 1)
   			table[1] = 0;

   		while (pos < pattern.length) {						// While iterating through pattern

   			if (pattern[pos-1] == pattern[finalIndex]) {	// If the substring continues
   				table[pos] = finalIndex + 1;
   				finalIndex++;
   				pos++;

   			} else if (finalIndex > 0) {					// If it doesn't, but fallback is possible
   				finalIndex = table[finalIndex];

   			} else {										// Reset to 0 otherwise
   				table[pos] = 0;
   				pos++;
   			}
   		}

   		return table;										// Return our new & shiny table

   }

        
  	public static void main(String[] args) throws FileNotFoundException { // This testing suite was included with the assignment
		Scanner s;
		if (args.length > 0) {
			try {													// If arg[0] != null, make it a file and initialize a Scanner 
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e) {				// If arg[0] == null, print error and exit
				System.out.println("Unable to open "+args[0]+ ".");
				return;
			}
			System.out.println("Opened file "+args[0] + ".");
			String text = "";										// Initialize our text string
			while(s.hasNext()) {									// Load tokens from arg[0] into text
				text+=s.next()+" ";
			}
			
			for(int i=1; i<args.length ;i++) {						// For each search query in args (after the filename)
				KMP k = new KMP(args[i]);							// Make a KMT 
				int index = search(text);							// Returns the index of the first character in the match (bad testing, only checks the length of the string)
				if(index >= text.length())System.out.println(args[i]+ " was not found.");	
				else System.out.println("The string \""+args[i]+ "\" was found at index "+index + ".");
			}
			//System.out.println(text);
		}else{
			System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");	// If template is improper, show usage
		}
	}
}
