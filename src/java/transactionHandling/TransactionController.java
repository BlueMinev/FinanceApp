package transactionHandling;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TransactionController {
    @FXML
    public TableColumn<transactionRecord, Void> actionColumn;
    public Button addTransactionButton;
    public DatePicker datePickerFrom;
    public DatePicker datePickerTo;
    public Button resetFiltersButton;
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
    private void handleFilterTransactions() {
        List<Predicate<transactionRecord>> predicates = new ArrayList<>();

        transactionTypes selectedTransactionType = transactionTypeComboBox.getValue();
        if (selectedTransactionType != null) {
            predicates.add(transactionFilters.hasTransactionType(selectedTransactionType));
        }

        billingTypes selectedBillingType = billingTypeComboBox.getValue();
        if (selectedBillingType != null) {
            predicates.add(transactionFilters.hasBillingType(selectedBillingType));
        }

        LocalDate selectedDateFrom = datePickerFrom.getValue();
        LocalDate selectedDateTo = datePickerTo.getValue();
        if (selectedDateFrom != null && selectedDateTo != null) {
            predicates.add(transactionFilters.hasDateInRange(selectedDateFrom, selectedDateTo));
        } else if (selectedDateFrom != null) {
            predicates.add(transactionFilters.hasDateEqualTo(selectedDateFrom));
        }

        List<transactionRecord> filteredTransactions = tracker.filterTransactions(predicates.toArray(new Predicate[0]));
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

    @FXML
    private void handleResetFilters() {
        transactionTypeComboBox.setValue(null);
        billingTypeComboBox.setValue(null);
        datePickerFrom.setValue(null);
        datePickerTo.setValue(null);
        transactions.setAll(tracker.transactions);
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

    public List<transactionRecord> getRecentTransactions() {
        // Assuming transactions are sorted by date in descending order
        return transactions.stream().limit(5).collect(Collectors.toList());
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(tr -> tr.transactionType() == transactionTypes.INCOME)
                .mapToDouble(transactionRecord::amount)
                .sum();
    }

    public double getTotalExpenses() {
        return transactions.stream()
                .filter(tr -> tr.transactionType() != transactionTypes.INCOME)
                .mapToDouble(transactionRecord::amount)
                .sum();
    }


}
