package transactionHandling;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Record for each transaction
 */
public record transactionRecord(
        double amount,
        transactionTypes transactionType,
        billingTypes billingType,
        LocalDate date, // use isBefore() isAfter() isEqual() to compare dates
        String transactionID) {

    /**
     * Custom constructor for the record which assigns the correct +ve or -ve value
     * to amount
     * ASwell as assigns a random string to each record to act as the transaction ID
     * 
     * @param amount
     * @param transactionType
     * @param billingType
     * @param date
     */
    public transactionRecord(double amount, transactionTypes transactionType, billingTypes billingType, LocalDate date)
    {
        this(returnAmount(amount, transactionType), transactionType, billingType, date, UUID.randomUUID().toString());
    }

    /**
     * Method which returns either a +ve or -ve value of amount depending on whether
     * it is an expense or an income
     * 
     * @param amount
     * @param transactionType
     * @return
     */
    private static double returnAmount(double amount, transactionTypes transactionType) {
        // if the transaction is an expense the amount is made negative regardless
        if (transactionType != transactionTypes.INCOME) {
            if (amount > 0) {
                return amount * -1;
            }
        } else // and if it's an expense the amount is made negative regardless
        {
            return Math.abs(amount);
        }

        return amount;
    }
}