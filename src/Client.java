import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class Client {

    private static final String HOSTNAME = "localhost";
    private static Socket clientSocket;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static String clientParams;


	private static void connect() {
		
        try {
			clientSocket = new Socket(HOSTNAME, 11112);
	        System.out.println("Client: Socket created.");
			initializeStreams();
			sendParameters();
			begin();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOSTNAME);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
        }
        
        
    }

    private static void initializeStreams() throws IOException {
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
            objOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objIn = new ObjectInputStream(clientSocket.getInputStream());
    }

    private static void sendParameters(){
        try {

            Message newMessage = new Message(clientParams);
            System.out.println("Client: Parameters: " + newMessage.get());
            objOut.writeObject(newMessage);
            
        } catch (IOException e) {
            System.out.println("Client: connection to server closed. Params do not match");
            System.err.println("IOException:  " + e);
        }
        
        System.out.println("Client: connection to server established. Params match.");

    }
    
    private static void begin(){
    	//TODO only auth if A in agreed params
    	
    	try {
    		
    		boolean success = authenticateToServer();
    		
			if(success){
		        System.out.println("Client: connection to server open...");
				
		        //Scanner sc = new Scanner(System.in);
		        //System.out.print("Input message to server: ");
		        //String message = sc.nextLine();
		        
		        SymmetricKeyGen gen = new SymmetricKeyGen();
		        byte[] sessionKey = gen.generateSessionKey();
				System.out.println("Client: Session Key: [" + Base64.getEncoder().encodeToString(sessionKey)+ "].");
				
		        //send symmetric key to server
		        //TODO protect key with asymmetric encryption
		        objOut.write(sessionKey);
		        objOut.flush();
		        
		        
		        //send test plaintext message to server
		        //objOut.writeObject(new Message("hello there!"));
		        
		        //send test encrypted message
		        String encMsg = gen.encryptMessage("secret message!!!", sessionKey);
		        Message encMessage = new Message(encMsg);
				System.out.println("Client: Sending Encrypted and Encoded Message: [" + encMsg+ "].");
		        objOut.writeObject(encMessage);
		        
		       //listen for any messages
	           while(true){
	        	   Message msg = null;
	        	   try {
						if((msg = (Message)objIn.readObject()) != null){
							   System.out.println("Client: message received from server: " + msg.get());
						   }
						
						//possibly send another message
						//TODO sendMessagePrompt();
						
				        Scanner sc = new Scanner(System.in);
				        System.out.print("Message for server: ");
				        String message = sc.nextLine();
				        
				        //encrypt message and send
				        
						
							  
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
				        System.out.println("Client: connection closed.");

						e.printStackTrace();
						return;
					}
	           }
		            
	
    		}else{
    			begin();  //allow for unlimited auth attempts

    		}
    		
    		
    		System.out.println("Client: Exiting.");
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    private static boolean authenticateToServer() throws IOException, ClassNotFoundException{
    	
    	//authenticate
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter username: ");
		String userid = sc.nextLine();
		System.out.print("Enter password: ");
		String pass = sc.nextLine();
		
		//need to protect this plaintext information with encryption?
		Message authToServer = new Message(userid + ":" + pass);
		objOut.writeObject(authToServer);
		System.out.println("Client: auth info sent to server.");

		
		boolean authSuccess =  objIn.readBoolean();
		if(authSuccess){
			System.out.println("Client: Authentication Success.");
		}else{
			System.out.println("Client: Authentication Failure.");

		}
		return authSuccess;	
    }
    
    private static void sendMessagePrompt(){
    	
    }
    
    private static void sendPlaintextMessage(){
    	
    }
    
    private static void sendEncryptedMessage(){
    	
    }

	public static void main(String[] args) {
		
		clientParams = Arrays.toString(args);
		//for testing ease
		if(clientParams == null || clientParams.equals("[]")){
			clientParams = "[CIA]";
		}
		
	    if(args.length != 3) {
	        System.out.println("Missing one or more parameters. Please try again.");
	        System.out.println("FORMAT: \"java Client <C> <I> <A>\"");
	        System.out.println("Where <C>, <I>, <A> are represented with boolean logic.");
	        System.out.println("Usage: ");
	        System.out.println("\t <C> (Confidentiality): Encrypts the message b/w endpoints.");
	        System.out.println("\t <I> (Integrity): Ensures the content received by the server matches the content sent by the client.");
	        System.out.println("\t <A> (Authentication): [Placeholder] ");
	        //return;
        }
        connect();
	}
}
