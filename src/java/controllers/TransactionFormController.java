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
import transactionHandling.transactionTracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.*;

public class TransactionFormController {

    public Button submitButton;
    private TransactionController transactionController;
    private transactionTracker tracker = new transactionTracker();

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
        LocalDate date = LocalDate.parse(dateField.getValue().format(ofPattern("yyyy-MM-dd")));
        String purchase = descriptionField.getText();
        String place = placeField.getText();
        int accountNum = 0;

        //Add new transaction
        this.tracker.addTransaction(amount,transactionType, billingType, date, purchase,place);

        // Update the TableView
        transactionController.addTransaction();

        // Assuming the handleSubmit is connected to button click event
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}