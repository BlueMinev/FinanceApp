package controllers;

import budgetCreator.BudgetCreatorController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transactionHandling.TransactionController;
import transactionHandling.transactionFilters;
import transactionHandling.transactionRecord;
import transactionHandling.transactionTracker;

import java.time.LocalDate;

public class HomeController {

    public ProgressBar overallBudgetBar;
    public Label overallBudgetPercentage;
    @FXML
    private TableView<transactionRecord> recentTransactionsTable;
    @FXML
    private TableColumn<transactionRecord, LocalDate> dateColumn;
    @FXML
    private TableColumn<transactionRecord, String> descriptionColumn;
    @FXML
    private TableColumn<transactionRecord, Double> amountColumn;

    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalExpensesLabel;
    @FXML
    private Label balanceLabel;

    private TransactionController transactionController;
    private BudgetCreatorController budgetCreatorController = new BudgetCreatorController();
    private final ObservableList<transactionRecord> transactions;
    private final transactionTracker tracker;

    public HomeController() {
        this.tracker = new transactionTracker();
        this.transactions = FXCollections.observableArrayList();
        this.budgetCreatorController = new BudgetCreatorController();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        transactionController = new TransactionController();
        budgetCreatorController.initialize();


        transactions.addAll(tracker.transactions);
        recentTransactionsTable.setItems(transactions);

        totalIncomeLabel.setText(Double.toString(tracker.getTotalIn()));
        totalExpensesLabel.setText(Double.toString(tracker.getTotalOut()));
        balanceLabel.setText(Double.toString(tracker.getBalance()));

//        // Bind the overall budget bar progress property
//        overallBudgetBar.progressProperty().bind(budgetCreatorController.overallBudgetProgressProperty());
    }

    private void setupTableColumns() {
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().amount()).asObject());
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().date()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().description()));
    }



}
