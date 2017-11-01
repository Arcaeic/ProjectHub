import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Server {

    private static int[] paramArray = new int[3];
    private static final int PORT = 11112;
    private static ServerSocket server;
    private static Socket clientSocket;
	private static DataInputStream is;
	private static PrintStream os;
	private static String clientParams;

	private static void startServer() {
		System.out.println("Starting Server...");
		try {

			server = new ServerSocket(PORT);
			while(true){ connect(); }

		} catch (IOException e) { System.out.println("Could not establish I/O between client."); }
	}
	
	private static void connect(){

		try {
            findConnection();
            ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream());
            getClientParameters(objIn);

            if(parametersMatch()) { listen(); }
            else { close(); }
	
		} catch (IOException e) { e.printStackTrace(); }
	}

	private static boolean parametersMatch() {
        boolean sameConfig = Arrays.toString(paramArray).equals(clientParams);
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
        startServer();
    }
}