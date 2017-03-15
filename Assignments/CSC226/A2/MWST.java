/* MWST.java
   CSC 225 - Spring 2017
   Assignment 2 - Template for a Minimum Weight Spanning Tree algorithm
   Jory Anderson - V00843894
   February 12th, 2017
*/
import java.util.Scanner;
import java.io.File;
import java.util.Arrays;

public class MWST{


	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int mwst(int[][] G){
		int numVerts = G.length;
		int numEdges = 0;
		boolean[] visited = new boolean[numVerts];
		Arrays.fill(visited, false);
		int totalWeight = 0;
		visited[0] = true; //Start at 0

		int lowI = 0;
		int lowJ = 0;
		int minWeight = 0;
		while(numEdges < numVerts-1){ //By Definition: In an MST, E = V-1
			minWeight = 0; //Reset minWeight
			for(int i = 0; i < numVerts; i++){
				if(visited[i] == true){	//Check for discovered nodes		
					for(int j = 0; j < numVerts; j++){ //Look for edges in discovered node
						if(G[i][j] == 0) //If edge DNE
							continue;
						if(visited[j] == false){ //If undiscovered node, determine minWeight in adjacent edges
							if(minWeight == 0 || minWeight >= G[i][j]){
								minWeight = G[i][j];
								lowI = i;
								lowJ = j;
							}
						}
					}
				}
			}
			//Set new node to discovered
			visited[lowJ] = true;
			//Add edge to MST, inc numEdges
			totalWeight += G[lowI][lowJ];
			numEdges++;
		}
		return totalWeight;
	}


	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		int graphNum = 0;
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(!s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				G[i] = new int[n];
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			if (!isConnected(G)){
				System.out.printf("Graph %d is not connected (no spanning trees exist...)\n",graphNum);
				continue;
			}
			int totalWeight = mwst(G);
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
				
		}
	}

	/* isConnectedDFS(G, covered, v)
	   Used by the isConnected function below.
	   You may modify this, but nothing in this function will be marked.
	*/
	static void isConnectedDFS(int[][] G, boolean[] covered, int v){
		covered[v] = true;
		for (int i = 0; i < G.length; i++)
			if (G[v][i] > 0 && !covered[i])
				isConnectedDFS(G,covered,i);
	}
	   
	/* isConnected(G)
	   Test whether G is connected.
	   You may modify this, but nothing in this function will be marked.
	*/
	static boolean isConnected(int[][] G){
		boolean[] covered = new boolean[G.length];
		for (int i = 0; i < covered.length; i++)
			covered[i] = false;
		isConnectedDFS(G,covered,0);
		for (int i = 0; i < covered.length; i++)
			if (!covered[i])
				return false;
		return true;
	}

}