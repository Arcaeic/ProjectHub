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
    private static SecretKey[] sessionKeys = {null, null};
    private static KeyStore keyStore;
	private static byte[] masterKey;

	private static void connect() {
		
        try {
			clientSocket = new Socket(HOSTNAME, 11112);
	        System.out.println("Client: Starting Client with parameters " + clientParams);
	        keyStore = KeyPairGen.loadClientKeyStore();
			initializeStreams();
			sendParameters();
			begin();
        } catch (UnknownHostException e) {
            System.err.println("Client: ERROR! Could not locate " + HOSTNAME);
        } catch (IOException e) {
            System.err.println("Client: ERROR! Could not initialize I/O streams.");
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
            //System.out.println("Client: Parameters: " + newMessage.get());
            objOut.writeObject(newMessage);
            boolean matches = objIn.readBoolean();

            if(!matches) {
                System.out.println("Client: ERROR! Parameters do not match. Connection to Server closed.");
                close();
                exit(-1);
            }
            
        } catch (IOException e) {
			System.out.println("Server: ERROR! Could not send parameters.");
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
    			if(!authSuccess){
    				System.out.println("Client: ERROR! Mutual authentication failed. Connection closed.");
    				close();
    				exit(-1);
    			}else{
    				System.out.println("Server: SUCCESS! Mutual authentication complete.");
    			}
    		}else{
    			authSuccess = true;
		        System.out.println("Client: Connection to Server open.");
    		}
    		
    		//establish session keys if confidential or integrity is necessary
			if(enableConfidential || enableIntegrity && authSuccess){
				
				//generate and store session keys
		        initializeSessionKeys();
		        System.out.println("Client: Generated session key.");

		        //send master key to server
		    	PublicKey serverPubKey = keyStore.getCertificate("ServerCert").getPublicKey();
		    	byte[] encryptedSessionKeys = protectMasterKey(serverPubKey);

		    	objOut.write(encryptedSessionKeys);
				objOut.flush();
		        System.out.println("Client: Sent session key to Server.");

			}
				
			while(true){
        	   Object msg = null;
        	   try {
        		   
        		   	//ask for new message input
        		   	String message = inputMessagePrompt();
        		   	
			        //Wrap message in class; input params to control confidentiality and integrity
			        EncryptedMessage eMsg = new EncryptedMessage(message, sessionKeys[0], sessionKeys[1], enableConfidential, enableIntegrity);
			  
			        //write message
			        objOut.writeObject(eMsg);
					System.out.println("Client: Waiting for Server's response.");
			        
					//receive message from server
					if((msg = (Message) objIn.readObject()) != null){
						//System.out.println("Client: message received.");
						EncryptedMessage recEMsg = ((EncryptedMessage) msg);
						
						String output = null;
						
						if(enableIntegrity){
							
							//verify message
							if(recEMsg.verifyMAC(sessionKeys[1])){
								System.out.println("Client: message verified.");
								
								//also decrypt message if necessary
								if(enableConfidential){
									output = recEMsg.decrypt(sessionKeys[0]);
									System.out.println("Client: Message decrypted.");

								}else{
									output = new String(recEMsg.getMessage());
								}
								
								System.out.println("Server: [" + output + "].");
							}else{
								System.out.println("Client: ERROR! Verification failed: [" + output + "].");
							}
						
						//no integrity checks
						}else{
							
							//also decrypt message if necessary
							if(enableConfidential){
								output = recEMsg.decrypt(sessionKeys[0]);
								System.out.println("Client: Message decrypted.");
							}else{
								output = new String(recEMsg.getMessage());
							}
							
							System.out.println("Server: [" + output + "].");
						}
					
					}
					
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Client: ERROR! Connection closed.");
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
			System.out.println("Client: ERROR! Could not retrieve Client's certificate from the KeyStore.");
			e.printStackTrace();
		}
		objOut.writeObject(clientCert);
		//System.out.println("Client: Certificate sent to server.");

		
		boolean validClientCert =  objIn.readBoolean();
		if(!validClientCert){
			System.out.println("Server: ERROR! Client's certificate is invalid.");
			return false;
		}
		
		Certificate serverCert = (Certificate) objIn.readObject();
		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e) {
			System.out.println("Client: ERROR! Could not retrieve Server's certificate from the KeyStore.");
			e.printStackTrace();
		}
		
		boolean validServerCert = KeyPairGen.verifySignature(serverCert, caCert, caCert.getPublicKey());
		if(!validServerCert){
			System.out.println("Client: ERROR! Server certificate is invalid.");
		}
		objOut.writeBoolean(validServerCert);
		objOut.flush();

		return validServerCert;	
    }
    
    private static String inputMessagePrompt(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Message for Server: ");
        String message = sc.nextLine();
        return message;
    }
    
	private static void close() {
		try {
			os.close();
			is.close();
			objIn.close();
			objOut.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	    System.out.print("Ensure Integrity?       (0/1): ");
	    paramArray[1] = scanner.nextInt();
	    System.out.print("Ensure Authenticity?    (0/1): ");
	    paramArray[2] = scanner.nextInt();
    }

	public static void loginInterface() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Client:                  User: ");
		String user = scanner.nextLine();
		System.out.print("Client:              Password: ");
		String password = scanner.nextLine();
		UserDB db = new UserDB();
		if (db.authenticate(user, password)) {
			return;
		} else {
			System.out.println("Client: Username or password is incorrect. Try again.");
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

