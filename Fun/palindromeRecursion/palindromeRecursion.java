/** About
@author: Jory Anderson
@date: December 30, 2019
@desc: A simple recursive-based approach for validating palindromes. Supports alphanumeric characters and special
	   characters. Program will loop, exit the Java Runtime (CTRL + C) to quit the application.
**/

import java.util.*;


public class palindromeRecursion {
	
	
	/** run(String, int, int)
	@param(String word): The alleged palindrome to be traversed
	@param(int a): The "min" index pointer, begins at index 0.
	@param(int b): The "max" index pointer, begins at word.length()-1
	@desc: Compares the characters at word[min], word[max]. Any uniqueness results in false, as that would not be
	a palindrome. Continues recursively calling run() until the two pointers "overlap" in the middle, 
	@return(boolean): Returns false if word[min] and word[max] are different, true if the word is exhausted.
	**/
	public static boolean run (String word, int a, int b) {
		
		//Grab the current character (a) and its reflection (b)
		char letter1 = Character.toLowerCase(word.charAt(a));
		char letter2 = Character.toLowerCase(word.charAt(b));
		
		//Check if the characters are identical
		if (letter1 == letter2) {
			//Check if a and b have "overlapped" each other, else does the recursive call inline.
			if( a >= b || run(word, ++a, --b) )
				return true;
		}
		return false;
	}
	
	/** main()
	@desc: Handles user input, and input validation, then pushes the word and min/max indices to the 
	recursive function
	**/
	public static void main (String[] args) {
		
		//Initialize Scanner and prompt user for input.
		Scanner ask = new Scanner(System.in);
		
		while(true) {
			System.out.print("\nPlease insert a word/phrase. The word/phrase will be validated for palindrome-ness.\n");
			
			//Grab input, and validate.
			String word = ask.nextLine();
			while (word.length() == 0) {
				System.out.println("\nYou need to actually enter something. Try again. \n");
				word = ask.nextLine();
			}
			
			//Palindrome Verification
			if(run(word, 0, word.length()-1 ))
				System.out.println("The word is a palindrome.\n");
			else {
				System.out.println("Wot da f0k man.\n");
			}
			
			System.out.println("----------Restarting----------");
		}
	}
}