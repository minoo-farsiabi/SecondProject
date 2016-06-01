import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            readAndParseServerFile();
            serverSocket = new ServerSocket(ServerParser.serverModel.getClientPort());
            Set<Socket> clientSockets = new HashSet<Socket>();
            System.out.println("Waiting for connection.....");
            while (true) {
                Socket currentClientSocket = new Socket();
                currentClientSocket = serverSocket.accept();
                clientSockets.add(currentClientSocket);
                System.err.println(clientSockets.size());
                System.out.println("Connection successful");
                ServerParser.serverModel.saveOutLog("New client!\n");
                ServerThread serverThread = new ServerThread(currentClientSocket);
                serverThread.start();
            }

            //For Test
//            ServerThread thread1 = null;
//            ServerThread thread2 = null;
//            for (int i = 0; i < 2; i++) {
//                Socket currentClientSocket = new Socket();
//                currentClientSocket = serverSocket.accept();
//                clientSockets.add(currentClientSocket);
//                System.err.println(clientSockets.size());
//                System.out.println("Connection successful");
//                if (i == 0) {
//                    thread1 = new ServerThread(currentClientSocket);
//                } else {
//                    thread2 = new ServerThread(currentClientSocket);
//                }
//            }
//            while (true) {
//                thread1.start();
//                thread2.start();
//            }
            //End For Test!

        } catch (IOException e) {
            System.err.println("Could not listen on port:" + ServerParser.serverModel.getClientPort());
            e.printStackTrace();
            ServerParser.serverModel.saveOutLog("Server is shutting down...");
        }

        serverSocket.close();
        ServerParser.serverModel.saveOutLog("Server is shutting down...");
    }

    private static ServerParser readAndParseServerFile() throws FileNotFoundException {
        FileReader reader = new FileReader("core.json");
        ServerParser serverParser = new ServerParser(reader);
        serverParser.parseJSON();
        ServerParser.serverModel.saveOutLog("Reading core.json completed.\n");
        return serverParser;
    }
}
