import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Base64;
import javax.crypto.SecretKey;

import static java.lang.System.exit;

public class Client {

    private static int[] paramArray = new int[3];
    private static final String HOSTNAME = "localhost";
    private static Socket clientSocket;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static String clientParams;
    private static SecretKey[] sessionKeys;
    private static KeyStore keyStore;
	private static byte[] masterKey;

	private static void connect() {
		
        try {
			clientSocket = new Socket(HOSTNAME, 11112);
	        System.out.println("Client: Socket created.");
	        keyStore = KeyPairGen.loadClientKeyStore();
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
            boolean matches = objIn.readBoolean();

            if(!matches) {
                System.out.println("Client: Connection to Server closed. Parameters do not match.");
                exit(-1);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            exit(-1);
        }
    }
    
    private static void begin(){
    	//TODO only auth if A in agreed params
    	try {
    		boolean success = authCertToServer();
			if(success){
		        System.out.println("Client: connection to server open...");
		        SymKeyGen master = new SymKeyGen();
		        masterKey = SymKeyGen.generateMasterKey();
		        SymKeyGen gen = new SymKeyGen();
		        sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(masterKey));
		        
				System.out.println("Client: Session (master) Key: [" + SymKeyGen.encode64(masterKey) + "].");
				
		        //send symmetric key to server
				
					PublicKey serverPubKey = keyStore.getCertificate("ServerCert").getPublicKey();
					byte[] encodedMasterKey = Base64.getEncoder().encode(masterKey);
					byte[] encryptedSessionKeys = KeyPairGen.encrypt((new String(encodedMasterKey)), serverPubKey);
					//String encKey = Base64.getEncoder().encodeToString(encryptedSessionKey);
					System.out.println("Client: Master key [" + encryptedSessionKeys.length+"] encrypted with server's public key: "+ Base64.getEncoder().encodeToString(encryptedSessionKeys));
					objOut.write(encryptedSessionKeys);
					objOut.flush();

		        //send test plaintext message to server
		        //objOut.writeObject(new Message("hello there!"));
		        
		        //send test encrypted message
		        /*
		        String encMsg = gen.encryptMessage("secret message!!!", sessionKey);
		        Message encMessage = new Message(encMsg);
				System.out.println("Client: Sending Encrypted and Encoded Message: [" + encMsg+ "].");
		        objOut.writeObject(encMessage);
		        objOut.flush();
		        */
		        
		       //listen for any messages
	           while(true){
	        	   Message msg = null;
	        	   try {
	        		   
	        		   //send to server
	        		   //possibly send another message
	        		   //TODO sendMessagePrompt();
						
				        Scanner sc = new Scanner(System.in);
				        System.out.print("Input message for server: ");
				        String message = sc.nextLine();
				        
				        //encrypt message and send
				        EncryptedMessage eMsg = new EncryptedMessage(message, sessionKeys[0]);
				        
				        objOut.writeObject(eMsg);
						System.out.println("Client: waiting for server to respond. ");
				        
	        		   //receive from server
						if((msg = (Message) objIn.readObject()) != null){
							System.out.println("Client: message received: [" + ((EncryptedMessage) msg).decrypt(sessionKeys[0])+ "].");
						}else{
							System.out.println("Server: connection still open.......... ");
						}
						
					} catch (ClassNotFoundException | IOException e) {
				        System.out.println("Client: connection closed.");
						return;
					}
	           }
		            
	
    		}else{
    			begin();  //allow for unlimited auth attempts
    		}
    					
		} catch (IOException | ClassNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		}

    }
    
    private static boolean authPassToServer() throws IOException, ClassNotFoundException{
    	
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
    
    private static boolean authCertToServer() throws IOException, ClassNotFoundException{
    	
        Certificate clientCert = null;
		try {
			clientCert = keyStore.getCertificate("ClientCert");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objOut.writeObject(clientCert);
		System.out.println("Client: Certificate sent to server.");

		
		boolean validClientCert =  objIn.readBoolean();
		if(validClientCert){
			System.out.println("Client: Authentication Success. Valid client cert.");
		}else{
			System.out.println("Client: Authentication Failure. Invalid client cert.");
			return false;
		}
		
		Certificate serverCert = (Certificate) objIn.readObject();
		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean validServerCert = KeyPairGen.verifySignature(serverCert, caCert, caCert.getPublicKey());
		if(validServerCert){
			System.out.println("Client: Server certificate is valid.");
		}else{
			System.out.println("Client: Server certificate is invalid!");
		}
		objOut.writeBoolean(validServerCert);
		objOut.flush();
		System.out.println("Client: sent success = "+ validServerCert);


		return validServerCert;	
    }
    
    private static void sendMessagePrompt(){
    	
    }
    
    private static void sendPlaintextMessage(){
    	
    }
    
    private static void sendEncryptedMessage(){
    	
    }

    private static void textUI() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Ensure Confidentiality? (0/1): ");
	    paramArray[0] = scanner.nextInt();
	    System.out.print("Ensure Integrity? (0/1): ");
	    paramArray[1] = scanner.nextInt();
	    System.out.print("Ensure Authenticity? (0/1): ");
	    paramArray[2] = scanner.nextInt();
    }

	public static void main(String[] args) {

    textUI();
    clientParams = Arrays.toString(paramArray);
    connect();
	}
	
	private class SessionKey{
		
		
		
		public SessionKey(SecretKey key){
			
		}
	}


}

