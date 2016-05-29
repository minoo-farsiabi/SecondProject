import java.io.*;
import java.net.Socket;

/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class ServerThread extends Thread {
    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String line = "";

            while ((line = in.readLine()) != null) {
                String clientCommand = in.readLine();
                System.out.println("Client Says :" + clientCommand);
                    out.println(clientCommand);
                    out.flush();
            }
        }catch(IOException e)

        {
            e.printStackTrace();
        }
    }
}
