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

	private static ServerSocket server;
	private static Socket clientSocket;
	private static final int PORT = 11112;
	
	private static boolean[] serverParams;
	private static boolean[] clientParams;
	
	public static boolean enableA;
	public static boolean enableI;
	public static boolean enableC;

	private static DataInputStream is;
	private static DataOutputStream os;
	private static ObjectInputStream objIn;
	public static ObjectOutputStream objOut;

    public static SecretKey[] sessionKeys = {null, null};
    private static KeyStore keyStore;
    
    public static ServerMsgGUI gui;
    
    
    
	public void startServer() {

		serverParams = gui.params;
		enableC = serverParams[0];
		enableI = serverParams[1];
		enableA = serverParams[2];
		
		gui.printStatus("Starting Server...");

		try {

			server = new ServerSocket(PORT);
			keyStore = KeyPairGen.loadServerKeyStore();
			while (true) {
				connect();
			}
			
		} catch (IOException e) {
			gui.printStatus("ERROR! Could not start server.");
		}
	}
	
	private void connect() {

		waitForConnection();
		if (parametersMatch()) { 
			beginConnection(); 
		}
		else { closeSocketAndStreams(); }

	}

	private void waitForConnection() {
		try {
			gui.printStatus("Listening for Client...");
			clientSocket = server.accept();
			gui.printStatus("Received connection request from Client.");
			initializeStreams();
		} catch (IOException e) {
			gui.printStatus("ERROR! Could not accept connection from Client.");
			//e.printStackTrace();
		}
	}

	private String getClientParameters() {
		
		String clientParams = "";
		try {
			Message paramsMsg = (Message) objIn.readObject();
			clientParams = paramsMsg.get();
			//gui.printStatus("Server: Client Parameters Received: "+ clientParams);
		} catch (IOException | ClassNotFoundException e) {
			gui.printStatus("ERROR! Did not recieve client parameters.");
			//e.printStackTrace();
		}
		return clientParams;
		
	}


	private static void initializeStreams() throws IOException {

		objIn = new ObjectInputStream(clientSocket.getInputStream());
		objOut = new ObjectOutputStream(clientSocket.getOutputStream());
		is = new DataInputStream(clientSocket.getInputStream());
		os = new DataOutputStream(clientSocket.getOutputStream());
	
	}

	private boolean parametersMatch() {
		
		String serverP = Arrays.toString(serverParams);
		String clientP = getClientParameters();
		boolean sameConfig = serverP.equals(clientP);

		if (sameConfig) {
			gui.printStatus("Parameters match Client's.");
			gui.printStatus("Connection to Client established.");

		} else {
			gui.printStatus("ERROR! Client and Server parameters do not match.");
        }

        try {
            objOut.writeBoolean(sameConfig);
            objOut.flush();
        } catch (IOException e) {
			gui.printStatus("ERROR! Could not send parameters acknowledgement.");
            //e.printStackTrace();
        }

        return sameConfig;
	}

	private boolean authClientCert() {
		
		//gui.printStatus("Server: Waiting for client to send certificate.");
		Certificate clientCert = null;
		try {
			clientCert = (Certificate) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			gui.printStatus("ERROR! Could not receive Client's certificate.");
			//e.printStackTrace();
		}

		//gui.printStatus("Server: Received certificate from client.");

		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e1) {
			gui.printStatus("ERROR! Could not retrieve Server's certificate from the KeyStore.");
			//e1.printStackTrace();
		}
		boolean success = false;
		if (KeyPairGen.verifySignature(clientCert, caCert, caCert.getPublicKey())) {
			success = true;
		} else {
			gui.printStatus("ERROR! The Client's certificate is invalid.");
			return false;
		}

		try {
			objOut.writeBoolean(success);
			objOut.flush();
			objOut.writeObject(caCert);

			success = objIn.readBoolean();
			
			if(success){
				gui.printStatus("SUCCESS! Mutual authentication complete.");
			}else{
				gui.printStatus("ERROR! Server's certificate is invalid.");
			}
			
		} catch (IOException e) {
			gui.printStatus("ERROR! Mutual authentication interrupted.");
			success = false;
		}

		return success;
	}

	private void beginConnection() {
		
		boolean authSuccess = false;
		if(enableA){
			authSuccess = authClientCert();
			if(!authSuccess){
				gui.printStatus("ERROR! Mutual authentication failed. Connection closed.");
				closeSocketAndStreams();
				exit(-1);
			}
		}else{ authSuccess = true; }
		
		//session key establishment if enabled
		if (enableC || enableI && authSuccess) {

			try {
				gui.printStatus("Connection to Client open.");
				
				sessionKeys = null;
				gui.printStatus("Waiting for Client to begin session key establishment.");
				PrivateKey serverPriKey = (PrivateKey) keyStore.getKey("ServerPrivate", "keypass".toCharArray());

				int encryptedMKeySizeBytes = SymKeyGen.SUB_KEY_SIZE * 8;
				byte[] encryptedMKey = new byte[encryptedMKeySizeBytes];
				int bytes_read = objIn.read(encryptedMKey, 0, encryptedMKeySizeBytes);

				String decryptedKey = KeyPairGen.decrypt(encryptedMKey, serverPriKey);
				sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(SymKeyGen.decode64(decryptedKey)));
				gui.printStatus("SUCCESS! Session key established.");
				gui.enableMessaging(true);
				gui.printStatus("You can now send messages.");
		
			} catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
				gui.printStatus("ERROR! Could not obtain/decrypt session keys.");
				//e.printStackTrace();
			}
		}

		try{
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
			
			Server serv = new Server();
			Callable<Boolean> t = serv.new ReceiveMessagesTask();
	        Future<Boolean> future = executor.submit(t);
	        
	        boolean finish = false;
	        try {
	        	finish = future.get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
	        
			if(!finish){
				throw new Exception("message reception exception");
			}
			
		}catch(Exception ex) // catch the wrapped exception sent from within the thread
	    {
			closeSocketAndStreams();
			gui.setVisible(false);
			gui.printStatus("Server: ERROR! Connection closed.");
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

							if (enableI) {

								// verify message
								if (recEMsg.verifyMAC(sessionKeys[1])) {
									gui.printStatus("Message verified.");

									// also decrypt message if necessary
									if (enableC) {
										output = recEMsg
												.decrypt(sessionKeys[0]);
										gui.printStatus("Message decrypted.");

									} else {
										output = new String(
												recEMsg.getMessage());
									}

									printMessage("Client: " + output);
								} else {
									gui.printStatus("ERROR! Verification failed: ["
											+ output + "].");
								}

								// no integrity checks
							} else {

								// also decrypt message if necessary
								if (enableC) {
									output = recEMsg.decrypt(sessionKeys[0]);
									gui.printStatus("Message decrypted.");
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

		gui.printStatus(message);

	}

	public static void printMessage(String message) {
		String header = "[" + String.format("%tF %<tT.%<tL", new Date())+ "] ";
		gui.printMessageAsync(header + message);
	}

	private static void closeSocketAndStreams() {
		try {
			os.close();
			is.close();
			objIn.close();
			objOut.close();
			clientSocket.close();
		} catch (IOException e) { e.printStackTrace(); }
	}

	public boolean authenticate(String user, String password){
	    UserDB db = new UserDB();
	    
	    boolean success = false;
	    if(!db.authenticate(user, password)) {
	        gui.printStatus("Username or password is incorrect. Try again.");
        }else{
        	success = true;
        }
		return success;
	    
    }


}