import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by m.farsiabi on 5/28/2016.
 */
public class ServerModel {
    private int clientPort;
    private List<Deposit> deposits;
    private String outLog;

    public ServerModel(int clientPort, List<Deposit> deposits, String outLog) {
        this.clientPort = clientPort;
        this.deposits = deposits;
        this.outLog = outLog;
    }

    public int getClientPort() {
        return clientPort;
    }

    public Deposit getDepositById(String id) {
        for (Deposit deposit : deposits) {
            if (deposit.getId().equals(id)) {
                return deposit;
            }
        }
        return null;
    }

    public String getOutLog() {
        return outLog;
    }

    public void saveOutLog(String line) {
        System.err.println(line);
        try {
            RandomAccessFile outLogFile = new RandomAccessFile(ServerParser.serverModel.getOutLog(), "rw");
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
