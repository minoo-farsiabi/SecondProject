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

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public String getOutLog() {
        return outLog;
    }
}
