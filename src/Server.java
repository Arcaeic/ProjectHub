import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Server {

    private static final int PORT = 11112;
    private static ServerSocket server;
    private static Socket clientSocket;
	private static DataInputStream is;
	private static DataOutputStream os;
	private static ObjectInputStream objIn;
	private static ObjectOutputStream objOut;
	private static String serverParams;
	private static String clientParams;
	private static byte[] sessionKey;


	private static void startServer(String[] args) {
		
		//server params from command line
		serverParams = Arrays.toString(args);
		//for testing ease
		if(serverParams == null || serverParams.equals("[]")){
			serverParams = "[CIA]";
		}
		
		System.out.println("Server: Starting Server with parameters["+ serverParams +"]");
		try {

			server = new ServerSocket(PORT);
			while(true){ connect(); }

		} catch (IOException e) { System.out.println("Could not establish I/O between client."); }
	}
	
	private static void initializeStreams(){
        try {
			objIn = new ObjectInputStream(clientSocket.getInputStream());
			objOut = new ObjectOutputStream(clientSocket.getOutputStream());

            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
            /*
             * add additional streams if necessary
             */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void connect(){

            waitForConnection();
            getClientParameters();

            if(parametersMatch()) { begin(); }
            else { close(); }
	
	}

	private static boolean parametersMatch() {
        boolean sameConfig = serverParams.equals(clientParams);
        if(sameConfig){
            System.out.println("Server: Parameters match Client's");
            System.out.println("Server: Connection to Client established");
            return true;
        }else{
            System.out.println("Server: MISMATCH! Client and Server parameters do not match.");
            System.out.println("Server: Severed connection to Client.");
            return false;
        }
    }

	private static void waitForConnection() {
        try {
            System.out.println("Server: Listening...");
            clientSocket = server.accept();
            System.out.println("Server: Connection available.");
            initializeStreams();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void getClientParameters() {
	    try{
            Message paramsMsg = (Message) objIn.readObject();
            clientParams = paramsMsg.get();
            System.out.println( "Server: Client Parameters Received: " + clientParams);
	    } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }
    
    
    /**
     * TODO need to protect integrity of this plaintext auth protocol?
     * 
     * @param objIn
     * @return is the client authenticated
     * @throws IOException 
     */
    private static boolean authenticateClient(){
		UserDB database = new UserDB();
		
		System.out.println("Server: Waiting for client to begin authentication.");
		Message authFromClient = null;
		try {
			authFromClient = (Message) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		String authInfo[] = authFromClient.get().split(":");
		System.out.println("Server: Received auth request from client[" + authInfo[0] + "] with password["+ authInfo[1] +"].");
		
		boolean success = false;
		if((success = database.authenticate(authInfo[0], authInfo[1]))){
			System.out.println("Server: Authentication success.");
		}else{
			System.out.println("Server: Autentication failure.");
		}
		
		try {
			objOut.writeBoolean(success);
			objOut.flush();
			System.out.println("Server: sent success = " + success );

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return success;
    }

	private static void begin() {
	
	    	/*
	    	 * TODO run authentication only when in agreed params
	    	*/
	    	if(authenticateClient()){

	           System.out.println("Server: Connection to Client open...");

	           //setup session key
	           //TODO protect in transit
	           sessionKey = new byte[16];
	           
	           try {
					objIn.read(sessionKey);
					System.out.println("Server: Session Key: [" + Base64.encode(sessionKey)+ "].");

				} catch (IOException e) {
					System.out.println("Server: Could not receive session key.");
				}
	           
	           
	           
	    	}else{
		           System.out.println("Server: Connection to Client still open. Waiting for reauth.");
		           begin();
	    	}
	    	
	    	//listen for any messages
           while(true){
        	   Message msg = null;
        	   try {
        		   
        		   //receive message
        		   if((msg = (Message) objIn.readObject()) != null){
       			   	
        			   
					   System.out.println("Server: message received from client... ");
					   
					   //TODO support plaintext message
					   if(true){
						   //if is encrypted
						   System.out.println("Server: Encrypted message: ["+msg.get()+"].");

						   String decMessage = SymmetricKeyGen.decryptMessage(msg.get(),sessionKey);
						   System.out.println("Server: message decrypted: ["+decMessage+"].");
					   }
				}
        		   
        		   //send message
        		   
				 //TODO add "send another message prompt" here
        		    Scanner sc = new Scanner(System.in);
			        System.out.print("Message for client: ");
			        String message = sc.nextLine();
				   
			        objOut.writeObject(new Message(SymmetricKeyGen.encryptMessage(message, sessionKey)));
					
			        System.out.println("Server: waiting for client to respond. ");

				   
        		   
						   

				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
			        System.out.println("Server: connection closed.");

					e.printStackTrace();
					return;
				}
           }
            


    }

    private static void close() {
	    try {
	    	os.close();
            is.close();
            objIn.close();
            objOut.close();
            clientSocket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
    	
        if(args.length != 3) {
            System.out.println("Missing one or more parameters. Please try again.");
            System.out.println("FORMAT: \"java Server <C> <I> <A>\"");
            System.out.println("Where <C>, <I>, <A> are represented with boolean logic.");
            System.out.println("Usage: ");
            System.out.println("\t <C> (Confidentiality): Encrypts the message b/w endpoints.");
            System.out.println("\t <I> (Integrity): Ensures the content received by the server matches the content sent by the client.");
            System.out.println("\t <A> (Authentication): [Placeholder] ");
            //return;
        }
        startServer(args);
    }
}