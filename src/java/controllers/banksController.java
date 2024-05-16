package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.BankAccount;
import models.Transaction;

public class banksController {
    public CheckBox transactionTypeCheckbox;
    public TextField transactionAmountField;
    public ChoiceBox transactionCategoryChoice;
    public TextField transactionDescriptionField;
    public ChoiceBox paymentFrequencyChoice;
    public TableView<Transaction> transactionTableView;
    public ChoiceBox accountChoiceBox;
    private ObservableList<BankAccount> bankAccounts = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactionRecords = FXCollections.observableArrayList();

    @FXML
    private TableView<BankAccount> bankAccountsTable;
    @FXML
    private TextField accountHolderNameField;
    @FXML
    private TextField accountNumberField;
    @FXML
    private TextField initialBalanceField;
    @FXML
    private ChoiceBox<String> accountTypeChoiceBox;

    public void initialize() {
        bankAccountsTable.setItems(bankAccounts);
        accountChoiceBox.setItems(bankAccounts);
        // Populate the account type choice box
        accountTypeChoiceBox.getItems().addAll("Checking", "Savings");
        // Populate the category type choice box
        transactionCategoryChoice.getItems().addAll("Grocery");
    }

    // Method to handle addition of bank accounts
    @FXML
    public void createBankAccount(ActionEvent actionEvent) {
        String accountHolderName = accountHolderNameField.getText();
        String accountNumber = accountNumberField.getText();
        double initialBalance = Double.parseDouble(initialBalanceField.getText());
        String accountType = accountTypeChoiceBox.getValue();

        // Create a new BankAccount object
        BankAccount newAccount = new BankAccount(accountHolderName, accountNumber, initialBalance, accountType);

        // Add the new account to the observable list
        bankAccounts.add(newAccount);

        // Update the accountChoiceBox with the new account
        accountChoiceBox.getItems().add(newAccount);

        // Refresh the bankAccountsTable
        bankAccountsTable.refresh();

        // Clear the input fields
        accountHolderNameField.clear();
        accountNumberField.clear();
        initialBalanceField.clear();
        accountTypeChoiceBox.setValue(null);
    }

    // Method to handle removal of bank accounts
    @FXML
    public void removeBankAccount(ActionEvent actionEvent) {
        // Get the selected account from the table
        BankAccount selectedAccount = bankAccountsTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            // Remove the selected account from the observable list
            bankAccounts.remove(selectedAccount);

            // Remove the account from the accountChoiceBox
            accountChoiceBox.getItems().remove(selectedAccount);
        }
    }

    // Mehtod to handle adding transactions
    @FXML
    public void addTransaction(ActionEvent event) {
        // Retrieve the user input from the input fields
        double transactionAmount = Double.parseDouble(transactionAmountField.getText());

        // Get the selected values from the ChoiceBoxes and cast them to String
        String transactionCategory = (String) transactionCategoryChoice.getSelectionModel().getSelectedItem();
        if (transactionCategory == null || transactionCategory.isEmpty()) {
            // Display an error message or handle the missing transaction category
            return;
        }

        String transactionDescription = transactionDescriptionField.getText();

        String paymentFrequency = (String) paymentFrequencyChoice.getSelectionModel().getSelectedItem();
        if (paymentFrequency == null || paymentFrequency.isEmpty()) {
            // Display an error message or handle the missing payment frequency
            return;
        }

        boolean isPositiveTransaction = transactionTypeCheckbox.isSelected();

        // Get the selected bank account
        BankAccount selectedAccount = (BankAccount) accountChoiceBox.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            // Display an error message or handle the missing bank account selection
            return;
        }

        // Perform input validation
        if (transactionAmount <= 0) {
            // Display an error message or handle the invalid transaction amount
            return;
        }

        if (transactionCategory == null || transactionCategory.isEmpty()) {
            // Display an error message or handle the missing transaction category
            return;
        }

        if (transactionDescription == null || transactionDescription.isEmpty()) {
            // Display an error message or handle the missing transaction description
            return;
        }

        if (paymentFrequency == null || paymentFrequency.isEmpty()) {
            // Display an error message or handle the missing payment frequency
            return;
        }

        // Adjust the transaction amount based on the positive/negative indication
        if (!isPositiveTransaction) {
            transactionAmount *= -1; // Make the transaction amount negative
        }

        // Create a new Transaction object with the adjusted transaction amount, user input, and selected account
        Transaction newTransaction = new Transaction(transactionAmount, transactionCategory, transactionDescription, paymentFrequency, isPositiveTransaction, selectedAccount);

        // Add the new transaction to the transaction records
        transactionRecords.add(newTransaction);

        // Refresh the TableView to display the updated data
        transactionTableView.refresh();

        // Clear the input fields
        transactionAmountField.clear();
        transactionCategoryChoice.setValue(null);
        transactionDescriptionField.clear();
        paymentFrequencyChoice.setValue(null);
        transactionTypeCheckbox.setSelected(false);

        // Notify the user or update the UI to reflect the new transaction with adjusted amount
        // ...
    }

    @FXML
    private void openDashboard() {
        // Code to handle opening the dashboard goes here
        System.out.println("Opening Dashboard");
    }

    @FXML
    private void openBankAccounts() {
        // Code to handle opening the bank accounts goes here
        System.out.println("Opening Bank Accounts");
    }

    @FXML
    private void openSavings() {
        // Code to handle opening the savings goes here
        System.out.println("Opening Savings");
    }

    @FXML
    private void openInvestments() {
        // Code to handle opening the investments goes here
        System.out.println("Opening Investments");
    }

    @FXML
    private void openExpenseTracker() {
        // Code to handle opening the expense tracker goes here
        System.out.println("Opening Expense Tracker");
    }

    @FXML
    private void openGoals() {
        // Code to handle opening the goals goes here
        System.out.println("Opening Goals");
    }

    @FXML
    private void openBudget() {
        // Code to handle opening the budget goes here
        System.out.println("Opening Budget");
    }

    @FXML
    private void openShopping() {
        // Code to handle opening the shopping goes here
        System.out.println("Opening Shopping");
    }

    @FXML
    private void openSettings() {
        // Code to handle opening the settings goes here
        System.out.println("Opening Settings");
    }
}
