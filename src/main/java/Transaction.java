/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class Transaction {
    private String id;
    private String type;
    private String amount;
    private String deposit;

    public Transaction(String id, String type, String amount, String deposit) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.deposit = deposit;
    }
}
