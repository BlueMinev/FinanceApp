package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class dashboardController {


    public Label homeTab;

    @FXML
    private void openDashboard() {
        // Code to handle opening the dashboard goes here
        System.out.println("Opening Dashboard");
    }

    @FXML
    private void openBankAccounts() {
        try {
            // Load the shopping view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/banks.fxml"));
            Parent root = loader.load();

            // Create a new stage for the shopping view
            Stage shoppingStage = new Stage();
            shoppingStage.setScene(new Scene(root));
            shoppingStage.setTitle("BankAccounts");

            // Show the shopping stage
            shoppingStage.show();

            // Close the current stage (home view)
            Stage currentStage = (Stage) homeTab.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSavings() {
        // Code to handle opening the savings goes here
        System.out.println("Opening Savings");
    }

    @FXML
    private void openInvestments() {
        // Code to handle opening the investments goes here
        System.out.println("Opening Investments");
    }

    @FXML
    private void openExpenseTracker() {
        // Code to handle opening the expense tracker goes here
        System.out.println("Opening Expense Tracker");
    }

    @FXML
    private void openGoals() {
        // Code to handle opening the goals goes here
        System.out.println("Opening Goals");
    }

    @FXML
    private void openBudget() {
        // Code to handle opening the budget goes here
        System.out.println("Opening Budget");
    }

    @FXML
    private void openShopping() {
        try {
            // Load the shopping view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/shoppingView.fxml"));
            Parent root = loader.load();

            // Create a new stage for the shopping view
            Stage shoppingStage = new Stage();
            shoppingStage.setScene(new Scene(root));
            shoppingStage.setTitle("Shopping");

            // Show the shopping stage
            shoppingStage.show();

            // Close the current stage (home view)
            Stage currentStage = (Stage) homeTab.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings() {
        // Code to handle opening the settings goes here
        System.out.println("Opening Settings");
    }
}
