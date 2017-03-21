/* 	TripleSum.java
   	CSC 225 - Summer 2016
   	Assignment 1 - Template for TripleSum
	
	Jory Anderson
	V00843894
	September 20th, 2016

*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;

//Do not change the name of the TripleSum class
public class TripleSum{
	/* TripleSum225()
		The input array A will contain non-negative integers. The function
		will search the input array A for three elements which sum to 225.
		If such a triple is found, return true. Otherwise, return false.
		The triple may contain the same element more than once.
		The function may modify the array A.

		Do not change the function signature (name/parameters).
	*/
	public static boolean TripleSum225(int[] A){
		
		int[] newArray = new int[226];		// Create array of size 226, the max for this assignment
		Arrays.fill(newArray, -675);		// Fill with negative numbers
		for(int i = 0; i < A.length; i++) { 
				if(A[i] < 226)				// Load integers into newArray
					newArray[A[i]] = A[i];
		}
		for(int i = 0; i < 226; i++) {		// O(n^3), tries every combination of 3 elements in the array, compares the sum to 225
			for(int j = 0; j < 226; j++){
				for(int k = 0; k < 226; k++) {
					if(newArray[k] + newArray[i] + newArray[j] == 225)
						return true;		// True if triple == 225
				}
			}
		}
		return false;	// False if no triple == 225 is found
	}

	/* main()
	   Contains code to test the TripleSum225 function. Nothing in this function
	   will be marked. You are free to change the provided code to test your
	   implementation, but only the contents of the TripleSum225() function above
	   will be considered during marking.
	*/
	public static void main(String[] args){		// Provided for this assignment
		Scanner s;
		if (args.length > 0){		// If there was an argument passed
			try{
				s = new Scanner(new File(args[0]));		// Create a scanner pointing to it
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);	/// If arg does not exist, exit
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);	// If no arg passed, get values from stdin
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)		// Add numbers
			inputVector.add(v);

		int[] array = new int[inputVector.size()];		// Convert to array

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.currentTimeMillis();	// Start timer

		boolean tripleExists = TripleSum225(array);		// call TripleExists, passing in array

		long endTime = System.currentTimeMillis();		// End timer

		double totalTimeSeconds = (endTime-startTime)/1000.0;	// Find elapsed time by taking difference

		System.out.printf("Array %s a triple of values which add to 225.\n",tripleExists? "contains":"does not contain");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
	}
}
