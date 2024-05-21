package transactionHandling;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import controllers.TransactionFormController;
import controllers.TransactionFormEditController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TransactionController {
    @FXML
    public TableColumn<transactionRecord, Void> actionColumn;
    public Button addTransactionButton;
    @FXML
    private TableColumn<transactionRecord, String> transactionIdColumn;
    @FXML
    private TableView<transactionRecord> transactionTableView;
    @FXML
    private TableColumn<transactionRecord, Double> amountColumn;
    @FXML
    private TableColumn<transactionRecord, transactionTypes> transactionTypeColumn;
    @FXML
    private TableColumn<transactionRecord, billingTypes> billingTypeColumn;
    @FXML
    private TableColumn<transactionRecord, LocalDate> dateColumn;
    @FXML
    private TableColumn<transactionRecord, String> descriptionColumn;
    @FXML
    private TableColumn<transactionRecord, String> placeColumn;
    @FXML
    private ComboBox<transactionTypes> transactionTypeComboBox;
    @FXML
    private ComboBox<billingTypes> billingTypeComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField totalIncomeField;
    @FXML
    private TextField totalExpensesField;
    @FXML
    private TextField balanceField;

    private transactionTracker tracker;
    private transactionFilters filters;
    private ObservableList<transactionRecord> transactions;
    private ObservableList<transactionRecord> transactionList;

    public TransactionController() {
        this.tracker = new transactionTracker();
        this.filters = new transactionFilters();
        this.transactions = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        setupTableColumns();
        actionColumn.setCellFactory(new Callback<TableColumn<transactionRecord, Void>, TableCell<transactionRecord, Void>>() {
            @Override
            public TableCell<transactionRecord, Void> call(final TableColumn<transactionRecord, Void> param) {
                return new TableCell<transactionRecord, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final HBox hbox = new HBox(editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> {
                            transactionRecord record = getTableView().getItems().get(getIndex());

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/transactionFormEdit.fxml"));
                                Parent parent = fxmlLoader.load();

                                TransactionFormEditController controller = fxmlLoader.getController();
                                controller.setTransactionController(TransactionController.this);  // Pass the main controller
                                controller.setRecord(record); // Pass the selected record to the edit form

                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });


                        deleteButton.setOnAction(event -> {
                            transactionRecord record = getTableView().getItems().get(getIndex());
                            if (tracker.deleteTransaction(record.transactionID())) {
                                getTableView().getItems().remove(record);
                                updateTotalFields();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        transactionTypeComboBox.setItems(FXCollections.observableArrayList(transactionTypes.values()));
        billingTypeComboBox.setItems(FXCollections.observableArrayList(billingTypes.values()));

        transactions.addAll(tracker.transactions);
        transactionTableView.setItems(transactions);

        totalIncomeField.setText(Double.toString(tracker.getTotalIn()));
        totalExpensesField.setText(Double.toString(tracker.getTotalOut()));
        balanceField.setText(Double.toString(tracker.getBalance()));
    }

    private void setupTableColumns() {
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().amount()).asObject());
        transactionTypeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().transactionType()));
        billingTypeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().billingType()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().date()));
        transactionIdColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().transactionID()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().description()));
        placeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().place()));
    }

    @FXML
    private void handleAddTransaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/transactionForm.fxml"));
            Parent root = loader.load();

            TransactionFormController formController = loader.getController();
            formController.setTransactionController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalFields() {
        totalIncomeField.setText(Double.toString(tracker.getTotalIn()));
        totalExpensesField.setText(Double.toString(tracker.getTotalOut()));
        balanceField.setText(Double.toString(tracker.getBalance()));
    }

    @FXML
    private void handleEditTransaction() {
        int selectedIndex = transactionTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < transactions.size()) {
            transactionRecord oldRecord = transactions.get(selectedIndex);
            if (oldRecord != null) {
                transactionRecord newRecord = new transactionRecord(
                        oldRecord.amount(),
                        oldRecord.transactionType(),
                        oldRecord.billingType(),
                        oldRecord.date(),
                        oldRecord.transactionID(),
                        oldRecord.description(),
                        oldRecord.place());

                transactions.set(selectedIndex, newRecord);
                transactionTableView.getItems().set(selectedIndex, newRecord);
                transactionTableView.refresh();
            } else {
                // Handle the situation when 'oldRecord' is null, maybe showing an error message or logging it.
            }
        } else {
            // Handle the case where 'selectedIndex' is not a valid index for 'transactions',
            // maybe show an error message or log it.
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

    public void addTransaction(transactionRecord record) {
        tracker.addTransaction(record.amount(), record.transactionType(), record.billingType(), record.date(), record.description(), record.place());
        transactions.add(record);
        transactionTableView.refresh();
        updateTotalFields();
    }
    public void editTransaction(transactionRecord record) {
        tracker.editTransaction(record.transactionID(), record.amount(), record.transactionType(), record.billingType(), record.date(), record.description(), record.place());

        // Find the index of the existing record and update it in the list
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).transactionID().equals(record.transactionID())) {
                transactions.set(i, record);
                break;
            }
        }

        transactionTableView.refresh();
        updateTotalFields();
    }



}
