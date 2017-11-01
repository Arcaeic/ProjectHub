import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    private static int[] paramArray = new int[3];
    private static final String HOSTNAME = "localhost";
    private static Socket clientSocket;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static ObjectOutputStream objOut;

	private static void connect() {
        createSocketStreams();
        if (clientSocket != null && os != null && is != null) {
            sendParameters();
        }
    }

    private static void createSocketStreams() {
        try {
            clientSocket = new Socket(HOSTNAME, 11112);
            System.out.println("Client socket created.");
            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());
            objOut = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOSTNAME);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + HOSTNAME);
        }
    }

    private static void sendParameters() {
        try {

            Message newMessage = new Message(Arrays.toString(paramArray));
            System.out.println("Parameters: " + newMessage.get());
            objOut.writeObject(newMessage);
            System.out.println("Client: Sent parameters to server.");
            if(is.read() == -1) {
                System.out.println("Client: Server parameters do not match Client parameters.\nExiting...");
            } else {
                System.out.println("Client: Parameters match. Connection established.");
            }

        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {
            System.out.println("Client: connection to server closed");
            System.err.println("IOException:  " + e);
        }
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
        connect();
	}
}
