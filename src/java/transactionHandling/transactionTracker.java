package transactionHandling;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDate;

public class transactionTracker {

        /**
         * ArrayList for storing transactionRecord objects
         */
        private ArrayList<transactionRecord> transactions;

        /**
         * Constructor for the transactionTracker class
         * instantiates transactions as an arraylist
         */
        public transactionTracker() {
                transactions = new ArrayList<>();
        }

        // kinda just for testing atm lol
        public static void main(String[] args) {
                transactionTracker expTracker = new transactionTracker();

                expTracker.addTransaction(1000,
                                transactionTypes.BILL,
                                billingTypes.MONTHLY,
                                "2014-04-27");

                expTracker.addTransaction(500.00,
                                transactionTypes.BILL,
                                billingTypes.MONTHLY,
                                "2014-05-15");

                expTracker.addTransaction(2500,
                                transactionTypes.INCOME,
                                billingTypes.MONTHLY,
                                "2014-05-05");

                System.out.println(expTracker.getBalance());
                System.out.println(expTracker.getTotalIn());
                System.out.println(expTracker.getTotalOut());

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
                        String date) {
                transactions.add(new transactionRecord(amount, transactionType, billingType, LocalDate.parse(date)));
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
         * 
         * @param predicates
         * @return List of filtered transactions
         */
        public List<transactionRecord> filterTransactions(Predicate<transactionRecord>... predicates) {
                Predicate<transactionRecord> combinedPredicate = Stream.of(predicates) // combining the predicate using stream API
                                .reduce((pred1, pred2) -> pred1.and(pred2)) // and the .reduce() method to combine them
                                .orElse(student -> true); // if no predicates are probided it defaults to true

                return transactions.stream() // start the stream on the transactions arraylist
                                .filter(combinedPredicate) // filter by the combined predicate made using .reduce
                                .collect(Collectors.toList()); // collect the filtered transactions to a list and return it
        }
}
