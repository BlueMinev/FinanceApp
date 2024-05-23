package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class dashboardController {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void initialize() {
        openDashboard(); // Automatically open the dashboard when initialized
    }

    @FXML
    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent homeView = loader.load();
            mainBorderPane.setCenter(homeView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void openExpenseTracker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/financeReportGenerator.fxml"));
            Parent ReportView = loader.load();
            mainBorderPane.setCenter(ReportView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openGoals() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/transactionHandling.fxml"));
            Parent TransactionView = loader.load();
            mainBorderPane.setCenter(TransactionView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openBudget() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BudgetCreator.fxml"));
            Parent BudgetView = loader.load();
            mainBorderPane.setCenter(BudgetView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
