import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by m.farsiabi on 5/29/2016.
 */
public class ServerThread extends Thread {
    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            while (true) {
                Object command = objectInputStream.readObject();

                if (command instanceof Transaction) {
                    Transaction transaction = (Transaction) command;
                    System.out.println("I got a transaction!");
                    TransactionResponse transactionResponse = performTransaction(transaction);
                    //TransactionResponse transactionResponse = performTransactionNonSynchronized(transaction);
                    objectOutputStream.writeObject(transactionResponse);
                } else if (command instanceof String) {
                    String stringCommand = (String) command;
                    if (stringCommand.equals("exit")) {
                        objectInputStream.close();
                        inputStream.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }

    private TransactionResponse performTransaction(Transaction transaction) throws BusinessLogicException {
        System.err.println("transaction.getDeposit(): " + transaction.getDeposit());
        Deposit destinationDeposit = ServerParser.serverModel.getDepositById(transaction.getDeposit().toString());
        if (destinationDeposit == null) {
            ServerParser.serverModel.saveOutLog("Deposit with id " + transaction.getDeposit() + " was not found.\r\n");
            throw new DepositNotFoundException();
        }

        TransactionResponse transactionResponse = null;

        synchronized (destinationDeposit) {
            if (transaction.getType().equals("deposit")) {
                transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "0", "عملیات با موفقیت انجام شد");
                destinationDeposit.setInitialBalance(destinationDeposit.getInitialBalance().add(transaction.getAmount()));
                ServerParser.serverModel.saveOutLog(transaction.getAmount().toString() + " Rials deposited to deposit " + transaction.getDeposit() + " current balance: " + destinationDeposit.getInitialBalance() + " at " + getTimeandDate() + "\r\n");
            } else if (transaction.getType().equals("withdraw")) {
                if (((destinationDeposit.getInitialBalance().subtract(transaction.getAmount()))).longValue() < 0) {
                    transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "1", "موجودی کافی نیست");
                    ServerParser.serverModel.saveOutLog("Deposit " + transaction.getDeposit() + " didn't have enough balance to withdraw " + transaction.getAmount().toString() + " Rials at " + getTimeandDate() + " (initial balance = " + destinationDeposit.getInitialBalance().toString() + ")\r\n");
                }
                //throw new InadequateBalanceForTransactionException();
                else if (transaction.getAmount().longValue() > destinationDeposit.getUpperBound().longValue()) {
                    transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "2", "سقف مجاز برای برداشت رعایت نشده است");
                    ServerParser.serverModel.saveOutLog("Request amount = " + transaction.getAmount().toString() + " exceeded the limit " + destinationDeposit.getUpperBound().toString() + " of deposit " + transaction.getDeposit() + " at " + getTimeandDate() + "\r\n");
                }
                //throw new UpperBoundReachedForTransactionException();
                else {
                    destinationDeposit.setInitialBalance(destinationDeposit.getInitialBalance().subtract(transaction.getAmount()));
                    transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "0", "عملیات با موفقیت انجام شد");
                    ServerParser.serverModel.saveOutLog(transaction.getAmount().toString() + " Rials withdrawn from deposit " + transaction.getDeposit() + " current balance: " + destinationDeposit.getInitialBalance() + " at " + getTimeandDate() + "\r\n");
                }

            }
        }
        return transactionResponse;
    }

    private TransactionResponse performTransactionNonSynchronized(Transaction transaction) throws BusinessLogicException {
        TransactionResponse transactionResponse = null;
        System.err.println("transaction.getDeposit(): " + transaction.getDeposit());
        Deposit destinationDeposit = ServerParser.serverModel.getDepositById(transaction.getDeposit().toString());
        if (destinationDeposit == null) {
            ServerParser.serverModel.saveOutLog("Deposit with id " + transaction.getDeposit() + " was not found.\r\n");
            throw new DepositNotFoundException();
        }


        if (transaction.getType().equals("deposit")) {
            transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "0", "عملیات با موفقیت انجام شد");
            destinationDeposit.setInitialBalance(destinationDeposit.getInitialBalance().add(transaction.getAmount()));
            ServerParser.serverModel.saveOutLog(transaction.getAmount().toString() + " Rials deposited to deposit " + transaction.getDeposit() + " current balance: " + destinationDeposit.getInitialBalance() + " at " + getTimeandDate() + "\r\n");
        } else if (transaction.getType().equals("withdraw")) {
            if (((destinationDeposit.getInitialBalance().subtract(transaction.getAmount()))).longValue() < 0) {
                transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "1", "موجودی کافی نیست");
                ServerParser.serverModel.saveOutLog("Deposit " + transaction.getDeposit() + " didn't have enough balance to withdraw " + transaction.getAmount().toString() + " Rials at " + getTimeandDate() + " (initial balance = " + destinationDeposit.getInitialBalance().toString() + ")\r\n");
            }
            //throw new InadequateBalanceForTransactionException();
            else if (transaction.getAmount().longValue() > destinationDeposit.getUpperBound().longValue()) {
                transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "2", "سقف مجاز برای برداشت رعایت نشده است");
                ServerParser.serverModel.saveOutLog("Request amount = " + transaction.getAmount().toString() + " exceeded the limit " + destinationDeposit.getUpperBound().toString() + " of deposit " + transaction.getDeposit() + " at " + getTimeandDate() + "\r\n");
            }
            //throw new UpperBoundReachedForTransactionException();
            else {
                destinationDeposit.setInitialBalance(destinationDeposit.getInitialBalance().subtract(transaction.getAmount()));
                transactionResponse = new TransactionResponse(transaction.getDeposit().toString(), "0", "عملیات با موفقیت انجام شد");
                ServerParser.serverModel.saveOutLog(transaction.getAmount().toString() + " Rials withdrawn from deposit " + transaction.getDeposit() + " current balance: " + destinationDeposit.getInitialBalance() + " at " + getTimeandDate() + "\r\n");
            }

        }
        return transactionResponse;
    }

    private String getTimeandDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
