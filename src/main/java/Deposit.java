/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class Deposit {
    private String customer;
    private String id;
    private String initialBalance;
    private String upperBound;

    public Deposit(String customer, String id, String initialBalance, String upperBound) {
        this.customer = customer;
        this.id = id;
        this.initialBalance = initialBalance;
        this.upperBound = upperBound;
    }

    public String getCustomer() {
        return customer;
    }

    public String getId() {
        return id;
    }

    public String getInitialBalance() {
        return initialBalance;
    }

    public String getUpperBound() {
        return upperBound;
    }
}
