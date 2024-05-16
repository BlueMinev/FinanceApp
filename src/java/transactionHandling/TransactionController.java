package transactionHandling;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TransactionController {
    @FXML private TableView<transactionRecord> transactionTableView;
    @FXML private ComboBox<transactionTypes> transactionTypeComboBox;
    @FXML private ComboBox<billingTypes> billingTypeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private TextField totalIncomeField;
    @FXML private TextField totalExpensesField;
    @FXML private TextField balanceField;

    private transactionTracker tracker;
    private transactionFilters filters;
    private ObservableList<transactionRecord> transactions;

    public TransactionController() {
        this.tracker = new transactionTracker();
        this.filters = new transactionFilters();
        this.transactions = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // Initialize the ComboBoxes with available options
        transactionTypeComboBox.setItems(FXCollections.observableArrayList(transactionTypes.values()));
        billingTypeComboBox.setItems(FXCollections.observableArrayList(billingTypes.values()));

        // Bind the TableView to the transactions ObservableList
        transactionTableView.setItems(transactions);

        // Bind the total fields to the transactionTracker values
        totalIncomeField.textProperty().bind(new SimpleStringProperty(Double.toString(tracker.getTotalIn())));
        totalExpensesField.textProperty().bind(new SimpleStringProperty(Double.toString(tracker.getTotalOut())));
        balanceField.textProperty().bind(new SimpleStringProperty(Double.toString(tracker.getBalance())));
    }

    @FXML
    private void handleAddTransaction() {
        double amount = Double.parseDouble(transactionTableView.getEditingCell().getText());
        transactionTypes type = transactionTypeComboBox.getValue();
        billingTypes billing = billingTypeComboBox.getValue();
        LocalDate date = datePicker.getValue();

        transactionRecord newRecord = new transactionRecord(amount, type, billing, date);
        tracker.addTransaction(newRecord);
        transactions.add(newRecord);
    }

    @FXML
    private void handleEditTransaction() {
        int selectedIndex = transactionTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            transactionRecord record = transactions.get(selectedIndex);
            record.setDisableProperty(false);
            // Update the UI components to show the record's data
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        int selectedIndex = transactionTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            transactionRecord record = transactions.get(selectedIndex);
            tracker.removeTransaction(record);
            transactions.remove(record);
        }
    }

    @FXML
    private void handleFilterTransactions() {
        List<transactionRecord> filteredTransactions = tracker.filterTransactions(
                filters.hasTransactionType(transactionTypeComboBox.getValue()),
                filters.hasBillingType(billingTypeComboBox.getValue()),
                filters.hasDateEqualTo(datePicker.getValue())
        );
        transactions.setAll(filteredTransactions);
    }

    @FXML
    private void handleTransactionTypeChange() {
        handleFilterTransactions();
    }

    @FXML
    private void handleBillingTypeChange() {
        handleFilterTransactions();
    }

    @FXML
    private void handleDateChange() {
        handleFilterTransactions();
    }
}
