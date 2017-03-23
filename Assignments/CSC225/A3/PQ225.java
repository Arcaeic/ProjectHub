/*Jory Anderson
* Nov 3rd, 2016
* CSC 225 - Assignment 3
* V00843894
*/
import java.util.Random;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class PQ225 {		// Min-heap object implementation

	ArrayList<Integer> heapArray;	// Array-based heap

	public PQ225() {

		heapArray = new ArrayList<Integer>(100);		// Initialize at 100, reducing amortization
	}

	public void ranGen(int n, int low, int high){ 		// Generates n random numbers between low and high, inclusive.

		long seed = System.currentTimeMillis();			// Initialize seed
		int randNum = 0;

		Random rand = new Random(seed);
		while (n != 0) {								// Creates new random number.
			do {
				randNum = rand.nextInt(high + 1); 		// randNum <= high
			} while(randNum < low);						//Appends random number to temp.
			heapArray.add(randNum);
			n--;
		}
	}

	public int size(){					//Returns size of heapArray.

		return this.heapArray.size();
	}

	public void insert(int num) {		//Inserts num into heapArray, then Sifts up.

		this.heapArray.add(num);
		maintainUpHeap(this.size()-1);
	}

	private void maintainUpHeap(int index){		// Helper method for insert(). Ensures the added item is in the correct spot

		if(index != 0) {														// If the heap is not empty
			int parentIndex = (index-1)/2;											// Declare the parent of the added item
			if(this.heapArray.get(parentIndex) > this.heapArray.get(index)) {		// If our parent is bigger than our added item
				int temp = this.heapArray.get(parentIndex);								// Swap parent and child
				this.heapArray.set(parentIndex, this.heapArray.get(index));
				this.heapArray.set(index, temp);
				maintainUpHeap(parentIndex);										// Continue sifting up
			}
		}
	}

	private void maintainDownHeap(int index){	// Helper method fore deleteMin(). Ensures the heap property is not violated after deleteMin().

		int minIndex;
		int leftChildIndex = (2 * index) + 1;	// Declare left and right children
		int rightChildIndex = (2 * index) + 2;

		if(rightChildIndex >= size()) {			// If right is greater than size, then it does not exist
			if(leftChildIndex >= size())			// If the left also does not exist, no sifting to do
				return;
			else
				minIndex = leftChildIndex;			// Set new minimum if left exists

		} else {
			if(heapArray.get(leftChildIndex) <= heapArray.get(rightChildIndex))	// If the left child is smaller than the right
				minIndex = leftChildIndex;											// Set as new minimum
			else
				minIndex = rightChildIndex; 									// else, set right as new mimnimum
		}

		if(heapArray.get(index) > heapArray.get(minIndex)) {	// If our parent index is larger than the minIndex
			int temp = heapArray.get(minIndex);						// Swap minIndex and parent
			heapArray.set(minIndex, heapArray.get(index));
			heapArray.set(index, temp);
			maintainDownHeap(minIndex);								// Continue sifting
		}														// Else we are finished sifting
	}

	public int deleteMin() {				// Deletes the smallest value from the heapArray, then sifts down.

		int i = heapArray.get(0);			// In our array-based implementation of a heap, the first element is the minimum value

		if(size() == 0) 					// Check if heap is empty
			System.out.print("ERROR: No element to delete.");

		else {
			this.heapArray.set(0, heapArray.get(size()-1));	// Swap min with last item
			this.heapArray.remove(size()-1);				// Delete last item
			if(size() > 0)						// If heap is not empty
				maintainDownHeap(0);				// Begin sifting
		}

		return i;		// Return the deleted value
	}

	public void makeHeap() {				// Transforms heapArray into a heap structure.

		heapArray.trimToSize();				// Unallocate unused space before making heap
		for(int i = (size()/2)-1; i>= 0; i--) {
			buildHeap(size(), i);
		}
	}

	public void buildHeap(int size, int index) {		// Helper method for makeHeap.

		int leftChildIndex = (2 * index) + 1;			// In our array-based implementation, the left child of the current node is (2*index) + 1
		int rightChildIndex = (2 * index) + 2;			// "                                    " right child of the current node is (2*index) + 2
		int maxValueIndex;

		if(leftChildIndex < size && heapArray.get(leftChildIndex) < heapArray.get(index))	// Find index of the maxiumum value
			maxValueIndex = leftChildIndex;

		else
			maxValueIndex = index;

		if(rightChildIndex < size && heapArray.get(rightChildIndex) < heapArray.get(maxValueIndex))
			maxValueIndex = rightChildIndex;

		if(maxValueIndex != index) {				// If the maximum value's index doesn't match our current index
			int temp = heapArray.get(index);						// Swap the value at index with max value
			heapArray.set(index, heapArray.get(maxValueIndex));
			heapArray.set(maxValueIndex, temp);
			buildHeap(size, maxValueIndex);			// Continue building heap
		}

	}		// If it equals, no other larger value found, therefore finished

	public int heapsort() {				//Sorts the min-heap into descending order.

		int temp;
		int heapSize = size();
		makeHeap();

		while (heapSize > 1) {			// While we heapArray still contains values, swap maxValue with minValue and buildHeap()
			temp = heapArray.get(0);
			heapArray.set(0, heapArray.get(heapSize-1));
			heapArray.set(heapSize-1, temp);
			heapSize--;
			buildHeap(heapSize, 0);
		}

		reverseArray();			// Reverse to ascending order once heap is built
		return 0;
	}

	public void reverseArray() {		//Helper method for heapsort(). Rotates heapArray to be in ascending order. Represents level-order.

		int i = 0;
		int j = size()-1;
		int temp;

		while(i < j) {					// Until i and j cross
			temp = heapArray.get(i);	// Swap negative equivalent index
			heapArray.set(i, heapArray.get(j));
			heapArray.set(j, temp);
			i++;
			j--;
		}
	}

	/*
	* The contents from here downward are part of the testing suite I created
	*/

	public void printHeap(PrintWriter testWriter){		// Prints the heap on one line

		testWriter.println("Printing heapArray.");
		
		for(int i: this.heapArray) 
			testWriter.print(i + " ");
		testWriter.println("\n");
	}

	public int checkHeap(PrintWriter testWriter) {		// Determines whether heap property has been violated or not

		testWriter.println("Testing if heapArray is heap by comparing parent to children. parent <= children...");		// Parent must be less or equal to children
			for(int i = 0; i <= this.size(); i++) {
				if(2*i +1 <= this.size()) {				// Check all elements
					try {
						testWriter.println("Comparing: " + heapArray.get(i) + " to " + heapArray.get(2*i+1)
							+ " and " + heapArray.get(2*i+2));
						if(this.heapArray.get(i) > this.heapArray.get((2*i+1)) || this.heapArray.get(i) > this.heapArray.get((2*i)+2)) {
							return -1;
						}
					}catch(IndexOutOfBoundsException e) {
						testWriter.println("Attempted to compare a non-existing node. Proceeding...");		// Edge case: Attempted to check a null node (i points to a leaf)
					}
				} else if(2*i <= this.size()){						// Check the other child
					try{
						testWriter.println("Comparing: " + heapArray.get(i) + " to " + heapArray.get(2*i));
						if(this.heapArray.get(i) > this.heapArray.get(2*i)) {
							testWriter.println(i + " " + 2*i);
							return -1;
						}
					}catch(IndexOutOfBoundsException e) {
						testWriter.println("Attempted to compare a non-existing node. Proceeding...");		// Edge case: Attempted to check a null node (i points to a leaf)
					}
				} else
					break;
			}
			testWriter.println();
			return 0;
	}

	public static void test() throws FileNotFoundException {

		PrintWriter testWriter = new PrintWriter("pq_test.txt");		//Creating Writer objects.
	
		PQ225 test = new PQ225();										//Testing Object integrity
		testWriter.println("PQ225 object created. Checking...");
		if(test.size() == 0)
			testWriter.println("PQ225 initialization: Success.\n");
		else
			testWriter.println("PQ225 initialization: Failure.\n");

		test.ranGen(6, 10, 20);											//Testing ranGen()
		testWriter.println("6 random numbers between 10 and 20 inserted. Checking...");
		if(test.size() == 6) 
			testWriter.println("PQ225 size incrementation: Success.\n");
		else
			testWriter.println("PQ225 size incrementation: Failure.\n");

		test.ranGen(4, 10, 20);											//Attempt to append numbers to an already filled heapArray.
		testWriter.println("Added 4 more numbers between 10 and 20. Checking...");
		if(test.size() == 10)
			testWriter.println("ranGen append to PQ225: Success.\n");
		else
			testWriter.println("ranGen append to PQ225: Failure.\n");
		test.printHeap(testWriter);

		testWriter.println("Comparing elements to lower and upper bounds...");		//Check if numbers are between bounds 10 and 20 from previous test
		boolean check = true;
		for(int i = 0; i < test.size(); i++){
			if(test.heapArray.get(i) > 20) {
				testWriter.println("ranGen upperbound check: Failure.\n");
				check = false;
			}
			if(test.heapArray.get(i) < 10) {
				testWriter.println("ranGen lowerbound check: Failure.\n");
				check = false;
			}
		}
		if(check)
			testWriter.println("ranGen lower & upper bounds check: Success.\n");	//End ranGen(), size() tests. Begin makeHeap(), insert(), deleteMin() tests.
		
		test.makeHeap();				//Check if makeHeap creates a proper heap.
		test.printHeap(testWriter);	
		testWriter.println("Checking if heapArray is a valid heap structure...");
		if(test.checkHeap(testWriter) == 0)
			testWriter.println("Check if heapArray is a valid heap: Success.\n");
		else
			testWriter.println("Check if heapArray is a valid heap: Failure.\n");
		test.printHeap(testWriter);
 
		test.insert(36);			//Test if insert() and siftUp() maintain heap integrity. 
		test.insert(4);
		testWriter.println("Inserted 36 and 4. Checking...");
		if(test.heapArray.get(0) == 4)
			testWriter.println("4 is the root of the heap: Success.\n");
		else
			testWriter.println("4 is the root of the heap: Failure.\n");
		test.insert(8);
		test.printHeap(testWriter);
		if(test.checkHeap(testWriter) == 0)
			testWriter.println("Maintained heap integrity after insert: Success.");
		else
			testWriter.println("Maintained heap integrity after insert: Failure.");
		if(test.heapArray.contains(8))
			testWriter.println("Inserted 8 into heapArray: Success.\n");
		else
			testWriter.println("Inserted 8 into heapArray: Success.\n");
		testWriter.println("Testing deleteMin method by deleting 4. Checking...");
		test.deleteMin();
		test.printHeap(testWriter);
		if(test.heapArray.get(0) != 4){
			if(test.checkHeap(testWriter) == 0)
				testWriter.println("Maintained heap integrity after deleteMin: Success.");
			else
				testWriter.println("Maintained heap integrity after deleteMin: Failure.");
			testWriter.println("Deleted the smallest number: Success.\n");
		} else
			testWriter.println("Deleted the smallest number: Failure.\n");		//End insert(), deleteMin(), makeHeap() tests.
		
		test.heapsort();														//Begin heapSort tests.
		test.printHeap(testWriter);
		testWriter.println("Checking if heapArray is sorted...");
		boolean sorted = true;
		testWriter.println("Beginning comparisons of each element. num1 <= num2.");
		for(int i = 1; i < test.size(); i++) {
			testWriter.println("Comparing: " + test.heapArray.get(i-1) + " to " + test.heapArray.get(i));
			if(test.heapArray.get(i) < test.heapArray.get(i-1))
				sorted = false;
		}
		if(sorted)
			testWriter.println("Determining if heapArray is sorted: Success.");
		else
			testWriter.println("Determining if heapArray is sorted: Failure.");

		testWriter.close();														// End heapsort() tests, close PrintWriter
	}
	
	public static void main(String[] args) {		// Attempt to run test suite
		try{
			test();
		} catch (FileNotFoundException e) {			// Throw error if PrintWriter in test() cannot find file
			System.out.println("FileNotFoundException has prevented the test suite from launching.");
		}
	}
}