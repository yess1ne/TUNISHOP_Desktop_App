package test;

import entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class MainFX extends Application {
    private final UserService userService = new UserService();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        try {
            // Load the FXML file (Login.fxml in this case)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();  // Load the root element from the FXML file

            // Set the scene with the loaded FXML root node
            Scene scene = new Scene(root);

            // Set the title for the stage (window)
            primaryStage.setTitle("TuniShop Application");

            // Set the scene to the primary stage and show it
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
