/*Jory Anderson
* Nov 3rd, 2016
* CSC 225 - Assignment 3
* V00843894
*/
import java.util.Random;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class PQ225 {
	ArrayList<Integer> heapArray;

	public PQ225() {
		heapArray = new ArrayList<Integer>(100);
	}

//Generates n random numbers between low and high, inclusive.
	public void ranGen(int n, int low, int high){
		long seed = System.currentTimeMillis();
		int randNum = 0;

		Random rand = new Random(seed);
		while (n != 0) {
			//Creates new random number.
			do {
				randNum = rand.nextInt(high + 1); //randNum <= high
			} while(randNum < low);
			//Appends random number to temp.
			heapArray.add(randNum);
			n--;
		}
	}

//Returns size of heapArray.
	public int size(){
		return this.heapArray.size();
	}

//Inserts num into heapArray, then Sifts up.
	public void insert(int num) {
		this.heapArray.add(num);
		maintainUpHeap(this.size()-1);
	}

//Sifts up.
	private void maintainUpHeap(int index){	
		if(index != 0) {
			int parentIndex = (index-1)/2;
			if(this.heapArray.get(parentIndex) > this.heapArray.get(index)) {
				int temp = this.heapArray.get(parentIndex);
				this.heapArray.set(parentIndex, this.heapArray.get(index));
				this.heapArray.set(index, temp);
				maintainUpHeap(parentIndex);
			}

		}
	}

//Sifts Down.
	private void maintainDownHeap(int index){
		int minIndex;
		int leftChildIndex = (2 * index) + 1;
		int rightChildIndex = (2 * index) + 2;
		if(rightChildIndex >= size()) {
			if(leftChildIndex >= size())
				return;
			else
				minIndex = leftChildIndex;
		} else {
			if(heapArray.get(leftChildIndex) <= heapArray.get(rightChildIndex))
				minIndex = leftChildIndex;
			else
				minIndex = rightChildIndex;
		}
		if(heapArray.get(index) > heapArray.get(minIndex)) {
			int temp = heapArray.get(minIndex);
			heapArray.set(minIndex, heapArray.get(index));
			heapArray.set(index, temp);
			maintainDownHeap(minIndex);
		}
	}

//Deletes the smallest value from the heapArray, then sifts down.
	public int deleteMin() {
		int i = heapArray.get(0);
		if(size() == 0) 
			System.out.print("ERROR: No element to delete.");
		else {
			this.heapArray.set(0, heapArray.get(size()-1));
			this.heapArray.remove(size()-1);
			if(size() > 0)
				maintainDownHeap(0);
		}
		return i;
	}

//Transforms heapArray into a heap structure.
	public void makeHeap() {
		heapArray.trimToSize();
		for(int i = (size()/2)-1; i>= 0; i--) {
			buildHeap(size(), i);
		}
	}

//Helper method for makeHeap.
	public void buildHeap(int size, int index) {
		int leftChildIndex = (2 * index) + 1;
		int rightChildIndex = (2 * index) + 2;
		int maxValueIndex;

		if(leftChildIndex < size && heapArray.get(leftChildIndex) <
			heapArray.get(index))
			maxValueIndex = leftChildIndex;
		else
			maxValueIndex = index;
		if(rightChildIndex < size && heapArray.get(rightChildIndex) <
		 	heapArray.get(maxValueIndex))
			maxValueIndex = rightChildIndex;
		if(maxValueIndex != index) {
			int temp = heapArray.get(index);
			heapArray.set(index, heapArray.get(maxValueIndex));
			heapArray.set(maxValueIndex, temp);
			buildHeap(size, maxValueIndex);
		}
	}

//Sorts the min-heap into descending order.
	public int heapsort() {
		int temp;
		int heapSize = size();
		makeHeap();
		while (heapSize > 1) {
			temp = heapArray.get(0);
			heapArray.set(0, heapArray.get(heapSize-1));
			heapArray.set(heapSize-1, temp);
			heapSize--;
			buildHeap(heapSize, 0);
		}
		reverseArray();
		return 0;
	}

//Rotates heapArray to be in ascending order. Represents level-order.
	public void reverseArray() {
		int i = 0;
		int j = size()-1;
		int temp;
		while(i < j) {
			temp = heapArray.get(i);
			heapArray.set(i, heapArray.get(j));
			heapArray.set(j, temp);
			i++;
			j--;
		}
	}

	/*
	*The contents from here downward are part of the testing suite.
	*/

	public void printHeap(PrintWriter testWriter){
		testWriter.println("Printing heapArray.");
		for(int i: this.heapArray) 
			testWriter.print(i + " ");
		testWriter.println("\n");
	}

	public int checkHeap(PrintWriter testWriter) {
		testWriter.println("Testing if heapArray is heap by comparing parent to children. parent <= children...");
			for(int i = 0; i <= this.size(); i++) {
				if(2*i +1 <= this.size()) {
					try {
						testWriter.println("Comparing: " + heapArray.get(i) + " to " + heapArray.get(2*i+1)
							+ " and " + heapArray.get(2*i+2));
						if(this.heapArray.get(i) > this.heapArray.get((2*i+1)) || this.heapArray.get(i) > this.heapArray.get((2*i)+2)) {
							return -1;
						}
					}catch(IndexOutOfBoundsException e) {
						testWriter.println("Attempted to compare a non-existing node. Proceeding...");
					}
				} else if(2*i <= this.size()){
					try{
						testWriter.println("Comparing: " + heapArray.get(i) + " to " + heapArray.get(2*i));
						if(this.heapArray.get(i) > this.heapArray.get(2*i)) {
							testWriter.println(i + " " + 2*i);
							return -1;
						}
					}catch(IndexOutOfBoundsException e) {
						testWriter.println("Attempted to compare a non-existing node. Proceeding...");
					}
				} else
					break;
			}
			testWriter.println();
			return 0;
	}

	public static void test() throws FileNotFoundException {

		//Creating Writer objects.
		PrintWriter testWriter = new PrintWriter("pq_test.txt");
	
		//Testing Object integrity
		PQ225 test = new PQ225();
		testWriter.println("PQ225 object created. Checking...");
		if(test.size() == 0)
			testWriter.println("PQ225 initialization: Success.\n");
		else
			testWriter.println("PQ225 initialization: Failure.\n");

		//Testing ranGen()
		test.ranGen(6, 10, 20);
		testWriter.println("6 random numbers between 10 and 20 inserted. Checking...");
		if(test.size() == 6) 
			testWriter.println("PQ225 size incrementation: Success.\n");
		else
			testWriter.println("PQ225 size incrementation: Failure.\n");

		//Attempt to append numbers to an already filled heapArray.
		test.ranGen(4, 10, 20);
		testWriter.println("Added 4 more numbers between 10 and 20. Checking...");
		if(test.size() == 10)
			testWriter.println("ranGen append to PQ225: Success.\n");
		else
			testWriter.println("ranGen append to PQ225: Failure.\n");
		test.printHeap(testWriter);

		//Check if numbers are between bounds 10 and 20.
		testWriter.println("Comparing elements to lower and upper bounds...");
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
			testWriter.println("ranGen lower & upper bounds check: Success.\n");
		
		//End ranGen(), size() tests. Begin makeHeap(), insert(), deleteMin() tests.
		test.makeHeap();

		//Check if makeHeap creates a proper heap.
		test.printHeap(testWriter);
		testWriter.println("Checking if heapArray is a valid heap structure...");
		if(test.checkHeap(testWriter) == 0)
			testWriter.println("Check if heapArray is a valid heap: Success.\n");
		else
			testWriter.println("Check if heapArray is a valid heap: Failure.\n");
		test.printHeap(testWriter);

		//Test if insert() and siftUp() maintain heap integrity.  
		test.insert(36);
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
			testWriter.println("Deleted the smallest number: Failure.\n");
		
		//End insert(), deleteMin(), makeHeap() tests.
		//Begin heapSort tests.
		test.heapsort();
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

		//Closing PrintWriter.
		testWriter.close();
	}
	
	public static void main(String[] args) {
		try{
			test();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException has prevented the test suite from launching.");
		}
	}
}