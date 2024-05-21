package controllers;

import javafx.event.ActionEvent;
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

public class TransactionFormEditController {
    public Button editButton;
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

    private transactionRecord record;
    private TransactionController transactionController;

    public void setRecord(transactionRecord record) {
        this.record = record;
        if(record != null) {
            amountField.setText(String.valueOf(record.amount()));
            transactionTypeField.setValue(record.transactionType());
            billingTypeField.setValue(record.billingType());
            dateField.setValue(record.date());
            descriptionField.setText(record.description());
            placeField.setText(record.place());
        }
    }

    public void setTransactionController(TransactionController transactionController) {
        this.transactionController = transactionController;
    }
    @FXML
    public void handleEdit(ActionEvent actionEvent) {
        double amount = Double.parseDouble(amountField.getText());
        transactionTypes transactionType = transactionTypeField.getValue();
        billingTypes billingType = billingTypeField.getValue();
        LocalDate date = dateField.getValue();
        String description = descriptionField.getText();
        String place = placeField.getText();

        transactionRecord updatedRecord = new transactionRecord(amount, transactionType, billingType, date, record.transactionID(), description, place);
      //  transactionController.updateTransaction(updatedRecord);

        // Close the stage
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
