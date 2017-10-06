
//package shorteshpath;
/* ShortestPath.java
   CSC 226 A2
   Jory Anderson
   V00843894
   March 2nd, 2017
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;


//Do not change the name of the ShortestPath class
public class ShortestPath{

    //TODO: Your code here   
    public static int numVerts;
    public static int[] distance;	//Contains distance of each vertex from source.
    public static int[] root;		//Contains the "parent" of each path's vertices. (Think union find)

	static void ShortestPath(int[][] G, int source){ //Prints the total distance from source to each V.
		numVerts = G.length;
		if(source >= numVerts) { //Error Prevention
			System.out.println("ERROR: Source is an invalid vertex. Unable to find distances.");
			return;
		}
		ArrayList<Integer> visited = new ArrayList<Integer>();
		root = new int[numVerts]; 	//Contains Paths.
		distance = new int[numVerts]; 	//Contains distances from source.
		Arrays.fill(root, 1001); 	//If root[i] = 1001, then i is disconnect.
		Arrays.fill(distance, 1001); 	//Set each vertex to infinity (1001)

		root[source] = 0;		//Root of source should be source.
		distance[source] = 0;	 //Set source to 0 so alg starts at source.
		int min = 1001;	 	 //Set min (Vertex) to infinity. Min = VertexIndex
		boolean minSet = true; 	//A min should be selected each pass. Detects discontinuity.
		while(minSet == true) {
			min = 1001;	  	//Set min to infinity and minSet to false each pass.
			minSet = false; 
			for(int i = 0; i < numVerts; i++) {
				if(distance[i] <= min && !visited.contains(i))
					min = i; 	//Therefore min = the vertex w/ min distance
			}
			if(min != 1001) { 	//If a vertex was picked
				visited.add(min);	//Add to visited
				minSet = true;
			} else
				break;			//If a vertex is not picked during a pass, then we have found all connected vertices.

			for(int i = 0; i < numVerts; i++) { 	//Update shortest distances
				if(G[min][i] == 0) //Skip non-existant edges
					continue;
				if(distance[min] + G[min][i] < distance[i]){ 	//If shorter route exists, update
					distance[i] = distance[min] + G[min][i];
					root[i] = min;
				}
			}
		}
    	if(source != 0) { 	//Remake root if source != 0
    		int[] newRoot = new int[numVerts];
    		Arrays.fill(newRoot, source);
    		for(int i = 0; i < newRoot.length; i++){
    			if(root[i] != 0)
    				newRoot[i] = root[i];
    		}
    		root = newRoot;
    	}
	}
        
    static void PrintPaths(int source){	//Finds path of each vertex
    	if(source >= numVerts) {	//Error Prevention
    		System.out.println("ERROR: Source is an invalid vertex. Unable to print path.");
    		return;
    	}
    	int originalSource = source;	//Prevents redundancy in path printing
       for(int i = 0; i < root.length; i++) {
       		if(root[i] != source && root[i] != 1001) 	//Path contains more than one edge.
       			System.out.println("The path from " + source + " to " + i + " " + PrintPathHelp(i, root[i], originalSource) + i + " and the total distance " + distance[i]);
       		else if(i == source)	 //Current vertex is source
       			System.out.println("The path from " + source + " to " + i +  " " + i + " and the total distance " + distance[i]);
       		else if(root[i] == source) 	//Case: source -> i
       			System.out.println("The path from " + source + " to " + i + " " + source + " -> " + i + " and the total distance " + distance[i]);
 	   }
 	}

    static String PrintPathHelp(int source, int vertex, int originalVertex) {	//Helper-function for PrintPaths. Recursively prints path.
    	if(vertex == originalVertex)	//Base case: If current vertex is the source.
    		return vertex + " -> ";
    	else {							//Otherwise: continue following bread crumbs.
    		return PrintPathHelp(vertex, root[vertex], originalVertex) + vertex + " -> ";
    	}
    }
        
	/* main()
	   Contains code to test the ShortestPath function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args) throws FileNotFoundException{
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
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			ShortestPath(G, 0);
                        PrintPaths(0);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			//System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
