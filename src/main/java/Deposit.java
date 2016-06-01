import java.math.BigDecimal;

/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class Deposit {
    private String customer;
    private String id;
    private BigDecimal initialBalance;
    private BigDecimal upperBound;

    public Deposit(String customer, String id, BigDecimal initialBalance, BigDecimal upperBound) {
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

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }
}
