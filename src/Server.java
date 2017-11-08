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
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.SecretKey;


public class Server {

	private static final int PORT = 11112;
	private static int[] paramArray = new int[3];
	public static boolean enableAuth;
	public static boolean enableIntegrity;
	public static boolean enableConfidential;
	private static ServerSocket server;
	private static Socket clientSocket;

	private static DataInputStream is;
	private static DataOutputStream os;
	private static ObjectInputStream objIn;
	public static ObjectOutputStream objOut;

	private static String serverParams;
	private static String clientParams;

    public static SecretKey[] sessionKeys = {null, null};
    private static KeyStore keyStore;
    public static ServerMsgGUI gui;


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
		} catch (IOException e) { e.printStackTrace(); }
	}

	private static void connect() {

		waitForConnection();
		getClientParameters();
		if (parametersMatch()) { 
			//gui = new ServerMsgGUI("Server - Chat Logs");
			//gui.createGUI();
			begin(); 
		}
		else { close(); }

	}

	private static boolean parametersMatch() {
		boolean sameConfig = serverParams.equals(clientParams);

		if (sameConfig) {
			System.out.println("Server: Parameters match Client's.");
			System.out.println("Server: Connection to Client established.");

		} else {
			System.out.println("Server: ERROR! Client and Server parameters do not match.");
        }

        try {
            objOut.writeBoolean(sameConfig);
            objOut.flush();
        } catch (IOException e) {
			//System.out.println("Server: ERROR! Could not send parameters.");
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
				System.out.println("Client: ERROR! Server's certificate is invalid.");
			}
			
		} catch (IOException e) {
			System.out.println("Server: ERROR! Mutual authentication interrupted.");
		}

		return success;
	}

	private static void begin() {
		
		boolean authSuccess = false;
		if(enableAuth){
			authSuccess = authClientCert();
			if(!authSuccess){
				System.out.println("Server: ERROR! Mutual authentication failed. Connection closed.");
				close();
				exit(-1);
			}
		}else{ authSuccess = true; }
		
		//session key establishment if enabled
		if (enableConfidential || enableIntegrity && authSuccess) {

			try {
				System.out.println("Server: Connection to Client open.");
				
				sessionKeys = null;
				System.out.println("Server: Waiting for Client to begin session key establishment.");
				PrivateKey serverPriKey = (PrivateKey) keyStore.getKey("ServerPrivate", "keypass".toCharArray());

				int encryptedMKeySizeBytes = SymKeyGen.SUB_KEY_SIZE * 8;
				byte[] encryptedMKey = new byte[encryptedMKeySizeBytes];
				int bytes_read = objIn.read(encryptedMKey, 0, encryptedMKeySizeBytes);

				String decryptedKey = KeyPairGen.decrypt(encryptedMKey, serverPriKey);
				sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(SymKeyGen.decode64(decryptedKey)));
				System.out.println("Server: SUCCESS! Session key established.");
				
		
			} catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
				System.out.println("Server: Could not obtain/decrypt session keys.");
				e.printStackTrace();
			}
		}

		try{
			
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
			
			Server serv = new Server();
			Callable<Boolean> t = serv.new ReceiveMessagesTask();
	        Future<Boolean> future = executor.submit(t);
	        
			//sendMessagesWhile();
	        
	        boolean finish = false;
	        try {
	        	finish = future.get();

			} catch (InterruptedException e) {
				System.out.println("interrupted");
				e.printStackTrace();
				throw e;
			} catch (ExecutionException e) {
				System.out.println("execution");
				e.printStackTrace();
				throw e;
			}
	        
			if(!finish){
				throw new Exception("");
			}

			
		}catch(Exception ex) // catch the wrapped exception sent from within the thread
	    {
			close();
			gui.setVisible(false);
			System.out.println("Server: ERROR! Connection closed.");
			return;
	    }
		

	}
	
	private static Thread receiveMessagesAsync() throws IOException{

		return new Thread() {

			public void run() {
				
				try {
					while (true) {

						Object msg = null;
						
							if ((msg = (Message) objIn.readObject()) != null) {
								EncryptedMessage recEMsg = ((EncryptedMessage) msg);

								String output = null;

								if (enableIntegrity) {

									// verify message
									if (recEMsg.verifyMAC(sessionKeys[1])) {
										System.out
										.println("Server: message verified.");

										// also decrypt message if necessary
										if (enableConfidential) {
											output = recEMsg
													.decrypt(sessionKeys[0]);
											System.out
											.println("Server: Message decrypted.");

										} else {
											output = new String(
													recEMsg.getMessage());
										}

										printMessage("Client: " + output);
									} else {
										System.out
										.println("Server: ERROR! Verification failed: ["
												+ output + "].");
									}

									// no integrity checks
								} else {

									// also decrypt message if necessary
									if (enableConfidential) {
										output = recEMsg.decrypt(sessionKeys[0]);
										System.out
										.println("Server: Message decrypted.");
									} else {
										output = new String(recEMsg.getMessage());
									}

									printMessage("Client: " + output);
								}

							}
						
					}
				} catch (ClassNotFoundException | IOException e) {
					
						throw new RuntimeException(e);
					

				}
			}

		};

	}

	public static void sendMessagesWhile() {

		try {
		while(true){
				
					// ask for new message input
					String message = inputMessagePrompt();

					if (!message.equals("") && message != null) {

						// Wrap message in class; input params to control
						// confidentiality
						// and integrity
						EncryptedMessage eMsg = new EncryptedMessage(message,
								sessionKeys[0], sessionKeys[1],
								enableConfidential, enableIntegrity);

						// write message
						objOut.writeObject(eMsg);
						printMessage("Server: " + message);

						// System.out.println("Client: Waiting for Server's response.");
					}
				

			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}
	
	class ReceiveMessagesTask implements Callable<Boolean> {
	    public Boolean call() throws Exception {
	    	try {
				while (true) {

					Object msg = null;
					
						if ((msg = (Message) objIn.readObject()) != null) {
							EncryptedMessage recEMsg = ((EncryptedMessage) msg);

							String output = null;

							if (enableIntegrity) {

								// verify message
								if (recEMsg.verifyMAC(sessionKeys[1])) {
									System.out
									.println("Server: message verified.");

									// also decrypt message if necessary
									if (enableConfidential) {
										output = recEMsg
												.decrypt(sessionKeys[0]);
										System.out
										.println("Server: Message decrypted.");

									} else {
										output = new String(
												recEMsg.getMessage());
									}

									printMessage("Client: " + output);
								} else {
									System.out
									.println("Server: ERROR! Verification failed: ["
											+ output + "].");
								}

								// no integrity checks
							} else {

								// also decrypt message if necessary
								if (enableConfidential) {
									output = recEMsg.decrypt(sessionKeys[0]);
									System.out
									.println("Server: Message decrypted.");
								} else {
									output = new String(recEMsg.getMessage());
								}

								printMessage("Client: " + output);
							}

						}
					
				}
			} catch (ClassNotFoundException | IOException e) {
				
					return new Boolean(false);				

			}
	    	
	    }
	}

	public static void printStatus(String message) {

		System.out.println("\nServer: " + message);

	}

	public static void printMessage(String message) {

		String header = "[" + String.format("%tF %<tT.%<tL", new Date())+ "] ";
		gui.printMessageAsync(header + message);
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
	
    private static String inputMessagePrompt(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Message for Client: ");
        return sc.nextLine();
    }

	private static void textUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Server: Ensure Confidentiality? (0/1): ");
        paramArray[0] = scanner.nextInt();
        System.out.print("Server: Ensure Integrity?       (0/1): ");
        paramArray[1] = scanner.nextInt();
        System.out.print("Server: Ensure Authenticity?    (0/1): ");
        paramArray[2] = scanner.nextInt();
    }

    private static void loginInterface() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Server:                      Username: ");
	    String user = scanner.nextLine();
	    System.out.print("Server:                      Password: ");
	    String password = scanner.nextLine();
	    UserDB db = new UserDB();
	    if(!db.authenticate(user, password)) {
	        System.out.println("Server: Username or password is incorrect. Try again.");
	        loginInterface();
        }
    }

	public static void main(String[] args) {
        textUI();
		enableConfidential = paramArray[0] == 1;
		enableIntegrity = paramArray[1] == 1;
		enableAuth = paramArray[2] == 1;
	    if(paramArray[2] == 1) { loginInterface(); }
		startServer();
	}
	
	public void start() {
        textUI();
		enableConfidential = paramArray[0] == 1;
		enableIntegrity = paramArray[1] == 1;
		enableAuth = paramArray[2] == 1;
	    if(paramArray[2] == 1) { loginInterface(); }
		startServer();
	}
}