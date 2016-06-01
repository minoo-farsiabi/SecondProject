import java.io.Serializable;

/**
 * Created by m.farsiabi on 5/30/2016.
 */
public class TransactionResponse implements Serializable {
    private String id;
    private String resultCode;
    private String message;

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getResultCode() {
        return resultCode;
    }

    public TransactionResponse(String id, String resultCode, String message) {
        this.id = id;
        this.resultCode = resultCode;
        this.message = message;

    }
}
