import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDate;


public class transactionTracker {
    
        /**
         * Enums for types of transaction
         * and types of billings
         */
        public enum transactionTypes {HOME,BILL,GROCERIES,EATOUT,TRANSPORT,ENTERTAINMENT,HEALTH,SHOPPING,GENERAL,INCOME} // all but INCOME imply expense 
        public enum billingTypes{WEEKLY,BIWEEKLY,MONTHLY, QUARTERLY,ANNUAL,NA}

        /**
         * Record for each transaction
         */
        record transactionRecord (
                double amount,
                transactionTypes transactionType,
                billingTypes billingType,
                LocalDate date,
                String transactionID
                )
        {
                
                /**
                 * Custom constructor for the record which assigns the correct +ve or -ve value to amount
                 * ASwell as assigns a random string to each record to act as the transaction ID
                 * @param amount
                 * @param transactionType
                 * @param billingType
                 * @param date
                 */
                public transactionRecord(double amount, transactionTypes transactionType, billingTypes billingType, LocalDate date) {
                        this(returnAmount(amount, transactionType), transactionType, billingType, date, UUID.randomUUID().toString());
                }

                /**
                 * Method which returns either a +ve or -ve value of amount depending on whether it is an expense or an income
                 * @param amount
                 * @param transactionType
                 * @return
                 */
                private static double returnAmount(double amount, transactionTypes transactionType){
                        // if the transaction is an expense the amount is made negative regardless
                        if (transactionType != transactionTypes.INCOME){
                                if (amount > 0){
                                        return amount * -1;
                                }
                        }else // and if it's an expense the amount is made negative regardless
                        {
                                return Math.abs(amount);
                        }

                        return amount;
                }
        }

        /**
         * ArrayList for storing transactionRecord objects
         */
        private ArrayList<transactionRecord> transactions;

        /**
         * Constructor for the transactionTracker class
         * instantiates transactions as an arraylist
         */
        public transactionTracker(){
                transactions = new ArrayList<>();
        }


        // kinda just for testing atm lol
        public static void main(String[] args){
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
         * @param amount
         * @param transactionType
         * @param billingType
         * @param date
         */
        public void addTransaction(double amount, transactionTypes transactionType, billingTypes billingType, String date){
                transactions.add(new transactionRecord(amount, transactionType, billingType, LocalDate.parse(date)));
        }

        /**
         * Adds a transaction to the List using the transactionRecord itself
         * instead of the parameters needed to create one
         * @param transactionRecord
         */
        public void addTransaction(transactionRecord transactionRecord){
                transactions.add(transactionRecord);
        }

        /**
         * Method for removing transaction from the list using the unique ID assigned to each transaction
         * @param transactionID
         */
        public void removeTransaction(String transactionID){
                for (transactionRecord transactionRecord : transactions) {
                        if (transactionRecord.transactionID == transactionID){
                                transactions.remove(transactionRecord);
                        }
                }
        }

        /**
         * Method for removing a transaction by the transactionRecord itself
         * @param transactionRecord
         */
        public void removeTransaction(transactionRecord transactionRecord){
                transactions.remove(transactionRecord);
        }

        /**
         * Sums up total incomes in the transactionsList
         * @return 
         */
        public double getTotalIn(){
                double totalIn = 0;
                for (transactionRecord transactionRecord : transactions) {
                        // if expense is +ve then it's an income
                        if (transactionRecord.amount() > 0){
                                totalIn = totalIn + transactionRecord.amount();
                        }
                }
                return totalIn;
        }

        /**
         * Sums up total expenses in the transactionsList
         * @return
         */
        public double getTotalOut(){
                double totalOut = 0;
                for (transactionRecord transactionRecord : transactions) {
                        // if expense is -ve it's an expense
                        if (transactionRecord.amount() < 0){
                                totalOut = totalOut + transactionRecord.amount();
                        }
                }
                return totalOut;
        }

        /**
         * Gets balance by summing totalIn and totalOut
         * @return
         */
        public double getBalance(){
                return getTotalIn() + getTotalOut();
        }

    // filter with streams
        //TODO work out how to implement dates correctly so i can filter by dates.
        //TODO create filters that take an input e.g. custom selection of groups
}

