package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeController {

    @FXML
    public void handleExploreClick() {
        // Handle the button click to explore the marketplace
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Explore Now");
        alert.setHeaderText("Explore our marketplace!");
        alert.setContentText("Here you can view the products available for purchase.");
        alert.showAndWait();
    }

    @FXML
    public void handleLogoutClick() {
        // Handle logout logic here if needed
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You have successfully logged out.");
        alert.setContentText("You can now log back in.");
        alert.showAndWait();
    }

    // Method to load the "Add Reclamation" interface
    @FXML
    public void loadAddReclamationPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/addReclamation.fxml")); // Correct path
            Parent root = loader.load();

            // Create a new stage (window) and set the loaded root as the scene
            Stage stage = new Stage();
            stage.setTitle("Add Reclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the 'Add Reclamation' page: " + e.getMessage());
        }
    }

    // Method to load the "List Reclamations" interface
    @FXML
    public void loadListReclamationsPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/listReclamations.fxml")); // Correct path
            Parent root = loader.load();

            // Create a new stage (window) and set the loaded root as the scene
            Stage stage = new Stage();
            stage.setTitle("List of Reclamations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the 'List Reclamations' page: " + e.getMessage());
        }
    }

    // Utility method to show an error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
