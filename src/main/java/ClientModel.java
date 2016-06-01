import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by m.farsiabi on 5/28/2016.
 */
public class ClientModel {
    private String terminalId;
    private String type;
    private String serverIP;
    private String serverPort;
    private String path;
    private List<Transaction> transactions;

    public ClientModel(String terminalId, String type, String serverIP, String serverPort, String path, List<Transaction> transactions) {
        this.terminalId = terminalId;
        this.type = type;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.path = path;
        this.transactions = transactions;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getPath() {
        return path;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void saveOutLog(String line) {
        System.err.println(line);
        try {
            RandomAccessFile outLogFile = new RandomAccessFile(ClientParser.clientModel.getPath(), "rw");
            outLogFile.seek(outLogFile.length());
            outLogFile.write(line.getBytes());
            outLogFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
