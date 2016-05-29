import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerParser serverParser = null;
        try {
            serverParser = readServerFile();
        } catch (ExceptionPool exceptionPool) {
            exceptionPool.printStackTrace();
        }
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(serverParser.serverModel.getClientPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + serverParser.serverModel.getClientPort());
            System.exit(1);
        }

        Socket clientSocket = null;
        System.out.println("Waiting for connection.....");

        try {
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection successful");
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.err.println("Accept failed.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ServerParser readServerFile() throws ExceptionPool {
        ServerParser serverParser = null;
        try {
            FileReader reader = new FileReader("core.json");
            serverParser = new ServerParser(reader);
            return serverParser;
        } catch (FileNotFoundException e) {
            throw new ExceptionPool("File not found!");
        }
    }
}
