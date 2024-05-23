package budgetCreator;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

import java.util.Map;

public class BudgetCreatorController {

    @FXML
    private TextField homeBudget, billBudget, shoppingBudget, groceriesBudget, generalBudget, eatoutBudget, transportBudget, entertainmentBudget;
    @FXML
    private Label weeksRemainingLabel;
    @FXML
    private ProgressBar homeBudgetBar, billBudgetBar, shoppingBudgetBar, groceriesBudgetBar, generalBudgetBar, eatoutBudgetBar, transportBudgetBar, entertainmentBudgetBar, overallBudgetBar;
    @FXML
    private Label homeBudgetLeft, homeBudgetLeftPercentage, billBudgetLeft, billBudgetLeftPercentage, shoppingBudgetLeft, shoppingBudgetLeftPercentage, groceriesBudgetLeft, groceriesBudgetLeftPercentage, generalBudgetLeft, generalBudgetLeftPercentage, eatoutBudgetLeft, eatoutBudgetLeftPercentage, transportBudgetLeft, transportBudgetLeftPercentage, entertainmentBudgetLeft, entertainmentBudgetLeftPercentage, overallBudgetLeft, overallBudgetLeftPercentage;

    private BudgetCreator budgetCreator;
    private FinanceTracker financeTracker;

    @FXML
    public void initialize() {
        budgetCreator = new BudgetCreator();
        financeTracker = new FinanceTracker();
    }

    @FXML
    public void handleSubmitBudgets() {
        try {
            budgetCreator.addBudgetMaximum("Home", Double.parseDouble(homeBudget.getText()));
            budgetCreator.addBudgetMaximum("Bill", Double.parseDouble(billBudget.getText()));
            budgetCreator.addBudgetMaximum("Shopping", Double.parseDouble(shoppingBudget.getText()));
            budgetCreator.addBudgetMaximum("Groceries", Double.parseDouble(groceriesBudget.getText()));
            budgetCreator.addBudgetMaximum("General", Double.parseDouble(generalBudget.getText()));
            budgetCreator.addBudgetMaximum("Eatout", Double.parseDouble(eatoutBudget.getText()));
            budgetCreator.addBudgetMaximum("Transport", Double.parseDouble(transportBudget.getText()));
            budgetCreator.addBudgetMaximum("Entertainment", Double.parseDouble(entertainmentBudget.getText()));

            budgetCreator.findWeeksRemaining();
            weeksRemainingLabel.setText("Weeks remaining in the month: " + budgetCreator.weeksRemainingInMonth);

            Map<String, Double> expenditures = financeTracker.fetchExpenditures();

            setBudgetLeft("Home", homeBudgetLeft, homeBudgetLeftPercentage, homeBudgetBar, expenditures, Color.RED);
            setBudgetLeft("Bill", billBudgetLeft, billBudgetLeftPercentage, billBudgetBar, expenditures, Color.BLUE);
            setBudgetLeft("Shopping", shoppingBudgetLeft, shoppingBudgetLeftPercentage, shoppingBudgetBar, expenditures, Color.GREEN);
            setBudgetLeft("Groceries", groceriesBudgetLeft, groceriesBudgetLeftPercentage, groceriesBudgetBar, expenditures, Color.YELLOW);
            setBudgetLeft("General", generalBudgetLeft, generalBudgetLeftPercentage, generalBudgetBar, expenditures, Color.ORANGE);
            setBudgetLeft("Eatout", eatoutBudgetLeft, eatoutBudgetLeftPercentage, eatoutBudgetBar, expenditures, Color.PURPLE);
            setBudgetLeft("Transport", transportBudgetLeft, transportBudgetLeftPercentage, transportBudgetBar, expenditures, Color.BROWN);
            setBudgetLeft("Entertainment", entertainmentBudgetLeft, entertainmentBudgetLeftPercentage, entertainmentBudgetBar, expenditures, Color.PINK);

            setOverallBudget(expenditures);

        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter valid budget amounts.");
        }
    }

    private void setBudgetLeft(String category, Label budgetLeftLabel, Label budgetLeftPercentageLabel, ProgressBar budgetBar, Map<String, Double> expenditures, Color color) {
        double expenditure = expenditures.getOrDefault(category, 0.0);
        double budgetLeft = budgetCreator.findBudgetLeft(category, expenditure);
        double budgetLeftPercentage = budgetCreator.findBudgetLeftPercentage(category, expenditure);
        budgetLeftLabel.setText(String.format("£%.2f", budgetLeft));
        budgetLeftPercentageLabel.setText(String.format("%.2f%%", budgetLeftPercentage));
        budgetBar.setProgress(budgetLeftPercentage / 100.0);
        budgetBar.setStyle("-fx-accent: " + toHexString(color) + ";");
    }

    private void setOverallBudget(Map<String, Double> expenditures) {
        double totalExpenditure = expenditures.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalBudget = budgetCreator.getTotalBudget();
        double overallBudgetLeftValue = totalBudget - totalExpenditure;
        double overallBudgetLeftPercentageValue = (overallBudgetLeftValue / totalBudget) * 100.0;
        overallBudgetLeftPercentageValue = Math.max(0, overallBudgetLeftPercentageValue); // Ensure percentage is not negative
        overallBudgetLeft.setText(String.format("£%.2f", overallBudgetLeftValue));
        overallBudgetLeftPercentage.setText(String.format("%.2f%%", overallBudgetLeftPercentageValue));
        overallBudgetBar.setProgress(overallBudgetLeftPercentageValue / 100.0);
    }

    public DoubleProperty overallBudgetProgressProperty() {
        return overallBudgetBar.progressProperty();
    }

    private String toHexString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
