package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import midlleware.TokenManager;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
