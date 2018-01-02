import static java.lang.System.exit;
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
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

class Client {

	private static final String HOSTNAME = "localhost";
	private static Socket clientSocket;
    private static boolean[] clientParams = new boolean[3];
            static boolean enableC;
            static boolean enableI;
    private static boolean enableA;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static ObjectInputStream objIn;
    static ObjectOutputStream objOut;

    private static KeyStore keyStore;
    private static byte[] masterKey;
    static SecretKey[] sessionKeys = { null, null };

    static ClientMsgGUI gui;
	
	Client(){
	}

    /** startClient
     * 1. Creates a client socket and grabs client parameters from the GUI
     * 2. Loads the keyStore, initializes streams, and sends parameters to the server
     *  2b. Parameters are matched against server's parameters
     * 3. Begins to establishes a connection with the server
     */
    void startClient() {

		try {
			clientSocket = new Socket(HOSTNAME, 11112);
			gui.printStatus("Starting Client...");
			clientParams = gui.params;
			
			keyStore = KeyPairGen.loadClientKeyStore();
			initializeStreams();
			
			sendParameters();

			enableC = clientParams[0];
			enableI = clientParams[1];
			enableA = clientParams[2];

			beginConnection();
			
		} catch (UnknownHostException e) {
			gui.printStatus("ERROR! Could not locate " + HOSTNAME);
		} catch (IOException e) {
			gui.printStatus("ERROR! Could not initialize I/O streams.");
		}

	}

	private void initializeStreams() throws IOException {
		os = new DataOutputStream(clientSocket.getOutputStream());
		is = new DataInputStream(clientSocket.getInputStream());
		objOut = new ObjectOutputStream(clientSocket.getOutputStream());
		objIn = new ObjectInputStream(clientSocket.getInputStream());
	}

    /** sendParameters
     *  Sends parameters to the server to ensure both the server and client
     *  selected the same parameters
     */
    private void sendParameters() {
		try {

			Message newMessage = new Message(Arrays.toString(clientParams));
			objOut.writeObject(newMessage);
			boolean matches = objIn.readBoolean();

			if (!matches) {
				gui.printStatus("ERROR! Parameters do not match. Connection to Server closed.");
				close();
				exit(0);
			}

		} catch (IOException e) {
			gui.printStatus("ERROR! Could not send parameters.");
			//e.printStackTrace();
			//exit(-1);
		}
	}

    /** beginConnection
     * 1. Mutual Certificate Authentication if Authentication is enabled
     * 2. Establish session keys if Confidentiality or Integrity are enabled
     *  2a. Generate and store session keys
     *  2b. Send Master Key to server
     * 3. Receive messages in new thread
     */
    private void beginConnection() {

		try {

            if (enableA) {
                if (authCertToServer()) {
                    gui.printStatus("SUCCESS! Mutual authentication complete.");
                } else {
                    gui.printStatus("ERROR! Mutual authentication failed. Connection closed.");
                    close();
                    exit(-1);
                }
            } else {
				gui.printStatus("Connection to Server open.");
			}

			if (enableC || enableI) {

				initializeSessionKeys();
				gui.printStatus("Generated session key.");

				PublicKey serverPubKey = keyStore.getCertificate("ServerCert").getPublicKey();
				byte[] encryptedSessionKeys = protectMasterKey(serverPubKey);

				objOut.write(encryptedSessionKeys);
				objOut.flush();
				gui.printStatus("Sent session key to Server.");

			}
			gui.enableMessaging(true);


			try{
		
				Thread recv = receiveMessagesAsync();
				recv.run();

			} catch(Exception ex) {
				close();
				gui.printStatus("ERROR! Connection closed.");
				gui.enableMessaging(false);
		    }

		} catch (IOException | ClassNotFoundException | KeyStoreException e) {
			gui.printStatus("ERROR! Connection closed.");
			//e.printStackTrace();
		}

	}

    /** authClientCert
     * Reads in Client's Cert, then generates one for Server, then writes that out to Client.
     * @return true if Client and Server are mutually authenticated, false otherwise
     * @throws IOException If a stream is unable to write in or out
     * @throws ClassNotFoundException An object is not able to be r/w by the object streams
     */
    private boolean authCertToServer() throws IOException, ClassNotFoundException {

		Certificate clientCert = null;
		try {
			clientCert = keyStore.getCertificate("ClientCert");
		} catch (KeyStoreException e) {
			gui.printStatus("ERROR! Could not retrieve Client's certificate from the KeyStore.");
		}
		objOut.writeObject(clientCert);

		boolean validClientCert = objIn.readBoolean();
		if (!validClientCert) {
			gui.printStatus("ERROR! Client's certificate is invalid.");
			return false;
		}

		Certificate serverCert = (Certificate) objIn.readObject();
		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e) {
			gui.printStatus("ERROR! Could not retrieve Server's certificate from the KeyStore.");
		}

		boolean validServerCert = KeyPairGen.verifySignature(serverCert, caCert);
		if (!validServerCert) {
			gui.printStatus("ERROR! Server certificate is invalid.");
		}
		objOut.writeBoolean(validServerCert);
		objOut.flush();

		return validServerCert;
	}

    /** close
     *  Closes all the streams, including the client socket.
     */
    private void close() {
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

    /** initializeSessionKeys
     *  Initializes the masterKey and the sessionKeys
     */
    private void initializeSessionKeys() {
		masterKey = SymKeyGen.generateMasterKey();
		sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen
				.splitMasterKey(masterKey));
	}

	/**	protectedMasterKey
	 * Used to encrypt the Master Key before sending to server
	 * @param key The key used when encrypting the master key
	 * @return A byte[] representation of an encrypted master key
	 */
	private byte[] protectMasterKey(PublicKey key) {
		byte[] encodedMasterKey = Base64.getEncoder().encode(masterKey);
		return KeyPairGen.encrypt((new String(encodedMasterKey)), key);
	}

	/**	authenticate
	 * @param user The username checked against the UserDB
	 * @param password The password checked against the UserDB
	 * @return True if the username exists and the password matches
	 */
    boolean authenticate(String user, String password){
	    UserDB db = new UserDB();
	    
	    if(!db.authenticate(user, password)) {
	        gui.printStatus("Username or password is incorrect. Try again.");
	        return false;
        } else {
	        return true;
        }
	    
    }

	/**	receiveMessagesAsync
	 * @return Creates a new Thread to receive messages
	 */
	private Thread receiveMessagesAsync() {

		return new Thread(() -> {

            try {
                while (true) {
                    Object msg = objIn.readObject();
                    if (msg == null) {
                        continue;
                    }

                    EncryptedMessage recEMsg = (EncryptedMessage) msg;
                    if (enableI) {

                        if (recEMsg.verifyMAC(sessionKeys[1])) {
                            gui.printStatus("Message verified.");
                            printMessage("Server: " + getMessage(recEMsg));
                        } else {
                            gui.printStatus("ERROR! Verification failed.");
                        }

                    } else {
                        printMessage("Server: " + getMessage(recEMsg));
                    }
                }

            } catch (ClassNotFoundException | IOException ex) {
                gui.printStatus("ERROR! Can't receive messages.");
                //gui.printStatus(ex.toString());
                exit(-1);
            }
        });

	}

	private String getMessage(EncryptedMessage EncrMsg) {
	    String output;
        if (enableC) {
            output = EncrMsg.decrypt(sessionKeys[0]);
            gui.printStatus("Message decrypted.");
        } else {
            output = new String(EncrMsg.getMessage());
        }
        return output;
    }
	void printMessage(String message) {

		String header = "[" + String.format("%tF %<tT", new Date())+ "] ";
		gui.printMessageAsync(header + message);
	}
}
