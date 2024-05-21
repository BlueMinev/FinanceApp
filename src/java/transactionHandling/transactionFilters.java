package transactionHandling;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

public class transactionFilters {

    // predicates for amount
    /**
     * 
     * @param amount
     * @return  True if the absolute value of the transaction is == amount, else false
     */
    public static Predicate<transactionRecord> hasAmountEqualTo(double amount) {
        return transaction -> Math.abs(transaction.amount()) == amount;
    }

    /**
     * 
     * @param amount
     * @return True if the absolute value is > amount, else false
     */
    public static Predicate<transactionRecord> hasAmountGreaterThan(double amount) {
        return transaction -> Math.abs(transaction.amount()) > amount;
    }

    /**
     * 
     * @param amount
     * @return True if the absolute value is < amount, else false
     */
    public static Predicate<transactionRecord> hasAmountLessThan(double amount) {
        return transaction -> Math.abs(transaction.amount()) < amount;
    }

    /**
     * 
     * @param amount
     * @return True if absolute value >= amount, else false
     */
    public static Predicate<transactionRecord> hasAmountGreaterThanEqualTo(double amount) {
        return transaction -> transaction.amount() >= amount;
    }
    
    /**
     * 
     * @param amount
     * @return True if absolute value <= amount, else false
     */
    public static Predicate<transactionRecord> hasAmountLessThanEqualTo(double amount) {
        return transaction -> transaction.amount() <= amount;
    }

    /**
     * 
     * @param minAmount
     * @param maxAmount
     * @return True if the absolute value is >= minAmount and <= maxAmount, else false
     */
    public static Predicate<transactionRecord> hasAmountBetween(double minAmount, double maxAmount) {
        return transaction -> Math.abs(transaction.amount()) >= minAmount && Math.abs(transaction.amount()) <= maxAmount;
    }

    // predicates for transactionType
    public static Predicate<transactionRecord> hasTransactionType(transactionTypes transactionType) {
        return transaction -> transaction.transactionType() == transactionType;
    }

    public static Predicate<transactionRecord> doesNotHaveTransactionType(transactionTypes transactionType) {
        return transaction -> transaction.transactionType() != transactionType;
    }

    // predicate for billingType
    public static Predicate<transactionRecord> hasBillingType(billingTypes billingType) {
        return transaction -> transaction.billingType() == billingType;
    }

    // predicates for dates
    public static Predicate<transactionRecord> hasDateBefore(LocalDate date) {
        return transaction -> transaction.date().isBefore(date);
    }

    public static Predicate<transactionRecord> hasDateAfter(LocalDate date) {
        return transaction -> transaction.date().isAfter(date);
    }

    public static Predicate<transactionRecord> hasDateEqualTo(LocalDate date) {
        return transaction -> transaction.date().isEqual(date);
    }

    // predicates for transactionID
    public static Predicate<transactionRecord> hasTransactionID(String transactionID) {
        return transaction -> transaction.transactionID().equals(transactionID);
    }
}
