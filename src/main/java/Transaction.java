import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class Transaction implements Serializable {
    private String id;
    private String type;
    private BigDecimal amount;
    private BigDecimal deposit;

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public String getId() {
        return id;

    }

    public Transaction(String id, String type, BigDecimal amount, BigDecimal deposit) {
        this.id = id;
        this.type = type;

        this.amount = amount;
        this.deposit = deposit;
    }
}
