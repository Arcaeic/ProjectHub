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
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
    private static SecretKey[] sessionKeys;
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
			System.out.println("Could not establish I/O between client.");
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
			close();
		}

	}

	private static boolean parametersMatch() {
		boolean sameConfig = serverParams.equals(clientParams);

		if (sameConfig) {
			System.out.println("Server: Parameters match Client's");
			System.out.println("Server: Connection to Client established");

		} else {
			System.out.println("Server: MISMATCH! Client and Server parameters do not match.");
			System.out.println("Server: Severed connection to Client.");
        }

        try {
            objOut.writeBoolean(sameConfig);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sameConfig;
	}

	private static void waitForConnection() {
		try {

			System.out.println("Server: Listening...");
			clientSocket = server.accept();
			System.out.println("Server: Connection available.");
			initializeStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getClientParameters() {
		try {
			Message paramsMsg = (Message) objIn.readObject();
			clientParams = paramsMsg.get();
			System.out.println("Server: Client Parameters Received: "
					+ clientParams);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO need to protect integrity of this plaintext auth protocol?
	 * 
	 * @param "objIn"
	 * @return is the client authenticated
	 * @throws IOException
	 */
	private static boolean authenticateClient() {
		UserDB database = new UserDB();
		
		System.out.println("Server: Waiting for client to begin authentication.");
		Message authFromClient = null;
		try {
			authFromClient = (Message) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		String authInfo[] = authFromClient.get().split(":");
		System.out.println("Server: Received auth request from client["
				+ authInfo[0] + "] with password[" + authInfo[1] + "].");

		boolean success = false;
		if ((success = database.authenticate(authInfo[0], authInfo[1]))) {
			System.out.println("Server: Authentication success.");
		} else {
			System.out.println("Server: Autentication failure.");
		}

		try {
			objOut.writeBoolean(success);
			objOut.flush();
			System.out.println("Server: sent success = " + success);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}
	
	private static boolean authClientCert() {
		
		System.out.println("Server: Waiting for client to send certificate.");
		Certificate clientCert = null;
		try {
			clientCert = (Certificate) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		System.out.println("Server: Received certificate from client.");

		Certificate caCert = null;
		try {
			caCert = keyStore.getCertificate("ServerCert");
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean success = false;
		if (KeyPairGen.verifySignature(clientCert, caCert, caCert.getPublicKey())) {
			System.out.println("Server: Authentication success. Client cert is valid.");
			success = true;
		} else {
			System.out.println("Server: Autentication failure. Client cert is invalid.");
			return false;
		}

		try {
			objOut.writeBoolean(success);
			objOut.flush();
			System.out.println("Server: sent success = " + success);
			objOut.writeObject(caCert);
			System.out.println("Server: (is CA) sent own certificate to client.");
			success = objIn.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

	private static void begin() {

		/*
		 * TODO run authentication only when in agreed params
		 */
		if (authClientCert()) {

			System.out.println("Server: Connection to Client open...");

			// setup session key
			// TODO protect in transit with asymm keypairs
			sessionKeys = null;

			try {
				
				System.out.println("Server: waiting for encrypted session key from client.");
				PrivateKey serverPriKey = (PrivateKey) keyStore.getKey("ServerPrivate", "keypass".toCharArray());
				System.out.println("Server: retreived private key from keystore:" + Base64.encode(serverPriKey.getEncoded()));
				int encryptedMKeySizeBytes = SymKeyGen.SUB_KEY_SIZE * 8;
				byte[] encryptedMKey = new byte[encryptedMKeySizeBytes];
				System.out.println(objIn.read(encryptedMKey, 0, encryptedMKeySizeBytes) + "bytes read.");
				System.out.println("Server: Recieved encrypted session key: " + encryptedMKey.length);
				System.out.println("Server: encrypted session key: " + Base64.encode(encryptedMKey));

				String decryptedKey = KeyPairGen.decrypt(encryptedMKey, serverPriKey);
				
				//byte[] sessionKeyBytes = SymmetricKeyGen.decode64(encryptedSKey);
				sessionKeys = SymKeyGen.convertKeyBytes(SymKeyGen.splitMasterKey(Base64.decode((decryptedKey))));
			
				System.out.println("Server: master Key: [" + decryptedKey.getBytes()+ "].");


			} catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
				System.out.println("Server: Could not receive session key.");
				e.printStackTrace();
			}

		} else {
			System.out.println("Server: Connection to Client still open. Waiting for reauth.");
			begin();
			return;
		}

		// listen for any messages
		while (true) {
			Object msg = null;
			try {
				
				// receive message from client
				if ((msg = objIn.readObject()) != null) {
					System.out.println("Server: Message received from client... ");

					// TODO support plaintext message
					if (true) {
						// if is encrypted (C is in params)
						String decMessage = ((EncryptedMessage) msg).decrypt(sessionKeys[0]);
						System.out.println("Server: message decrypted: ["
								+ decMessage + "].");
					}
				}

				//send message to client

				//TODO add "send another message prompt" here
				Scanner sc = new Scanner(System.in);
				System.out.print("Message for client: ");
				String message = sc.nextLine();

				objOut.writeObject(new EncryptedMessage(message, sessionKeys[0]));

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void textUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ensure Confidentiality? (0/1): ");
        paramArray[0] = scanner.nextInt();
        System.out.print("Ensure Integrity? (0/1): ");
        paramArray[1] = scanner.nextInt();
        System.out.print("Ensure Authenticity? (0/1): ");
        paramArray[2] = scanner.nextInt();
    }

	public static void main(String[] args) {
		//Server is CA --> user must login to server.
		//TODO add server pass hash and user to UserDB
		//TODO add interactive login before all else
	    textUI();
		startServer();
	}
}