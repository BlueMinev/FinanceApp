package transactionHandling;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import controllers.GlobalVariables;
import database.DBController;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class transactionTracker {

        /**
         * ArrayList for storing transactionRecord objects
         */
        public ArrayList<transactionRecord> transactions;

        private DBController dbController;

        private int accountID;

        /**
         * Constructor for the transactionTracker class
         * instantiates transactions as an arraylist
         * and calls the readPaymentTable method to populate the transaction list
         * Attempts to set the global var aaccountID through SQL queries
         */
        public transactionTracker() {
                transactions = new ArrayList<>();
                dbController = new DBController();

                try{
                        if (GlobalVariables.email != null) {
                                List<Map<String, Object>> response = dbController.executeSQL("SELECT id FROM tUser WHERE uName = '" + GlobalVariables.email + "';");
                                System.out.println(response.size());
                                String userID = Integer.toString((int)response.get(0).get("id"));
                                System.out.println(userID);
                                
                                response = dbController.executeSQL("SELECT * FROM tAccount WHERE ownerid = " + userID + ";");
                                System.out.println("tAccount: " + response);

                                int userIDint = Integer.parseInt(userID);

                            if (response.size() > 0){
                                    this.accountID = (Integer) response.get(0).get("ownerid");                                }
                                else {
                                    System.out.println("no account found, make new account");
                                    dbController.addAccount("default", userIDint, 0, "default");
                                    this.accountID = userIDint;
                                }


                                GlobalVariables.accountID = Integer.toString(accountID);
                        } else{
                                System.out.println("email is null");
                                accountID = Integer.parseInt(GlobalVariables.accountID);
                        }
                        

                } 
                catch (SQLException e){
                        e.printStackTrace();
                }

                this.readPaymentTable(accountID);
        }

        // kinda just for testing atm lol
        //@SuppressWarnings("unchecked")
        @SuppressWarnings("unchecked")
        public static void main(String[] args) {
                GlobalVariables.accountID = "0";
                transactionTracker tracker = new transactionTracker();
                
                
        }

        
       /**
        * Reads the payment table from the database and adds the transactions to the transaction list.
        * 
        * @throws SQLException if there is an error executing the SQL query
        */
       @SuppressWarnings("unchecked")
        private void readPaymentTable(int accountID){
                try{
                        // Read the payment table from the database
                        List<Map<String, Object>> trnsctns = dbController.executeSQL("SELECT * FROM tPayment WHERE accountNumber = '" + accountID +"';");
                        System.out.println("trnsctns: " + trnsctns);

                        // Loop through each transaction in the table
                        for(Map<String, Object> trnsctn : trnsctns){
                                // Add the transaction to the transaction list
                                this.addTransactionToList((double) trnsctn.get("amount"), // The amount of the transaction
                                        transactionTypes.valueOf((String)trnsctn.get("transaction_type")), // The type of the transaction
                                        billingTypes.valueOf((String)trnsctn.get("billing_type")), // The billing type of the transaction
                                        LocalDate.ofInstant(Instant.ofEpochMilli((long) trnsctn.get("date")), ZoneId.of("UTC")), // The date of the transaction
                                         // The ID of the transaction
                                        (String) trnsctn.get("purchase"), // The description of the transaction
                                        (String) trnsctn.get("place"), // The place where the transaction was made
                                        (String) Integer.toString((int)trnsctn.get("paymentID"))
                                        );
                        }


                        //transactions.addAll(this.filterTransactions(transactionFilters.hasTransactionID(Integer.toString(accountID))));

                }catch (SQLException e){
                        // Print the stack trace if there is an error executing the SQL query
                        e.printStackTrace();
                }


       } // End of readPaymentTable method

        /**
         * Deletes a transaction from the tPayment table in the database.
         *
         * @param  transactionID  the ID of the transaction to be deleted
         * @return                true if the transaction was successfully deleted, false otherwise
         */
       public boolean deleteTransaction(String transactionID){
                try{
                        dbController.executeSQL("DELETE FROM tPayment WHERE paymentID = " + transactionID + ";");

                        if (dbController.executeSQL("SELECT 1 FROM tPayment WHERE paymentID = " + transactionID + ";").size() == 0 ){
                                removeTransaction(transactionID);
                                return true;
                        }
                        else{
                                return false;
                        }
                } catch(SQLException e){
                        e.printStackTrace();
                        return false;
                }

       }

       public void editTransaction(String transactionID, double amount, transactionTypes transactionType, billingTypes billingType,
                                   LocalDate date, String description, String place){
                try{
                        StringBuilder queryBuilder = new StringBuilder();
                        queryBuilder.append("UPDATE tPayment SET ");
                        queryBuilder.append("amount = '").append(amount).append("', ");
                        queryBuilder.append("date = '").append(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()).append("', ");
                        queryBuilder.append("place = '").append(place).append("', ");
                        queryBuilder.append("purchase = '").append(description).append("', ");
                        queryBuilder.append("transaction_type = '").append(transactionType).append("', ");
                        queryBuilder.append("billing_type = '").append(billingType).append("' ");
                        queryBuilder.append("WHERE paymentID = ").append(transactionID).append(";");

                        dbController.executeSQL(queryBuilder.toString());


                } catch(SQLException e){
                        e.printStackTrace();
                }
       }

       /**
        * Adds a transaction to the transactions List and to the database.
        * 
        * @param amount The amount of the transaction.
        * @param transactionType The type of the transaction.
        * @param billingType The billing type of the transaction.
        * @param date The date of the transaction.
        * @param description The description of the transaction.
        * @param place The place where the transaction was made.
        */
       public void addTransaction(double amount, transactionTypes transactionType, billingTypes billingType,
                        LocalDate date, String description, String place){
               // Add the transaction to the transactionList
               this.addTransactionToList(amount, transactionType, billingType, date, description, place, null);
               
               // Add the transaction to the database
               this.addTransactionToDB(this.accountID, amount, date.toString(), place, description, transactionType.toString(), billingType.toString());
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
       public void addTransactionToDB(int accountNum, double amount, String date, String place, String purchase, String transactionType, String billingType){
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
                        LocalDate date, String description, String place, String transactionID) {
                transactions.add(new transactionRecord(amount, transactionType, billingType, date, transactionID, description, place));
        }

        /**
         * Adds a transaction to the List using the transactionRecord itself
         * instead of the parameters needed to create one
         * 
         * @param transactionRecord
         */
        private void addTransactionByRecord(transactionRecord transactionRecord) {
                transactions.add(transactionRecord);
        }

        /**
         * Method for removing transaction from the list using the unique ID assigned to
         * each transaction
         *
         * @param transactionID
         */
        private void removeTransaction(String transactionID) {
            Iterator<transactionRecord> iterator = transactions.iterator();
            while (iterator.hasNext()) {
                transactionRecord record = iterator.next();
                if (record.transactionID().equals(transactionID)) {
                    iterator.remove();
                    break;
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
