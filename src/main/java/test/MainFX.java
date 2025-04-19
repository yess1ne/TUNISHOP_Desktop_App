package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
<<<<<<< HEAD
=======
import midlleware.TokenManager;
>>>>>>> users

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
<<<<<<< HEAD
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
=======
            // Check if there is a valid token
            if (TokenManager.hasToken() && TokenManager.verifToken()) {
                // If a valid token exists, navigate based on the user role
                String userRole = TokenManager.getRoleFromToken();
                String resourcePath = "";

                switch (userRole) {
                    case "ROLE_ADMIN":
                        resourcePath = "/adminDashboard.fxml";
                        break;
                    case "ROLE_BUYER":
                        resourcePath = "/home.fxml";
                        break;
                    case "ROLE_SELLER":
                        resourcePath = "/sellerPage.fxml";
                        break;
                    default:
                        // If role is unknown, load the login screen
                        resourcePath = "/Login.fxml";
                        break;
                }

                // Load the corresponding FXML file based on the user's role
                FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                primaryStage.setTitle("TuniShop Application");
                primaryStage.setScene(scene);
                primaryStage.show();

            } else {
                // If no valid token, load the login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                primaryStage.setTitle("TuniShop Application");
                primaryStage.setScene(scene);
                primaryStage.show();
            }

>>>>>>> users
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
