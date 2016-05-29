import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by m.farsiabi on 5/28/2016.
 */
public class Client {

    public static void main(String[] args) {
        ClientParser clientParser = null;
        try {
            clientParser   = new ClientParser();
        } catch (ExceptionPool exceptionPool) {
            exceptionPool.printStackTrace();
        }

        String serverIP = new String (clientParser.getClientModel().getServerIP());
        String serverPort = new String (clientParser.getClientModel().getServerPort());

        System.out.println ("Attempting to connect to host " + serverIP + " on port " + serverPort);

        try {
            Socket clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));
            System.out.println("Connected Successfully");

            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
