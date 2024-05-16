package models;

public class Transaction {
    private double transactionAmount;
    private String transactionCategory;
    private String transactionDescription;
    private String paymentFrequency;
    private boolean isPositiveTransaction;
    private BankAccount bankAccount;

    public Transaction(double transactionAmount, String transactionCategory, String transactionDescription, String paymentFrequency, boolean isPositiveTransaction, BankAccount bankAccount) {
        this.transactionAmount = transactionAmount;
        this.transactionCategory = transactionCategory;
        this.transactionDescription = transactionDescription;
        this.paymentFrequency = paymentFrequency;
        this.isPositiveTransaction = isPositiveTransaction;
        this.bankAccount = bankAccount;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public boolean isPositiveTransaction() {
        return isPositiveTransaction;
    }

    public void setPositiveTransaction(boolean positiveTransaction) {
        isPositiveTransaction = positiveTransaction;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    @Override
    public String toString() {
        return bankAccount.getAccountHolderName() + " (" + bankAccount.getAccountNumber() + ")";
    }
}