package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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

    public void setRecord(transactionRecord record) {
        if(record != null) {
            amountField.setText(String.valueOf(record.amount()));
            transactionTypeField.setValue(record.transactionType());
            billingTypeField.setValue(record.billingType());
            dateField.setValue(record.date());
            descriptionField.setText(record.description());
            placeField.setText(record.place());
        } else {
            // Clear all the fields
            amountField.setText(null);
            transactionTypeField.setValue(null);
            billingTypeField.setValue(null);
            dateField.setValue(null);
            descriptionField.setText(null);
            placeField.setText(null);
        }
    }

    public void handleEdit(ActionEvent actionEvent) {
        Double amount = Double.parseDouble(amountField.getText());
        transactionTypes transactionType = transactionTypeField.getValue();
        billingTypes billingType = billingTypeField.getValue();
        LocalDate date = dateField.getValue();
        String transactionId = descriptionField.getText();
        String description = descriptionField.getText();
        String place = placeField.getText();

        transactionRecord updatedRecord = new transactionRecord(amount, transactionType, billingType, date, transactionId, description, place);

        // then call the database's 'update' method with the updated record.
        

    }
}
