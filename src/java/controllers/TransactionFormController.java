package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transactionHandling.TransactionController;
import transactionHandling.billingTypes;
import transactionHandling.transactionRecord;
import transactionHandling.transactionTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransactionFormController {

    public Button submitButton;
    private Map<String, transactionRecord> transactions = new HashMap<>();
    private TransactionController transactionController;

    public void setTransactionController(TransactionController transactionController) {
        this.transactionController = transactionController;
    }
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<transactionTypes> transactionTypeField;
    @FXML
    private ComboBox<billingTypes> billingTypeField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField placeField;

    @FXML
    public void initialize() {
        transactionTypeField.setItems(FXCollections.observableArrayList(transactionTypes.values()));
        billingTypeField.setItems(FXCollections.observableArrayList(billingTypes.values()));
    }

    @FXML
    private void handleSubmit() {
        double amount = Double.parseDouble(amountField.getText());
        transactionTypes type = transactionTypeField.getValue();
        billingTypes billing = billingTypeField.getValue();
        LocalDate date = dateField.getValue();
        String description = descriptionField.getText();
        String place = placeField.getText();

        // Generate a transaction ID
        String transactionID = UUID.randomUUID().toString();

        // Create the transactionRecord
        transactionRecord newRecord = new transactionRecord(amount, type, billing, date, transactionID, description, place);

        // Add the new transactionRecord to the transactions Map
        transactions.put(transactionID, newRecord);

        // Add the new transaction to the TableView in TransactionController
        transactionController.addTransaction(newRecord);

        // Assuming the handleSubmit is connected to button click event
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}