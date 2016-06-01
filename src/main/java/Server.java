import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerParser serverParser = null;
        ServerSocket serverSocket = null;
        try {
            serverParser = readAndParseServerFile();
            serverSocket = new ServerSocket(serverParser.serverModel.getClientPort());
            Set<Socket> clientSockets = new HashSet<Socket>();
            System.out.println("Waiting for connection.....");
            while (true) {
                Socket currentClientSocket = new Socket();
                currentClientSocket = serverSocket.accept();
                clientSockets.add(currentClientSocket);
                System.err.println(clientSockets.size());
                System.out.println("Connection successful");
                serverParser.serverModel.saveOutLog("New client!\n");
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
            System.err.println("Could not listen on port:" + serverParser.serverModel.getClientPort());
            e.printStackTrace();
            serverSocket.close();
            serverParser.serverModel.saveOutLog("Server is shutting down...");
        }

        serverSocket.close();
        serverParser.serverModel.saveOutLog("Server is shutting down...");
    }

    private static ServerParser readAndParseServerFile() throws FileNotFoundException {
        ServerParser serverParser = null;
        FileReader reader = new FileReader("core.json");
        serverParser = new ServerParser(reader);
        serverParser.parseJSON();
        serverParser.serverModel.saveOutLog("Reading core.json completed.\n");
        return serverParser;
    }
}
