package controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import financeReport.financeReportGenerator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import transactionHandling.*;

public class FinanceReportController {

    @FXML
    private TextField timeframeField;

    @FXML
    private ComboBox<transactionTypes> expenseTypeComboBox;

    @FXML
    private TableView<transactionRecord> reportTable;

    @FXML
    private TableColumn<transactionRecord, LocalDate> dateColumn;

    @FXML
    private TableColumn<transactionRecord, Double> amountColumn;

    @FXML
    private TableColumn<transactionRecord, transactionTypes> typeColumn;

    @FXML
    private TableColumn<transactionRecord, String> descriptionColumn;

    @FXML
    private TableColumn<transactionRecord, String> placeColumn;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpensesLabel;

    @FXML
    private Label overallTotalLabel;

    @FXML
    private Label totalByCategoryLabel;

    @FXML
    private VBox categoryTotalsContainer;

    private financeReportGenerator reportGenerator;

    public void initialize() {
        // Initialize ComboBox
        expenseTypeComboBox.setItems(FXCollections.observableArrayList(transactionTypes.values()));

        // Initialize TableView columns
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().date()));
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().amount()).asObject());
        typeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().transactionType()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().description()));
        placeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().place()));

        // Initialize reportGenerator
        transactionTracker txnTracker = new transactionTracker();
        reportGenerator = new financeReportGenerator(txnTracker);
    }

    @FXML
    private void handleGetIncomes() {
        int timeframe = Integer.parseInt(timeframeField.getText());
        List<transactionRecord> incomes = reportGenerator.getIncomes(timeframe);
        updateTable(incomes);
        updateTotals(incomes);
    }

    @FXML
    private void handleGetExpenses() {
        int timeframe = Integer.parseInt(timeframeField.getText());
        List<transactionRecord> expenses = reportGenerator.getExpenses(timeframe);
        updateTable(expenses);
        updateTotals(expenses);
    }

    @FXML
    private void handleGetExpensesByType() {
        int timeframe = Integer.parseInt(timeframeField.getText());
        transactionTypes type = expenseTypeComboBox.getValue();
        List<transactionRecord> expensesByType = reportGenerator.getExpensesByType(timeframe, type);
        updateTable(expensesByType);
        updateTotals(expensesByType);
    }

    private void updateTable(List<transactionRecord> records) {
        ObservableList<transactionRecord> data = FXCollections.observableArrayList(records);
        reportTable.setItems(data);
    }

    private void updateTotals(List<transactionRecord> records) {
        double totalIncome = 0;
        double totalExpenses = 0;
        Map<transactionTypes, Double> categoryTotals = records.stream()
                .collect(Collectors.groupingBy(
                        transactionRecord::transactionType,
                        Collectors.summingDouble(transactionRecord::amount)
                ));

        for (Map.Entry<transactionTypes, Double> entry : categoryTotals.entrySet()) {
            if (entry.getValue() > 0) {
                totalIncome += entry.getValue();
            } else {
                totalExpenses += entry.getValue();
            }
        }

        totalIncomeLabel.setText(String.format("Total Income: £%.2f", totalIncome));
        totalExpensesLabel.setText(String.format("Total Expenses: £%.2f", totalExpenses));
        overallTotalLabel.setText(String.format("Overall Total: £%.2f", totalIncome + totalExpenses));

        categoryTotalsContainer.getChildren().clear();
        for (Map.Entry<transactionTypes, Double> entry : categoryTotals.entrySet()) {
            Label label = new Label(String.format("%s: £%.2f", entry.getKey(), entry.getValue()));
            categoryTotalsContainer.getChildren().add(label);
        }
    }
}
