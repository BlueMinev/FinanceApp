package controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    public TextField emailField;
    public Button loginButton;
    public PasswordField passwordField;
    public Label errorLabel;
    public Button registerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(actionEvent -> onLogin());
        registerButton.setOnAction(actionEvent -> onRegister());
        Integer count = 0;
    }

    private void onRegister() {
        Stage stage = (Stage) registerButton.getScene().getWindow();

        // Close the stage
        stage.close();

        try {
            // Load the FXML file for the dashboard scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/registerView.fxml"));
            Parent root = loader.load();

            // Create a new stage for the dashboard scene
            Stage registerStage = new Stage();
            registerStage.setTitle("Register");

            // Set the scene for the dashboard stage
            Scene registerScene = new Scene(root);
            registerStage.setScene(registerScene);

            // Show the dashboard stage
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, such as showing an error message
        }
    }

    private void onLogin() {

        if (!(emailField.getText().equals("Admin") )){
            errorLabel.setText("Account does not exist");}
        else if (!(passwordField.getText().equals("Admin"))){
            errorLabel.setText("Password Wrong");
        }
         else if  (emailField.getText().equals("Admin") && passwordField.getText().equals("Admin")){


            // Get the stage from any control (e.g., loginButton)
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Close the stage
            stage.close();

            try {
                // Load the FXML file for the dashboard scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboardView.fxml"));
                Parent root = loader.load();

                // Create a new stage for the dashboard scene
                Stage dashboardStage = new Stage();
                dashboardStage.setTitle("Dashboard");

                // Set the scene for the dashboard stage
                Scene dashboardScene = new Scene(root);
                dashboardStage.setScene(dashboardScene);

                // Show the dashboard stage
                dashboardStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception, such as showing an error message
            }

        } else {
            errorLabel.setText("Login Failed");
        }
    }
}
