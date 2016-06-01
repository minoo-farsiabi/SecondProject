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

    public void setTerminalId(String terminalId) {

        this.terminalId = terminalId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getTerminalId() {

        return terminalId;
    }

    public String getType() {
        return type;
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
