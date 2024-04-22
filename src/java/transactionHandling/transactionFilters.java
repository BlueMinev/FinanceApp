package transactionHandling;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

public class transactionFilters {
    // predicates for amount
    public static Predicate<transactionRecord> hasAmountEqualTo(double amount) {
        return transaction -> transaction.amount() == amount;
    }

    public static Predicate<transactionRecord> hasAmountGreaterThan(double amount) {
        return transaction -> transaction.amount() > amount;
    }

    public static Predicate<transactionRecord> hasAmountLessThan(double amount) {
        return transaction -> transaction.amount() < amount;
    }

    public static Predicate<transactionRecord> hasAmountBetween(double minAmount, double maxAmount) {
        return transaction -> transaction.amount() >= minAmount && transaction.amount() <= maxAmount;
    }

    // predicates for transactionType
    public static Predicate<transactionRecord> hasTransactionType(transactionTypes transactionType) {
        return transaction -> transaction.transactionType() == transactionType;
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
