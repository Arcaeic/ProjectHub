import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    private static final int PORT = 11112;
    private static ServerSocket server;
    private static Socket clientSocket;
	private static DataInputStream is;
	private static PrintStream os;
	private static String clientParams;

	private static void startServer(String[] args) {
		System.out.println("Starting Server...");
		try {

			server = new ServerSocket(PORT);
			while(true){ connect(args); }

		} catch (IOException e) { System.out.println("Could not establish I/O between client."); }
	}
	
	private static void connect(String[] args){

		try {
            findConnection();
            ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());
            getClientParameters(objIn);

            if(parametersMatch(args)) { listen(); }
            else { close(); }
	
		} catch (IOException e) { e.printStackTrace(); }
	}

	private static boolean parametersMatch(String[] args) {
        boolean sameConfig = Arrays.toString(args).equals(clientParams);
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

	private static void findConnection() {
        try {
            System.out.println("Server: Listening...");
            clientSocket = server.accept();
            System.out.println("Server: Connection available.");
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void getClientParameters(ObjectInputStream objIn) {
	    try{
            Message paramsMsg = (Message) objIn.readObject();
            clientParams = paramsMsg.get();
            System.out.println( "Server: Parameters Received: " + clientParams);
	    } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }

	private static void listen() {
	    try {

            while(is.read() != -1){
                System.out.println("Server: Connection to Client open");
            }
            System.out.println("Server: Connection to Client lost.");
            is.close();
        } catch (IOException e) { e.printStackTrace(); }

    }

    private static void close() {
	    try {
            is.close();
            os.close();
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
            return;
        }
        startServer(args);
    }
}