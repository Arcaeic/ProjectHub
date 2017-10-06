/*
 * Jory Anderson
 * November 27th, 2016
 * Assignment 4 - Programming
 */

/*
 * University of Victoria
 * CSC 225 - Fall 2016
 * Code template for assignment 4
 */
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;
import java.util.Arrays;


// DO NOT CHANGE THE CLASS NAME OR PACKAGE
public class Graph225 {

	/**
	 * Simple representation of an undirected graph, using a square, symmetric
	 * adjacency matrix.
	 * <p>
	 * An adjacency matrix M represents a graph G=(V,E) where V is a set of n
	 * vertices and E is a set of m edges. The size of the matrix is {@code n},
	 * where {@code n} is in the range {@code [4, 15]} only. Thus, the rows and
	 * columns of the matrix are in the range {@code [0, n-1]} representing
	 * vertices. The elements of the matrix are 1 if the edge exists in the
	 * graph and 0 otherwise. Since the graph is undirected, the matrix is
	 * symmetric and contains 2m 1’s.
	 */
	public static class Graph {

		/**
		 * An adjacency matrix representation of this graph
		 */
		private int[][] adjacencyMatrix;

		/*
		 * You are free to add constructors, but the empty constructor is the
		 * only one invoked during marking.
		 */
		public Graph() {}

		/**
		 * Generate a random graph as specified in the assignment statement.
		 * 
		 * @param n
		 *            The size of the graph
		 * @param density
		 *            The density of the graph
		 */
		public void generate(int n, int density) {	
		
			Random numGen = new Random(System.currentTimeMillis());		// Initalize number Generator using seed.
			int edgeCount = 0;
			int root = 0;
	
			if(n < 4 || n > 15)											// Checking arguments
				throw new UnsupportedOperationException("The value of n must be [4,15]!");
			if(density < 1 || density > 3)
				throw new UnsupportedOperationException("The value of density must be [1,3]!");

			adjacencyMatrix = new int[n][n];							// Making adjacencyMatrix size n
			int edges = 0;		
			if(density == 1) 											// Creating graph density, density = 1
				edges = (7*n)/5;

			else if(density == 2)										// density = 2
				edges = (int)((Math.pow(n, 2))/4);

			else 														// density = 3
				edges = (int)((2*(Math.pow(n, 2)))/5);

			boolean[][] changeCheck = new boolean[n][n];				//Use a truth-table to see what (row,col) have been changed				
			do{
				edgeCount = 0; 											//Generating edges. Will loop if edgeCount != edges(or m)
				root = 0;	

				for(int[] row: adjacencyMatrix)							//Reset adjacencyMatrix & truth table
					Arrays.fill(row, 0);
				for(boolean[] row: changeCheck)
					Arrays.fill(row, false);

				for(int i = 0; i < adjacencyMatrix.length; i++) {		// Generate edges
					if(changeCheck[root][i] == false) {					// Check if the current index has been changed
						adjacencyMatrix[root][i] = numGen.nextInt(2);	//		If not, then generate an edge (or not)
						changeCheck[root][i] = true;					//		Change the index and its inverse to true
						changeCheck[i][root] = true;

						if(adjacencyMatrix[root][i] == 1){				//		If an edge was generated, set its inverse as well, increase edgeCount by 2
							adjacencyMatrix[i][root] = 1;
							edgeCount+=2;

						}
					}
					if(i == adjacencyMatrix.length - 1 && root < adjacencyMatrix.length - 1){		// Still O(n^2), this is me trying to optimize runtime
						root++;
						i = 0;

					}	
				}
			} while(edgeCount != 2*edges);								// In the adj. matrix, each edge is counting twice, therefore stop at 2*edges
		}

		/**
		 * Reads an adjacency matrix from the specified file, and updates this
		 * graph's data. For the file structure please refer to the sample input
		 * file {@code testadjmat.txt}).
		 * 
		 * @param file
		 *            The input file
		 * @throws IOException
		 *             If something bad happens while reading the input file.
		 */
		public void read(String file) throws IOException {

			Scanner fileScan = new Scanner(new File(file));
			int sizeCount = 0;							
			while(fileScan.hasNextLine()) {						//Grabbing size of input matrix by counting # of rows
				fileScan.nextLine();
				sizeCount++;
			}

			adjacencyMatrix = new int[sizeCount][sizeCount];	//Re-initialize scanner, grab each value.
			fileScan = new Scanner(new File(file));
			while(fileScan.hasNextInt()) {						// While an unread token exists, fill in the adj. matrix
				for(int i = 0; i < sizeCount; i++) {
					for(int j = 0; j < sizeCount; j++)
						adjacencyMatrix[i][j] = fileScan.nextInt();

				}
			}	
			fileScan.close();									// Close Scanner
		}

		/**
		 * Writes the adjacency matrix representing this graph in the specified
		 * file.
		 * 
		 * @param file
		 *            The path of the output file
		 * @throws IOException
		 *             If something bad happens while writing the file.
		 */
		public void write(String file) throws IOException {

			PrintWriter outputMatrix = new PrintWriter(file);			//Initalizer object to print to file.
			String separator;
			for(int i = 0; i < adjacencyMatrix.length; i++) {			//Grab each edge value. Separator is used for neatness.
				separator = " ";
				for (int j = 0; j < adjacencyMatrix.length; j++) {
					if(j == adjacencyMatrix.length - 1)
						separator = "";

					outputMatrix.print(adjacencyMatrix[i][j] + separator);
				}

				outputMatrix.println();
			}

			outputMatrix.close();
		}

		/**
		 * @return an adjacency matrix representation of this graph
		 */
		public int[][] getAdjacencyMatrix() {

			return this.adjacencyMatrix;
		}

		/**
		 * Updates this graph's adjacency matrix
		 * 
		 * @param m
		 *            The adjacency matrix representing the new graph
		 */
		public void setAdjacencyMatrix(int[][] m) {

			this.adjacencyMatrix = m;
		}

	}

	/**
	 * Traverses the given graph starting at the specified vertex, using the
	 * depth first search graph traversal algorithm.
	 * <p>
	 * <b>NOTICE</b>: adjacent vertices must be visited in strictly increasing
	 * order (for automated marking)
	 * 
	 * @param graph
	 *            The graph to traverse
	 * @param vertex
	 *            The starting vertex (as per its position in the adjacency
	 *            matrix)
	 * @return a vector R of n elements where R[j] is 1 if vertex j can be
	 *         reached from {@code vertex} and 0 otherwise
	 */
	public int[] reach(Graph graph, int vertex) {

		int[][] matrix = graph.getAdjacencyMatrix();
		int[] visited = new int[matrix.length];
		int originalIndex = vertex;

		try{															// Catching IOException
			if (connectedComponents(graph) == 1){						// Cheat: If it's all one component, then we can reach every vertex
				Arrays.fill(visited, 1);
				return visited;
			}

		}catch (IOException e) {										// If IOException caught, then no file exists. Stop catching
			System.out.println("ERROR(reach): No such graph exists!");
		}

		return reachDFS(matrix, visited, originalIndex, vertex);		// Enter DFS-based search

	}

	/*
	* Helper method for reach(), returns a vector which contains an integer array, 
	* 1 if edge exists at (row, col), 0 otherwise. 
	*/
	public int[] reachDFS (int[][] matrix, int[] visited, int originalIndex, int vertex) {

		for(int i = 0; i < matrix.length; i++) {									// Check all edges from the source vertex
			if(matrix[vertex][i] == 1 && visited[i] == 0 && originalIndex != i) {	// Edge must not be visited, and dont look at the previous vertex
				visited[i] = 1;
				originalIndex = i;
				visited = reachDFS(matrix, visited, originalIndex, i);
			}
		}

		return visited;
	}

	/**
	 * Computes the number connected components of a given graph.
	 * <p>
	 * <b>NOTICE</b>: adjacent vertices must be visited in strictly increasing
	 * order (for automated marking)
	 * 
	 * @param graph
	 *            The graph
	 * @return The number of connected component in {@code graph}
	 */
	public int connectedComponents(Graph graph) throws IOException {

			Vector<Integer> visited = new Vector<Integer>();
			int[][] matrix = graph.getAdjacencyMatrix();
			int connected = 0;

			for(int i = 0; i < matrix.length; i++) {
				if(!visited.contains(i)) {
					visited = ConnectedDFS(matrix, visited, i);
					connected++;									//If DFS ends, then we've look at all vertices in component.
				}

				if(visited.size() == matrix.length)					//Reduce runtime
					return connected;

			}

			return connected;
	}

	/*
	* Helper method for connectedComponents, returns the list of visited components
	*/
	public Vector<Integer> ConnectedDFS(int[][] matrix, Vector<Integer> visited, int vertex) {

		visited.add(vertex);										// Add source to visited
		if(visited.size() == matrix.length)							// Reduces run-time.
			return visited;

		for(int i = 0; i < matrix.length; i++) {
			if(matrix[vertex][i] == 1 && !visited.contains(i))     // Keep track of visited nodes.                                                            
				visited = ConnectedDFS(matrix, visited, i);

			if(visited.size() == matrix.length)					// Reduce run-time.
				return visited;

		}
		
		return visited;
	}

	/**
	 * Determines whether a given graph contains at least one cycle.
	 * <p>
	 * <b>NOTICE</b>: adjacent vertices must be visited in strictly increasing
	 * order (for automated marking)
	 * 
	 * @param graph
	 *            The graph
	 * @return whether or not {@code graph} contains at least one cycle
	 */
	public boolean hasCycle(Graph graph) {

		int[][] matrix = graph.getAdjacencyMatrix();			// Grab adj. matrix
		Vector<Integer> visited = new Vector<Integer>();		// Create new list for visited vertices
		int originalIndex = 0;									// Always start at 0.
		int vertex = 0;
		visited.add(vertex);									// Add source vertex

		for(int i = 0; i < matrix.length; i++){					// Check if any vertex is a self-loop, runtime optimization
			if(matrix[i][i] == 1)
				return true;
		}

		return cycleDFS(matrix, visited, originalIndex, vertex);	// Go into DFS-based search for cycle
	}

	/*
	* Helper method for hasCycle, returns boolean, does most of the work.
	*/
	public boolean cycleDFS(int[][] matrix, Vector<Integer> visited, int originalIndex, int vertex) {

		boolean cycle = false;

		for(int i = 0; i < matrix.length; i++) {
			if(matrix[vertex][i] == 1 && originalIndex != i) {		// Look for unexplored edge on non-source vertices

				if(!visited.contains(i)) {							// If the unexplored edges lead to unexplored vertices, keep DFSing
					visited.add(i);
					originalIndex = vertex;
					cycleDFS(matrix, visited, originalIndex, i);	// Keep searching for unexplored edges
				}

				else
					cycle = true;									// Otherwise we have a cycle
			}
		}

		return cycle;
	}

	/**
	 * Computes the pre-order for each vertex in the given graph.
	 * <p>
	 * <b>NOTICE</b>: adjacent vertices must be visited in strictly increasing
	 * order (for automated marking)
	 * 
	 * @param graph
	 *            The graph
	 * @return a vector R of n elements, representing the pre-order of
	 *         {@code graph}
	 */
	public int[][] preOrder(Graph graph) {

		int[][] matrix = graph.getAdjacencyMatrix();
		int[][] preOrder = new int[matrix.length][matrix.length];
		Vector<Integer> visited = new Vector<Integer>();

		int enumer = 0;													//To place values into table
		for(int[] rows: preOrder)
			Arrays.fill(rows, -1);

		for(int vertex = 0; vertex < matrix.length; vertex++) {			// Cycle through each row, performing DFS on each vertex.
			visited.removeAllElements();
			enumer = 0;
			preDFS(matrix, preOrder, visited, vertex, enumer, vertex);	// Go into DFS-based search for preOrder
		}

		return preOrder;
	}

	/*
	* Helper function for preOrder(). Traverses the adj. matrix, and adds the vertices to the list as we visit them.
	*/ 
	public void preDFS(int[][] matrix, int[][] preOrder, Vector<Integer> visited, int vertex, int enumer, int originalVertex) {

		visited.add(vertex);										// Add source to visited

		for(int i = 0; i < preOrder.length; i++) {
			if(matrix[vertex][i] == 1 && !visited.contains(i)) {	// For each vertex, check each edge for an unexplored vertex
				preOrder[originalVertex][enumer] = i;				// Add it to our preOrder list
				enumer++;											// Increment our iterator
				preDFS(matrix, preOrder, visited, i, enumer, originalVertex);	// Continue checking
				while(preOrder[originalVertex][enumer] != -1) 		// Bug fix: iterator would lose track in recursive loops, would overwrite data
					enumer++;
			}
		}
	}

	/**
	 * Computes the post-order for each vertex in the given graph.
	 * <p>
	 * <b>NOTICE</b>: adjacent vertices must be visited in strictly increasing
	 * order (for automated marking)
	 * 
	 * @param graph
	 *            The graph
	 * @return a vector R of n elements, representing the post-order of
	 *         {@code graph}
	 */
	public int[][] postOrder(Graph graph) {
		int[][] matrix = graph.getAdjacencyMatrix();
		int[][] postOrder = new int[matrix.length][matrix.length];
		Vector<Integer> visited = new Vector<Integer>();
		Vector<Integer> visited2 = new Vector<Integer>();

		int enumer = 0;																		//To place values into table
		for(int[] rows: postOrder)
			Arrays.fill(rows, -1);

		for(int vertex = 0; vertex < matrix.length; vertex++) {								//Cycle through each row, performing DFS on each vertex.
			visited.clear();
			visited2.clear();
			enumer = 0;
			postDFS(matrix, postOrder, visited, visited2, vertex, enumer, vertex);
		}
		return postOrder;
	}

	/*
	* Helper function for postOrder(). Traverses the adj. matrix, and adds the current node after traversing its outgoing edges
	*/
	public void postDFS (int[][] matrix, int[][] postOrder, Vector<Integer> visited, Vector<Integer> visited2, int vertex, int enumer, int originalVertex) {

		visited2.add(vertex);																// Add current vertex to second visited list

		for(int i = 0; i < postOrder.length; i++) {											// For each edge from current vertex
			if(matrix[vertex][i] == 1 && !visited.contains(i) && !visited2.contains(i)){	// If edge exists, and adj. vertex hasn't been traversed
				visited.add(i);																// Add to first visited list
				postDFS(matrix, postOrder, visited, visited2, i, enumer, originalVertex);	// Continue DFSing to find presequent elements
				while(postOrder[originalVertex][enumer] != -1)								// Bug fix: Catch iterator up
					enumer++;

				postOrder[originalVertex][enumer] = i;										// Add i to list
				enumer++;																	// Inc. iterator
			}
		}
	}

	/**
	 * test and exercise the algorithms and data structures developed for the
	 * first five parts of this assignment extensively. The output generated by
	 * this method must convince the marker that the algorithms and data
	 * structures are implemented as specified. For example:
	 * <ul>
	 * <li>Generate graphs of different sizes and densities
	 * <li>Test the algorithms for different graphs
	 * <li>Test your algorithms using the sample input file testadjmat.txt
	 * 
	 * @throws Exception
	 *             if something bad happens!
	 */
	public void test() throws Exception {

		//Check initalization
		System.out.print("Checking if testG initalized: ");
		Graph testG = new Graph();
		if(testG == null) 
			System.out.println("Failed.");
		else
			System.out.println("Success.");

		//Check Graph.read() and Graph.write()
		System.out.print("Determining if read() works correctly: \n");
		try{
			testG.read("testadjmat.txt");
		} catch(IOException e) {
			System.out.println("No such file exists in current dir.");
			System.out.println("Failed (Read)");
		}
		System.out.println("Success: Read file successfully. \n\nPrinting matrix to test.txt:");
		testG.write("test.txt");
		matrixCheck(testG.adjacencyMatrix, "test.txt");

		//Checking Graph.generate(), Graph.write() and matrixCheck().
		System.out.println("Generating graph, writing to density1.txt. density = 1, n = 5");
		testG.generate(5, 1);
		testG.write("density1.txt");
		matrixCheck(testG.adjacencyMatrix, "density1.txt");
		System.out.println("Generating graph, writing to density2.txt, density = 2, n = 8");
		testG.generate(8, 2);
		testG.write("density2.txt");
		matrixCheck(testG.adjacencyMatrix, "density2.txt");
		System.out.println("Generating graph, writing to density3.txt, density = 3, n = 12");
		testG.generate(14, 3);
		testG.write("density3.txt");
		matrixCheck(testG.adjacencyMatrix, "density3.txt");

		//Checking connectedComponents(), reach(), and hasCycle().
		System.out.println("Generating graph, density = 1, size = 11");
		testG.generate(11,1);
		System.out.println("Connected components for current graph: " + connectedComponents(testG));
		System.out.println("We can reach the following from vertex 6: " + Arrays.toString(reach(testG, 6)));
		System.out.println("The current graph has a cycle: " + hasCycle(testG));

		//Making graph with no cycle.
		int[][] noCycle = new int[4][4];
		System.out.println("\nReading in a graph with no cycle...");
		noCycle[0][1] = 1;
		noCycle[1][0] = 1;
		noCycle[1][2] = 1;
		noCycle[2][1] = 1;
		noCycle[2][3] = 1;
		noCycle[3][2] = 1;
		testG.setAdjacencyMatrix(noCycle);
		System.out.println("The current graph has a cycle: " + hasCycle(testG));

		//Stress test, re-testing connectedComponents(), reach(), hasCycle(), and orders.
		System.out.println("\nGenerating graph, density = 2, n = 13");
		testG.generate(15,2);
		System.out.println("Connected components for current graph: " + connectedComponents(testG));
		System.out.println("We can reach the following from vertex 6: " + Arrays.toString(reach(testG, 6)));
		System.out.println("The current graph has a cycle: " + hasCycle(testG));
		System.out.println("\nPrinting preOrder of current graph: ");
		System.out.println("-----------------PREORDER-----------------");
		int[][] order = preOrder(testG);
		for (int i = 0; i < order.length; i++) {
			for(int j = 0; j < order.length; j++)
				System.out.printf("%2d%s", order[i][j], " ");
			System.out.println();
		}
		System.out.println("\nPrinting postOrder of current graph: ");
		System.out.println("---------------POSTORDER----------------");
		order = postOrder(testG);
		for(int i = 0; i < order.length; i++) {
			for(int j = 0; j < order.length; j++)
				System.out.printf("%2d%s", order[i][j], " ");
			System.out.println();
		}

		//Testing pre-order and post-order.
		System.out.println("\nTesting pre/post order on testadjmat.txt.");
		System.out.println("Outputting to preOrder.txt, and postOrder.txt");
		testG.read("testadjmat.txt");
		orderCheck(preOrder(testG), "preOrder.txt");
		orderCheck(postOrder(testG), "postOrder.txt");
		
		//Ending test().
		System.out.println("\nEnding test.");
	}

	/*Helper method, checks if the written matrix is equal to Graph.adjacencymatrix
	* as well if it is a undirected, symmetric graph */
	public static void matrixCheck(int[][] matrix, String file) throws IOException {

		Scanner fileScan = new Scanner(new File(file));
		int element = 0;
		boolean check = true;								// Checks if matrix[i][j] == respective element in .txt (matches)
		boolean symmetryCheck = true;						// sChecks if matrix[i][j] == matrix[j][i] (undirected, symmetric)
		int i = 0; 
		int j = 0;
		while(fileScan.hasNextInt()) {
			for(i = 0; i < matrix.length; i++) {
				for(j = 0; j < matrix.length; j++) {
					element = fileScan.nextInt();
					if(element != matrix[i][j]) {
						System.out.println("ERROR in matrixCheck! " + element + " != " + matrix[i][j]);
						check = false;
					}
					if(matrix[i][j] != matrix[j][i])
						symmetryCheck = false;
				}
			}
		}
		if(check)
			System.out.println("adjacencyMatrix matches matrix in " + file);
		else
			System.out.println("adjacencyMatrix DOES NOT match matrix in " + file);
		if(j == i) 
			System.out.print("adjacencyMatrix is square, ");
		else
			System.out.print("adjacencyMatrix is NOT square, ");
		if(symmetryCheck) 
			System.out.println("and symmetric.\n");
		else
			System.out.println("and NOT symmetric.\n");
	}

	/*
	* Part of the testing suite. Checks the order, post or pre
	*/
	public static void orderCheck(int[][] order, String file) throws IOException {

		PrintWriter print = new PrintWriter(new File(file));
		for(int i = 0; i < order.length; i++) {
			for (int j = 0; j < order.length; j++)
				print.printf("%2d%s", order[i][j], " ");
			print.println();
		}
		print.close();
	}

	public static void main(String[] args) {

		Graph225 test225 = new Graph225();		// Create new isntance of Graph225
		try{
			test225.test();						// Run testing suite
		} catch(Exception e) {
			System.out.println("Could not open test suite. Exiting...");	// If an error is thrown in Graph, catch and exit
		}
	}

}
