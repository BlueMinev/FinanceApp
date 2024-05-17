package transactionHandling;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sqlite.util.LibraryLoaderUtil;

import controllers.GlobalVariables;
import controllers.dashboardController;
import database.DBController;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class transactionTracker {

        /**
         * ArrayList for storing transactionRecord objects
         */
        private ArrayList<transactionRecord> transactions;

        private DBController dbController;

        private int accountID;

        /**
         * Constructor for the transactionTracker class
         * instantiates transactions as an arraylist
         */
        public transactionTracker() {
                transactions = new ArrayList<>();
                dbController = new DBController();

                this.readPaymentTable();

                try{
                        List<Map<String, Object>> response = dbController.executeSQL("SELECT * FROM tUser WHERE email = " + GlobalVariables.email + ";");

                        String userID = (String) response.get(0).get("userID");
                        
                        response = dbController.executeSQL("SELECT * FROM tAccount WHERE userID = " + userID + ";");
                        this.accountID = Integer.parseInt((String)response.get(0).get("accountID"));

                        GlobalVariables.accountID = Integer.toString(accountID);

                } 
                catch (SQLException e){
                        e.printStackTrace();
                }
        }

        // kinda just for testing atm lol
        //@SuppressWarnings("unchecked")
        @SuppressWarnings("unchecked")
        public static void main(String[] args) {

                transactionTracker expTracker = new transactionTracker();

                System.out.println(expTracker.getBalance());

                
                expTracker.addTransactionToDB(8, 400, "2024-05-16", "McDonalds", "Too many bigmacs", "EATOUT", "NA");

                
        }
        
       /**
        * Reads the payment table from the database and adds the transactions to the transaction list.
        * 
        * @throws SQLException if there is an error executing the SQL query
        */
       @SuppressWarnings("unchecked")
        private void readPaymentTable(){
                try{
                        // Read the payment table from the database
                        List<Map<String, Object>> trnsctns = dbController.readTable("tPayment");

                        // Loop through each transaction in the table
                        for(Map<String, Object> trnsctn : trnsctns){
                                // Add the transaction to the transaction list
                                this.addTransaction((double) trnsctn.get("amount"), // The amount of the transaction
                                        transactionTypes.valueOf((String)trnsctn.get("transaction_type")), // The type of the transaction
                                        billingTypes.valueOf((String)trnsctn.get("billing_type")), // The billing type of the transaction
                                        LocalDate.ofInstant(Instant.ofEpochMilli((long) trnsctn.get("date")), ZoneId.of("UTC")), // The date of the transaction
                                        (String) trnsctn.get("paymentID").toString(), // The ID of the transaction
                                        (String) trnsctn.get("purchase"), // The description of the transaction
                                        (String) trnsctn.get("place") // The place where the transaction was made
                                        );
                        }
                       

                        transactions.addAll(this.filterTransactions(transactionFilters.hasTransactionID(Integer.toString(accountID))));

                }catch (SQLException e){
                        // Print the stack trace if there is an error executing the SQL query
                        e.printStackTrace();
                }

                
       } // End of readPaymentTable method

       /**
        * Adds a transaction to the transactions List and to the database.
        * 
        * @param amount The amount of the transaction.
        * @param transactionType The type of the transaction.
        * @param billingType The billing type of the transaction.
        * @param date The date of the transaction.
        * @param transactionID The ID of the transaction.
        * @param description The description of the transaction.
        * @param place The place where the transaction was made.
        */
       public void addTransaction(double amount, transactionTypes transactionType, billingTypes billingType,
                        LocalDate date, String transactionID, String description, String place){
               // Add the transaction to the transactionList
               this.addTransactionToList(amount, transactionType, billingType, date, transactionID, description, place);
               
               // Add the transaction to the database
               this.addTransactionToDB(accountID, amount, date.toString(), place, description, transactionID, place);
       }


       /**
        * Adds a transaction to the database.
        * 
        * @param accountNum The account number of the user.
        * @param amount The amount of the transaction.
        * @param date The date of the transaction in the format "yyyy-MM-dd".
        * @param place The place where the transaction was made.
        * @param purchase The purchase description.
        * @param transactionType The type of the transaction.
        * @param billingType The billing type of the transaction.
        */
       private void addTransactionToDB(int accountNum, double amount, String date, String place, String purchase, String transactionType, String billingType){
               dbController.addPayment(accountNum, amount, date, place, purchase, transactionType, billingType);
       }

        /**
         * Adds a transaction to the transactions List
         * Takes all parameters needed to create a transactionRecord
         * 
         * @param amount
         * @param transactionType
         * @param billingType
         * @param date
         */
        private void addTransactionToList(double amount, transactionTypes transactionType, billingTypes billingType,
                        LocalDate date, String transactionID, String description, String place) {
                transactions.add(new transactionRecord(amount, transactionType, billingType, date, transactionID, description, place));
        }

        /**
         * Adds a transaction to the List using the transactionRecord itself
         * instead of the parameters needed to create one
         * 
         * @param transactionRecord
         */
        private void addTransaction(transactionRecord transactionRecord) {
                transactions.add(transactionRecord);
        }

        /**
         * Method for removing transaction from the list using the unique ID assigned to
         * each transaction
         * 
         * @param transactionID
         */
        private void removeTransaction(String transactionID) {
                for (transactionRecord transactionRecord : transactions) {
                        if (transactionRecord.transactionID() == transactionID) {
                                transactions.remove(transactionRecord);
                        }
                }
        }

        /**
         * Method for removing a transaction by the transactionRecord itself
         * 
         * @param transactionRecord
         */
        private void removeTransaction(transactionRecord transactionRecord) {
                transactions.remove(transactionRecord);
        }

        /**
         * Sums up total incomes in the transactionsList
         * 
         * @return
         */
        public double getTotalIn() {
                double totalIn = 0;
                for (transactionRecord transactionRecord : transactions) {
                        // if expense is +ve then it's an income
                        if (transactionRecord.amount() > 0) {
                                totalIn = totalIn + transactionRecord.amount();
                        }
                }
                return totalIn;
        }

        /**
         * Sums up total expenses in the transactionsList
         * 
         * @return
         */
        public double getTotalOut() {
                double totalOut = 0;
                for (transactionRecord transactionRecord : transactions) {
                        // if expense is -ve it's an expense
                        if (transactionRecord.amount() < 0) {
                                totalOut = totalOut + transactionRecord.amount();
                        }
                }
                return totalOut;
        }

        /**
         * Gets balance by summing totalIn and totalOut
         * 
         * @return double balance of the account
         */
        public double getBalance() {
                return getTotalIn() + getTotalOut();
        }

        
        /**
         * To use this you call filterTransactions(), and for parameters use the transactionFilters class to get predicates (filters)
         * You can put as many predicates as you want as parameters, it will combine them.
         * For example if you want to get all transactions on GROCERIES over £100:
         * filterTransactions(transactionFilters.hasTransactionType(transactionTypes.GROCERIES), transactionFilters.hasAmountGreaterThan(100));
         *  
         * @param predicates
         * @return List of filtered transactions
         */
        @SuppressWarnings("unchecked")
        public List<transactionRecord> filterTransactions(Predicate<transactionRecord>... predicates) {
                Predicate<transactionRecord> combinedPredicate = Stream.of(predicates) // combining the predicate using stream API
                                .reduce((pred1, pred2) -> pred1.and(pred2)) // and the .reduce() method to combine them
                                .orElse(transaction -> true); // if no predicates are probided it defaults to true

                return transactions.stream() // start the stream on the transactions arraylist
                                .filter(combinedPredicate) // filter by the combined predicate made using .reduce
                                .collect(Collectors.toList()); // collect the filtered transactions to a list and return it
        }

        /**
         * slightly different version which can take any list of transactionRecords
         * doesn't use the instance List transaction therefore is static
         * @param listToFilter
         * @param predicates
         * @return filtered list
         */
        @SuppressWarnings("unchecked")
        public static List<transactionRecord> filterTransactions(List<transactionRecord> listToFilter, Predicate<transactionRecord>... predicates) {
                Predicate<transactionRecord> combinedPredicate = Stream.of(predicates) // combining the predicate using stream API
                                .reduce((pred1, pred2) -> pred1.and(pred2)) // and the .reduce() method to combine them
                                .orElse(transaction -> true); // if no predicates are probided it defaults to true

                return listToFilter.stream() // start the stream on the transactions arraylist
                                .filter(combinedPredicate) // filter by the combined predicate made using .reduce
                                .collect(Collectors.toList()); // collect the filtered transactions to a list and return it
        }

}
