package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.BankAccount;

public class banksController {
    private ObservableList<BankAccount> bankAccounts = FXCollections.observableArrayList();

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
        // Populate the account type choice box
        accountTypeChoiceBox.getItems().addAll("Checking", "Savings");
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
        }
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
