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
    	
		boolean enableConfidential = paramArray[0] == 1;
		boolean enableIntegrity = paramArray[1] == 1;
		boolean enableAuth = paramArray[2] == 1;
    	
    	try {
    		
    		//mutual certificate authentication if authentication is enabled
    		boolean authSuccess = false;
    		if(enableAuth){
    			authSuccess = authCertToServer();
    			if(!authSuccess){	//recall function if auth fails (unlimited auth attempts)
    				begin();
    			}
    		}else{
    			authSuccess = true;
    		}
    		
    		//establish session keys if confidential or integrity is necessary
			if(enableConfidential || enableIntegrity && authSuccess){
				
				//generate and store session keys
		        System.out.println("Client: connection to server open...");
		        initializeSessionKeys();
				System.out.println("Client: Session (master) Key: [" + SymKeyGen.encode64(masterKey) + "].");
				
		        //send master key to server
		    	PublicKey serverPubKey = keyStore.getCertificate("ServerCert").getPublicKey();
		    	byte[] encryptedSessionKeys = protectMasterKey(serverPubKey);
				System.out.println("Client: Master key [" + encryptedSessionKeys.length+"] encrypted with server's public key: "+ SymKeyGen.encode64(encryptedSessionKeys));
				objOut.write(encryptedSessionKeys);
				objOut.flush();
			}
				
			while(true){
        	   Message msg = null;
        	   try {
        		   
        		   	//ask for new message input
        		   	String message = inputMessagePrompt();
			        
			        //Wrap message in class; input params to control confidentiality and integrity
			        EncryptedMessage eMsg = new EncryptedMessage(message, sessionKeys[0], sessionKeys[1], enableConfidential, enableIntegrity);
			  
			        //write message
			        objOut.writeObject(eMsg);
					System.out.println("Client: waiting for server to respond. ");
			        
					//receive message from server
					if((msg = (Message) objIn.readObject()) != null){
						System.out.println("Client: message received.");
						EncryptedMessage recEMsg = ((EncryptedMessage) msg);
						
						String output = null;
						
						if(enableIntegrity){
							
							//verify message
							if(recEMsg.verifyMAC(sessionKeys[1])){
								System.out.println("Client: message verified.");
								
								//also decrypt message if necessary
								if(enableConfidential){
									output = recEMsg.decrypt(sessionKeys[0]);
								}else{
									output = new String(recEMsg.message);
								}
							}else{
								System.out.println("Client: message is INVALID.");
							}
						
						//no integrity checks
						}else{
							
							//also decrypt message if necessary
							if(enableConfidential){
								output = recEMsg.decrypt(sessionKeys[0]);
							}else{
								output = new String(recEMsg.message);
							}
						}
					
						System.out.println("Client: Message from server [" + output + "].");
					}else{
						System.out.println("Server: connection still open.......... ");
					}
					
				} catch (ClassNotFoundException | IOException e) {
			        System.out.println("Client: connection closed.");
					return;
				}
	           }
    					
		} catch (IOException | ClassNotFoundException | KeyStoreException e) {
			e.printStackTrace();
		}

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
    
    private static String inputMessagePrompt(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input message for server: ");
        String message = sc.nextLine();
        sc.close();
        return message;
    }
    
    private static void initializeSessionKeys(){
    	masterKey = SymKeyGen.generateMasterKey();
        sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(masterKey));
    }
    
    private static byte[] protectMasterKey(PublicKey key){
		byte[] encodedMasterKey = Base64.getEncoder().encode(masterKey);
		byte[] encryptedSessionKeys = KeyPairGen.encrypt((new String(encodedMasterKey)), key);
		return encryptedSessionKeys;
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

	public static void loginInterface() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("User: ");
		String user = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		UserDB db = new UserDB();
		if (db.authenticate(user, password)) {
			return;
		} else {
			System.out.println("Username or password is incorrect. Try again.");
			loginInterface();
		}
	}

	public static void main(String[] args) {

    textUI();
    clientParams = Arrays.toString(paramArray);
    if(paramArray[2] == 1) { loginInterface(); }
    connect();
	}

}

