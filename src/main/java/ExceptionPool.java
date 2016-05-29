/**
 * Created by m.farsiabi on 5/25/2016.
 */
public class ExceptionPool extends Exception {
    public ExceptionPool(String message) {
        super(message);
        System.err.println("Error:" + message);
    }
}
