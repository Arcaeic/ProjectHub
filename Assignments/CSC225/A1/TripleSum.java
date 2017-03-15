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
		
		int[] newArray = new int[226];
		Arrays.fill(newArray, -675);
		for(int i = 0; i < A.length; i++) { 
				if(A[i] < 226)
					newArray[A[i]] = A[i];
		}
		for(int i = 0; i < 226; i++) {
			for(int j = 0; j < 226; j++){
				for(int k = 0; k < 226; k++) {
					if(newArray[k] + newArray[i] + newArray[j] == 225)
						return true;
				}
			}
		}
		return false;
	}

	/* main()
	   Contains code to test the TripleSum225 function. Nothing in this function
	   will be marked. You are free to change the provided code to test your
	   implementation, but only the contents of the TripleSum225() function above
	   will be considered during marking.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);

		int[] array = new int[inputVector.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.currentTimeMillis();

		boolean tripleExists = TripleSum225(array);

		long endTime = System.currentTimeMillis();

		double totalTimeSeconds = (endTime-startTime)/1000.0;

		System.out.printf("Array %s a triple of values which add to 225.\n",tripleExists? "contains":"does not contain");
		System.out.printf("Total Time (seconds): %.2f\n",totalTimeSeconds);
	}
}
