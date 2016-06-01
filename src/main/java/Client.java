import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.farsiabi on 5/28/2016.
 */
public class Client {

    public static void main(String[] args) throws BusinessLogicException, IOException {
        Socket clientSocket = null;
        try {
            ClientParser clientParser = new ClientParser();
            clientParser.parseTerminalFile();
            clientParser.clientModel.saveOutLog("Reading terminal.xml completed.\n");

            String serverIP = clientParser.clientModel.getServerIP();
            String serverPort = clientParser.clientModel.getServerPort();

            List<TransactionResponse> allTransactionResponses = new ArrayList<TransactionResponse>();

            System.out.println("Attempting to connect to host " + serverIP + " on port " + serverPort);
            clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));
            System.out.println("Connected Successfully");
            clientParser.clientModel.saveOutLog("Connection successful\n");

            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            InputStream inputStream = clientSocket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            for (Transaction transaction : clientParser.clientModel.getTransactions()) {
                objectOutputStream.writeObject(transaction);
                clientParser.clientModel.saveOutLog("transaction #" + transaction.getId() + " of type " + transaction.getType() + " performed: Deposit No. " + transaction.getDeposit() + " Amount: " + transaction.getAmount() + "\n");
                TransactionResponse transactionResponse = (TransactionResponse) objectInputStream.readObject();
                if (transactionResponse != null) {
                    System.out.println("response: " + transactionResponse.getMessage());
                    clientParser.clientModel.saveOutLog("Response of transaction #" + transactionResponse.getId() + ": result code: " + transactionResponse.getResultCode() + " message: " + transactionResponse.getMessage() + "\n");
                    allTransactionResponses.add(transactionResponse);
                }
                //update logfile
            }

            keepResponseRecord(allTransactionResponses);

            objectOutputStream.writeObject(new String("exit"));
            clientParser.clientModel.saveOutLog("Commands finished.\n");

            objectOutputStream.close();
            outputStream.close();
            clientSocket.close();
            clientParser.clientModel.saveOutLog("client is shutting down.\n");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            clientSocket.close();
        }
    }

    private static void keepResponseRecord(List<TransactionResponse> allTransactionResponses) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("transactionResults");
            doc.appendChild(rootElement);

            for (TransactionResponse transactionResponse : allTransactionResponses) {
                Element transactionResultElement = doc.createElement("transactionResult");
                rootElement.appendChild(transactionResultElement);
                transactionResultElement.setAttribute("id", transactionResponse.getId());

                Element resultCodeElement = doc.createElement("resultCode");
                resultCodeElement.appendChild(doc.createTextNode(transactionResponse.getResultCode()));
                transactionResultElement.appendChild(resultCodeElement);

                Element messageElement = doc.createElement("message");
                messageElement.appendChild(doc.createTextNode(transactionResponse.getMessage()));
                transactionResultElement.appendChild(messageElement);
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("response.xml"));
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
