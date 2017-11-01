import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Client {

    private static final String HOSTNAME = "localhost";
    private static Socket clientSocket;
    private static DataOutputStream os;
    private static DataInputStream is;
    private static ObjectOutputStream objOut;

	private static void connect(String[] args) {
        createSocketStreams();
        if (clientSocket != null && os != null && is != null) {
            sendParameters(args);
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

    private static void sendParameters(String[] args) {
        try {

            Message newMessage = new Message(Arrays.toString(args));
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

	public static void main(String[] args) {
	    if(args.length != 3) {
	        System.out.println("Missing one or more parameters. Please try again.");
	        System.out.println("FORMAT: \"java Client <C> <I> <A>\"");
	        System.out.println("Where <C>, <I>, <A> are represented with boolean logic.");
	        System.out.println("Usage: ");
	        System.out.println("\t <C> (Confidentiality): Encrypts the message b/w endpoints.");
	        System.out.println("\t <I> (Integrity): Ensures the content received by the server matches the content sent by the client.");
	        System.out.println("\t <A> (Authentication): [Placeholder] ");
	        return;
        }
        connect(args);
	}
}
