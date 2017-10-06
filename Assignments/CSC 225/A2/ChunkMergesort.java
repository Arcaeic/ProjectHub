/*
 * University of Victoria
 * CSC 225 - Fall 2016
 * Assignment 2 - Template for ChunkMergesort
 *
 * Jory Anderson
 * V00843894
 * Oct. 10th, 2016
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Do not change the name of the ChunkMergesort class
public final class ChunkMergesort {

	/**
	 * Use this class to return two lists.
	 *
	 * Example of use:
	 *
	 * Chunks p = new Chunks(S1, S2); // where S1 and S2 are lists of integers
	 */
	public static final class Chunks {

		private final List<Integer> left;		// Inititalize Left and Right subtree
		private final List<Integer> right;

		public Chunks(List<Integer> left, List<Integer> right) {

			this.left = left;
			this.right = right;
		}

		public List<Integer> left() {	// Getter: return left

			return this.left;
		}

		public List<Integer> right() {	// Getter: return right

			return this.right;
		}
	}

	/**
	 * The list containing the integer comparisons in order of occurrence. Use
	 * this list to persist the comparisons; the method report will be invoked
	 * to write a text file containing this information.
	 *
	 * Example of use (when comparing 1 and 9):
	 *
	 * Integer[] d = new Integer[2];
	 * d[0] = 1;
	 * d[1] = 9;
	 * this.comparisons.add(d);
	 *
	 * or just:
	 *
	 * this.comparisons.add(new Integer[]{1, 9});
	 */

	private final List<Integer[]> comparisons;

	public ChunkMergesort() {

		this.comparisons = new ArrayList<Integer[]>();
	}

	public List<Integer> chunkMergesort(List<Integer> S) {

		if (S.size() < 2)			//If the list is empty or has size of one:
			return S;				//It is sorted
		else {

			int chunks = 1;
			for (int i = 1; i < S.size(); i++) { 		//Checks for total chunks

					if(S.get(i-1) > S.get(i)) 			//Comparison 1
						chunks++;
					this.comparisons.add(new Integer[]{S.get(i-1), S.get(i)});
			}
		if (chunks == 1) 	//One chunk, already sorted.
			return S;
	
		List<Integer> S2 = new ArrayList<Integer>();
		int chunkCount = chunks/2; 						//Get amount of chunks for first half.
		if(chunkCount*2 != chunks) 						//If chunks is odd
			chunkCount++;	

		int tempChunk = chunkCount; 					//Counter
			for (int i = 1; i < S.size(); i++) { 		//Checking where to split the list.

				if (S.get(i-1) > S.get(i)) 				//Comparison 2
					tempChunk--;

				this.comparisons.add(new Integer[]{S.get(i-1), S.get(i)});
				if (tempChunk == 0 ) { 					//If 0, then we know where the first half ends
					tempChunk = -1;

					for (int j = 0; j != i; j++) 		//Splitting the List.
						S2.add(S.remove(0));
				}
			}

		if (chunkCount == 1 && chunks-chunkCount == 1) { //Edge case. Both are sorted.
			return merge (S2, S);
		}

		Chunks left = chunkDivide(S2, chunkCount);					// Divide left subtree into two sorted subtrees
		List<Integer> left2 = merge(left.left(), left.right());		// Fully sorted left side
		Chunks right = chunkDivide(S, chunks-chunkCount);			// Repeat for right side
		List<Integer> right2 = merge(right.left(), right.right());

		return merge(left2, right2);	// Merge the sorted right and left, and return the result
		}
	}

	public Chunks chunkDivide(List<Integer> S, int c) {

		if (c==1) { 											//If 1, then list is sorted.
			Chunks oneSided = new Chunks(null, S);
			return oneSided;

		} else if (c==2) { 									//If 2, then it will be sorted after split.
			List<Integer> S2 = new ArrayList<Integer>();
			for (int i = 1; i < S.size(); i++) {
				this.comparisons.add(new Integer[]{S.get(i-1), S.get(i)});

				if (S.get(i-1) > S.get(i)) { //Comparison 3
					for (int j = 0; j != i; j++)
						S2.add(S.remove(0));
					Chunks finalChunk = new Chunks(S2, S);
					return finalChunk;
				}
			}

		} else if (c > 2) { 									//If >2, use recursion to sort unsorted list.
			List<Integer> S2 = new ArrayList<Integer>();
			for(int i = 1; i < S.size(); i++) {
				this.comparisons.add(new Integer[]{S.get(i-1), S.get(i)});

				if (S.get(i-1) > S.get(i)) {					 //Comparison 4
					for (int j = 0; j != i; j++)
						S2.add(S.remove(0));
					Chunks finalChunk = new Chunks(S2, listSort(S, c-1));
					return finalChunk;
				}
			}
		}
		return null; 			//Somehow the List had <1 chunk.
	}

	public List<Integer> listSort(List<Integer> S, int c) {

		List<Integer> S2 = new ArrayList<Integer>();
		for (int i = 1; i < S.size(); i++) {
			this.comparisons.add(new Integer[]{S.get(i-1), S.get(i)});

			if (S.get(i-1) > S.get(i)) {			//Comparison 5
				for (int j = 0; j != i; j++)
					S2.add(S.remove(0));

				if (c == 2)							//Base Case
					return merge(S2, S);

				else {
					List<Integer> tempArray = listSort(S, c-1);
					return merge(S2, tempArray);
				}
			}
		}

		return S2; //Something went wrong.
 	}

	public List<Integer> merge(List<Integer> S1, List<Integer> S2) {
		
		if (S1 == null)				 //Nothing to sort
			return S2;

		if (S2 == null)
			return S1;

		List<Integer> tempList = new ArrayList<Integer>();
		while (!S1.isEmpty() && !S2.isEmpty()) {						 //While both arrays are not empty.
			this.comparisons.add(new Integer[]{S1.get(0), S2.get(0)});

			if (S1.get(0) < S2.get(0)) 					 //Smaller element is inserted into temp.
				tempList.add(S1.remove(0));
			else 
				tempList.add(S2.remove(0));
		}

		while (!S1.isEmpty()) 							//S2 has been emptied into temp, dump S1.
			tempList.add(S1.remove(0));

		while (!S2.isEmpty()) 							//S1 has been emptied into temp, dump S2.
			tempList.add(S2.remove(0));

		return tempList;
	}

	/**
	 * Writes a text file containing all the integer comparisons in order of
	 * ocurrence.
	 *
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public void report() throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("comparisons.txt", false));
		for (Integer[] data : this.comparisons)
			writer.append(data[0] + " " + data[1] + "\n");
		writer.close();
	}

	/**
	 * Contains code to test the chunkMergesort method. Nothing in this method
	 * will be marked. You are free to change the provided code to test your
	 * implementation, but only the contents of the methods above will be
	 * considered during marking.
	 */
	public static void main(String[] args) {
		Scanner s;
		if (args.length > 0) {
			try {
				s = new Scanner(new File(args[0]));
			} catch (java.io.FileNotFoundException e) {
				System.out.printf("Unable to open %s\n", args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n", args[0]);
		} else {
			s = new Scanner(System.in);
			System.out.printf("Enter a list of integers:\n");
		}
		List<Integer> inputList = new ArrayList<Integer>();

		int v;
		while (s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputList.add(v);

		s.close();
		System.out.printf("Read %d values.\n", inputList.size());

		long startTime = System.currentTimeMillis();

		ChunkMergesort mergesort = new ChunkMergesort();
		/* List<Integer> sorted = */ mergesort.chunkMergesort(inputList);

		long endTime = System.currentTimeMillis();
		double totalTimeSeconds = (endTime - startTime) / 1000.0;

		System.out.printf("Total Time (seconds): %.2f\n", totalTimeSeconds);

		try {
			mergesort.report();
			System.out.println("Report written to comparisons.txt");
		} catch (IOException e) {
			System.out.println("Unable to write file comparisons.txt");
			return;
		}
	}

}