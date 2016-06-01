import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by m.farsiabi on 5/28/2016.
 */
public class ServerParser {
    private FileReader jsonFile;
    public static ServerModel serverModel;

    public ServerParser(FileReader jsonFile) {
        this.jsonFile = jsonFile;
    }

    public void parseJSON() {
        JSONParser jsonParser = new JSONParser();

        int port;
        String customer;
        String id;
        String initialBalance;
        String upperBound;
        String outLog;
        List<Deposit> allDeposits = new ArrayList<Deposit>();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonFile);
            port = ((Long) jsonObject.get("port")).intValue();
            JSONArray jsonArray = (JSONArray) jsonObject.get("deposits");
            for (Object aJsonArray : jsonArray) {
                JSONObject deposit = (JSONObject) aJsonArray;
                customer = (String) deposit.get("customer");
                id = (String) deposit.get("id");
                initialBalance = (String) deposit.get("initialBalance");
                upperBound = (String) deposit.get("upperBound");
                if (customer != null && id != null && initialBalance != null && upperBound != null) {
                    Deposit newDeposit = new Deposit(customer, id, new BigDecimal(initialBalance), new BigDecimal(upperBound));
                    allDeposits.add(newDeposit);
                }
            }
            outLog = (String) jsonObject.get("outLog");
            if (port != 0 && allDeposits.size() != 0 && outLog != null) {
                serverModel = new ServerModel(port, allDeposits, outLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
