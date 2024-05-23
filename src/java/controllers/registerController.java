package controllers;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerController implements Initializable{
    public TextField fNameField;
    public TextField lNameField;
    public TextField emailField;
    public TextField uNameField;
    public PasswordField passwordField1;
    public PasswordField passwordField2;
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(actionEvent -> onRegister());

    }
    private boolean isValidEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private void onRegister(){
        if (fNameField.getText().equals("")){
            System.out.println("Please put enter Your First Name");
            // errorLabel.setText("Please put enter Your First Name");
        } else if (lNameField.getText().equals("")){
            System.out.println("Please put enter Your Last Name");
            // errorLabel.setText("Please put enter Your Last Name");
        } else if (!(isValidEmail(emailField.getText()))){
            System.out.println("This is not a valid email");
            // errorLabel.setText("This is not a valid email");
        }
        //else if (email is already in database){
            // errorLabel.setText("An account already exists with this email please log in");
        //}
        else if (uNameField.getText().equals("")){
            System.out.println("Please put enter a Username");
            // errorLabel.setText("Please put enter a Username");
        }
        //else if (username exists in database){
            // errorLabel.setText("An account already exists with this username please choose another");
        //}
        else if (passwordField1.getText().equals("")){
            System.out.println("Please put enter a password1");
            // errorLabel.setText("Please put enter a password");
        } else if (passwordField2.getText().equals("")){
            System.out.println("Please put enter a password2");
            // errorLabel.setText("Please put enter a password");
        } else if (!(passwordField1.getText().equals(passwordField2.getText()))){
            System.out.println("The passwords are not the same");
            // errorLabel.setText("The passwords are not the same");
        }  else {
            // add to database


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
        }
    }
}
