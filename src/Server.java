import static java.lang.System.exit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.SecretKey;


public class Server {

	private static final int PORT = 11112;
	private static int[] paramArray = new int[3];
	private static ServerSocket server;
	private static Socket clientSocket;

	private static DataInputStream is;
	private static DataOutputStream os;
	private static ObjectInputStream objIn;
	private static ObjectOutputStream objOut;
	private static String serverParams;
	private static String clientParams;
    private static SecretKey[] sessionKeys = {null, null};
    private static KeyStore keyStore;
	private static byte[] masterKey;



	private static void startServer() {

		// server params from command line
		serverParams = Arrays.toString(paramArray);

		System.out.println("Server: Starting Server with parameters " + serverParams);
		try {

			server = new ServerSocket(PORT);
			keyStore = KeyPairGen.loadServerKeyStore();
			while (true) {
				connect();
			}

		} catch (IOException e) {
			System.out.println("Server: ERROR! Could not start server.");
		}
	}

	private static void initializeStreams() {
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

	private static void connect() {

		waitForConnection();
		getClientParameters();

		if (parametersMatch()) {
			begin();
		} else {
			System.out.println("Server: Closed connection to Client.");
			close();
		}

	}

	private static boolean parametersMatch() {
		boolean sameConfig = serverParams.equals(clientParams);

		if (sameConfig) {
			System.out.println("Server: Parameters match Client's.");
			System.out.println("Server: Connection to Client established.");

		} else {
			System.out.println("Server: MISMATCH! Client and Server parameters do not match.");
        }

        try {
            objOut.writeBoolean(sameConfig);
            objOut.flush();
        } catch (IOException e) {
			System.out.println("Server: ERROR! Could not send parameters.");
            e.printStackTrace();
        }

        return sameConfig;
	}

	private static void waitForConnection() {
		try {
			System.out.println("Server: Listening...");
			clientSocket = server.accept();
			System.out.println("Server: Received connection request from Client.");
			initializeStreams();
		} catch (IOException e) {
			System.out.println("Server: ERROR! Could not accept connection from Client.");
			e.printStackTrace();
		}
	}

	private static void getClientParameters() {
		try {
			Message paramsMsg = (Message) objIn.readObject();
			clientParams = paramsMsg.get();
			//System.out.println("Server: Client Parameters Received: "+ clientParams);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Server: ERROR! Did not recieve client parameters.");
			e.printStackTrace();
		}
	}

	private static boolean authClientCert() {
		
		//System.out.println("Server: Waiting for client to send certificate.");
		Certificate clientCert = null;
		try {
			clientCert = (Certificate) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Server: ERROR! Could not receive Client's certificate.");
			e.printStackTrace();
		}

		//System.out.println("Server: Received certificate from client.");

		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e1) {
			System.out.println("Server: ERROR! Could not retrieve Server's certificate from the KeyStore.");
			e1.printStackTrace();
		}
		boolean success = false;
		if (KeyPairGen.verifySignature(clientCert, caCert, caCert.getPublicKey())) {
			//System.out.println("Server: SUCCESS! The Client is authenticated.");
			success = true;
		} else {
			System.out.println("Server: ERROR! The Client's certificate is invalid.");
			return false;
		}

		try {
			objOut.writeBoolean(success);
			objOut.flush();
			//System.out.println("Server: sent success = " + success);
			objOut.writeObject(caCert);
			//System.out.println("Server: (is CA) sent own certificate to client.");
			success = objIn.readBoolean();
			
			if(success){
				System.out.println("Server: SUCCESS! Mutual authentication complete.");
			}else{
				System.out.println("Server: ERROR! Server's certificate is invalid.");
			}
			
		} catch (IOException e) {
			System.out.println("Server: ERROR! Mutual authentication interrupted.");
		}

		return success;
	}

	private static void begin() {
		
		boolean enableConfidential = paramArray[0] == 1;
		boolean enableIntegrity = paramArray[1] == 1;
		boolean enableAuth = paramArray[2] == 1;
		
		boolean authSuccess = false;
		if(enableAuth){
			authSuccess = authClientCert();
			if(!authSuccess){
				System.out.println("Server: ERROR: Mutual authentication failed. Connection closed.");
				close();
				exit(-1);
			}
		}else{
			authSuccess = true;
		}
		
		//session key establishment if enabled
		if (enableConfidential || enableIntegrity && authSuccess) {

			try {
				System.out.println("Server: Connection to Client open.");
				
				sessionKeys = null;
				System.out.println("Server: Waiting for Client to begin session key establishment.");
				PrivateKey serverPriKey = (PrivateKey) keyStore.getKey("ServerPrivate", "keypass".toCharArray());
				//System.out.println("Server: retreived private key from keystore:" + SymKeyGen.encode64(serverPriKey.getEncoded()));
				int encryptedMKeySizeBytes = SymKeyGen.SUB_KEY_SIZE * 8;
				byte[] encryptedMKey = new byte[encryptedMKeySizeBytes];
				int bytes_read = objIn.read(encryptedMKey, 0, encryptedMKeySizeBytes);
				//System.out.println(bytes_read + "bytes read.");
				//System.out.println("Server: Recieved session key: " + encryptedMKey.length);
				//System.out.println("Server: encrypted session key: " + SymKeyGen.encode64(encryptedMKey));
	
				String decryptedKey = KeyPairGen.decrypt(encryptedMKey, serverPriKey);
				sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(SymKeyGen.decode64(decryptedKey)));
				//System.out.println("Server: master Key: [" + decryptedKey.getBytes()+ "].");
				System.out.println("Server: SUCCESS! Session key established.");
		
			} catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
				System.out.println("Server: Could not obtain session keys.");
				e.printStackTrace();
			}
		}

		//listen for any messages
		while (true) {
			Object msg = null;
			try {
				
				//receive message from client
				if ((msg = (Message) objIn.readObject()) != null) {
					EncryptedMessage recEMsg = ((EncryptedMessage) msg);
					
					String output = null;
					
					if(enableIntegrity){
						
						//verify message
						if(recEMsg.verifyMAC(sessionKeys[1])){
							System.out.println("Server: Message verified.");
							
							//also decrypt message if necessary
							if(enableConfidential){
								output = recEMsg.decrypt(sessionKeys[0]);
								System.out.println("Server: Message decrypted.");

							}else{
								output = new String(recEMsg.message);
							}
							
							System.out.println("Server: Message: [" + output + "].");

						}else{
							System.out.println("Server: ERROR! Verification failed.");
							System.out.println("Server: INVALID Message: [" + output + "].");

						}
					
					//no integrity checks
					}else{
						
						//also decrypt message if necessary
						if(enableConfidential){
							output = recEMsg.decrypt(sessionKeys[0]);
							System.out.println("Server: Message decrypted.");
						}else{
							output = new String(recEMsg.message);
						}
						
						System.out.println("Server: Message: [" + output + "].");
					}
				
				}

				//send message to client
				String message = inputMessagePrompt();
				objOut.writeObject(new EncryptedMessage(message, sessionKeys[0], sessionKeys[1], enableConfidential, enableIntegrity));

				System.out.println("Server: Waiting for Client's response. ");

			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Server: ERROR! Could not recieve/decrypt message. Connection closed.");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private static String inputMessagePrompt(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Message for Client: ");
        String message = sc.nextLine();
        return message;
    }

	public static void textUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Server: Ensure Confidentiality? (0/1): ");
        paramArray[0] = scanner.nextInt();
        System.out.print("Server: Ensure Integrity?       (0/1): ");
        paramArray[1] = scanner.nextInt();
        System.out.print("Server: Ensure Authenticity?    (0/1): ");
        paramArray[2] = scanner.nextInt();
    }

    public static void loginInterface() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Server: UserName: ");
	    String user = scanner.nextLine();
	    System.out.print("Server: Password: ");
	    String password = scanner.nextLine();
	    UserDB db = new UserDB();
	    if(db.authenticate(user, password)) {
	        return;
        } else {
	        System.out.println("Server: Username or password is incorrect. Try again.");
	        loginInterface();
        }
    }

	public static void main(String[] args) {
        textUI();
	    if(paramArray[2] == 1) { loginInterface(); }
		startServer();
	}
}