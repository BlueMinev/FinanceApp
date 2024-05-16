package transactionHandling;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sqlite.util.LibraryLoaderUtil;

import controllers.dashboardController;
import database.DBController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class transactionTracker {

        /**
         * ArrayList for storing transactionRecord objects
         */
        private ArrayList<transactionRecord> transactions;

        private DBController dbController;

        /**
         * Constructor for the transactionTracker class
         * instantiates transactions as an arraylist
         */
        public transactionTracker() {
                transactions = new ArrayList<>();
                dbController = new DBController();

                this.readPaymentTable();
        }

        // kinda just for testing atm lol
        //@SuppressWarnings("unchecked")
        @SuppressWarnings("unchecked")
        public static void main(String[] args) {

                transactionTracker expTracker = new transactionTracker();

                System.out.println(expTracker.getBalance());

                
        }
        
       private void readPaymentTable(){
                try{
                        List<Map<String, Object>> trnsctns = dbController.readTable("tPayment");

                        for(Map<String, Object> trnsctn : trnsctns){
                                this.addTransaction((double) trnsctn.get("amount"), 
                                        (transactionTypes) trnsctn.get("transactionType"), // how is date stored as a long in the DB???
                                        (billingTypes) trnsctn.get("billingType"), 
                                        LocalDate.parse(trnsctn.get("date").toString(), DateTimeFormatter.ISO_LOCAL_DATE));
                        }
                       

                }catch (SQLException e)
                {
                        e.printStackTrace();
                }

                
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
        public void addTransaction(double amount, transactionTypes transactionType, billingTypes billingType,
                        LocalDate date) {
                transactions.add(new transactionRecord(amount, transactionType, billingType, date));
        }

        /**
         * Adds a transaction to the List using the transactionRecord itself
         * instead of the parameters needed to create one
         * 
         * @param transactionRecord
         */
        public void addTransaction(transactionRecord transactionRecord) {
                transactions.add(transactionRecord);
        }

        /**
         * Method for removing transaction from the list using the unique ID assigned to
         * each transaction
         * 
         * @param transactionID
         */
        public void removeTransaction(String transactionID) {
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
        public void removeTransaction(transactionRecord transactionRecord) {
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

        //TODO connect to database so it can actually read transactions
}
