
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Load the FXML file and set up the scene
        Parent root = FXMLLoader.load(getClass().getResource("/views/loginView.fxml"));
        Scene scene = new Scene(root);

        //Set up the stage
        primaryStage.initStyle(StageStyle.UNDECORATED); //Removes default window decorations
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}