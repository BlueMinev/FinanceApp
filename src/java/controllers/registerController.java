package controllers;
import database.DBController;
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
    public Label errorLabel;
    private DBController dbController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(actionEvent -> onRegister());
        dbController = new DBController();
    }
    private boolean isValidEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
    private void addUserToDB() {
        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String Email = emailField.getText();
        String uName = uNameField.getText();
        String password = passwordField1.getText();

        dbController.addUser(fName, lName, Email, uName, Hasher.getHashedValue(password));

    }

    private void onRegister(){
        if (fNameField.getText().equals("")){
             errorLabel.setText("Please enter Your First Name");
        } else if (lNameField.getText().equals("")){
             errorLabel.setText("Please enter Your Last Name");
        } else if (!(isValidEmail(emailField.getText()))){
             errorLabel.setText("This is not a valid email");
        }
        //else if (email is already in database){
            // errorLabel.setText("An account already exists with this email please log in");
        //}
        else if (uNameField.getText().equals("")){
             errorLabel.setText("Please enter a Username");
        }
        //else if (username exists in database){
            // errorLabel.setText("An account already exists with this username please choose another");
        //}
        else if (passwordField1.getText().equals("")){
            errorLabel.setText("Please enter a password");
        } else if (passwordField2.getText().equals("")){
             errorLabel.setText("Please re-enter a password");
        } else if (!(passwordField1.getText().equals(passwordField2.getText()))){
             errorLabel.setText("The passwords are not the same");
        }  else {
            addUserToDB();


            // Get the stage from any control (e.g., loginButton)
            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Close the stage
            stage.close();

            try {
                // Load the FXML file for the dashboard scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/loginView.fxml"));
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
