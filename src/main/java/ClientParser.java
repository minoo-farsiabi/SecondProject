import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.farsiabi on 5/23/2016.
 */
public class ClientParser {

    public static ClientModel clientModel;

    public void parseTerminalFile() throws BusinessLogicException {
        try {
            File fXmlFile = new File("terminal.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            String terminalId = null;
            String terminalType = null;
            String serverIP = null;
            String serverPort = null;
            String outLogPath = null;
            List<Transaction> allTransactions = new ArrayList<Transaction>();

            //Checking terminal parameters
            NodeList terminalList = doc.getElementsByTagName("terminal");
            for (int temp = 0; temp < terminalList.getLength(); temp++) {

                Node terminalNode = terminalList.item(temp);
                if (terminalNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) terminalNode;
                    if (element.hasAttribute("id")) {
                        terminalId = element.getAttribute("id");
                    }
                    if (element.hasAttribute("type")) {
                        terminalType = element.getAttribute("type");
                    }
                }
            }

            System.out.println("id " + terminalId + "**type: " + terminalType);

            //Checking server parameters
            NodeList serverList = doc.getElementsByTagName("server");
            for (int temp = 0; temp < serverList.getLength(); temp++) {

                Node serverNode = serverList.item(temp);
                if (serverNode.getNodeType() == serverNode.ELEMENT_NODE) {
                    Element element = (Element) serverNode;
                    if (element.hasAttribute("ip")) {
                        serverIP = element.getAttribute("ip");
                    }
                    if (element.hasAttribute("port")) {
                        serverPort = element.getAttribute("port");
                    }
                }
            }
            System.out.println("ip " + serverIP + "**port: " + serverPort);

            //Checking outlog parameters
            NodeList outLogList = doc.getElementsByTagName("outLog");
            for (int temp = 0; temp < outLogList.getLength(); temp++) {

                Node outLogNode = outLogList.item(temp);
                if (outLogNode.getNodeType() == outLogNode.ELEMENT_NODE) {
                    Element element = (Element) outLogNode;
                    if (element.hasAttribute("path")) {
                        outLogPath = element.getAttribute("path");
                    }
                }
            }
            System.out.println("outlog path " + outLogPath);

            //Checking transactions parameters
            NodeList transactionsList = doc.getElementsByTagName("transactions");
            for (int temp = 0; temp < transactionsList.getLength(); temp++) {

                Node transactionsNode = transactionsList.item(temp);
                if (transactionsNode.getNodeType() == transactionsNode.ELEMENT_NODE) {
                    Element element = (Element) transactionsNode;
                    for (int j = 0; j < element.getElementsByTagName("transaction").getLength(); j++) {
                        Node transactionNode = element.getElementsByTagName("transaction").item(j);
                        if (transactionNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element transactionElement = (Element) transactionNode;
                            String id = null;
                            String type = null;
                            String amount = null;
                            String deposit = null;
                            if (transactionElement.hasAttribute("id")) {
                                id = transactionElement.getAttribute("id");
                            }
                            if (transactionElement.hasAttribute("type")) {
                                type = transactionElement.getAttribute("type");
                            }
                            if (transactionElement.hasAttribute("amount")) {
                                amount = transactionElement.getAttribute("amount");
                            }
                            if (transactionElement.hasAttribute("deposit")) {
                                deposit = transactionElement.getAttribute("deposit");
                            }

                            validateTransaction(id, type, amount, deposit);
                            if (id != null && type != null && amount != null && deposit != null) {
                                Transaction transaction = new Transaction(id, type, new BigDecimal(amount), new BigDecimal(deposit));
                                allTransactions.add(transaction);
                            }
                        }
                    }
                }
            }

            createClientIfPossible(terminalId, terminalType, serverIP, serverPort, outLogPath, allTransactions);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateTransaction(String id, String type, String amount, String deposit) throws BusinessLogicException {
        if ((id == null) || (Long.parseLong(id) < 1)) {
            throw new InvalidTransactionIdException();
        } else if (type == null || (!type.equals("deposit") && !type.equals("withdraw"))) {
            throw new InvalidTransactionTypeException();
        } else if (amount == null || (new BigDecimal(amount).longValue() <= 0)) {
            throw new InvalidTransactionAmountException();
        } else if (deposit == null || (new BigDecimal(deposit).longValue() < 0)) {
            throw new InvalidTransactionDepositException();
        }
    }

    private void createClientIfPossible(String terminalId, String terminalType, String serverIP, String serverPort, String outLogPath, List<Transaction> allTransactions) throws BusinessLogicException {
        if ((terminalId == null) || (Long.parseLong(terminalId) < 1)) {
            throw new InvalidorEmptyTerminalIdException();
        } else if (terminalType == null) {
            throw new InvalidorEmptyTerminalTypeException();
        } else if (serverIP == null) {
            throw new InvalidorEmptyServerIPException();
        } else if (serverPort == null || (Long.parseLong(serverPort) < 1)) {
            throw new InvalidorEmptyServerPortException();
        } else if (outLogPath == null) {
            throw new EmptyOutLogPathException();
        } else {
            clientModel = new ClientModel(terminalId, terminalType, serverIP, serverPort, outLogPath, allTransactions);
        }
    }
}