import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.farsiabi on 5/23/2016.
 */
public class ClientParser {

    public ClientModel getClientModel() {
        return clientModel;
    }

    private static ClientModel clientModel;

    public ClientParser() throws ExceptionPool {
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
                if (terminalNode.getNodeType() == terminalNode.ELEMENT_NODE) {
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
                        if (transactionNode.getNodeType() == transactionNode.ELEMENT_NODE) {
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
                            if (id != null && type != null && amount != null && deposit != null) {
                                Transaction transaction = new Transaction(id,type,amount,deposit);
                                allTransactions.add(transaction);
                            }
                        }
                    }
                }
            }

            createClientIfPossible(terminalId,terminalType,serverIP,serverPort,outLogPath,allTransactions);

        } catch (ParserConfigurationException e) {
            throw new ExceptionPool("Parser Configuration Problem!");
        } catch (IOException e) {
            throw new ExceptionPool("IOException");
        } catch (SAXException e) {
            throw new ExceptionPool("SAXException");
        }
    }

    private void createClientIfPossible(String terminalId, String terminalType, String serverIP, String serverPort, String outLogPath, List<Transaction> allTransactions) throws ExceptionPool {
        if (terminalId == null) {
            throw new ExceptionPool("Empty Terminal Id!");
        } else if (terminalType == null) {
            throw new ExceptionPool("Empty Terminal Type!");
        } else if (serverIP == null) {
            throw new ExceptionPool("Empty Server IP!");
        } else if (serverPort == null) {
            throw new ExceptionPool("Empty Server Port!");
        } else if (outLogPath == null) {
            throw new ExceptionPool("Empty OutLog Path!");
        } else {
            clientModel = new ClientModel(terminalId, terminalType, serverIP, serverPort, outLogPath, allTransactions);
        }
    }
}