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


public class TransactionFormController {

    public Button submitButton;
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
        transactionTypes transactionType = transactionTypeField.getValue();
        billingTypes billingType = billingTypeField.getValue();
        LocalDate date = dateField.getValue();
        String purchase = descriptionField.getText();
        String place = placeField.getText();


        // Create a new transactionRecord object
        transactionRecord newTransaction = new transactionRecord(
                amount, transactionType, billingType, date, null, purchase, place
        );

        // Update the TableView in TransactionController
        transactionController.addTransaction(newTransaction);

        // Close the form
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
