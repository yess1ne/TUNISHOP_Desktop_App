package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    public void handleExploreClick() {
        // Handle the button click, you can navigate to another page or show a message
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Explore Now");
        alert.setHeaderText("Explore our marketplace!");
        alert.setContentText("Here you can view the products available for purchase.");
        alert.showAndWait();
    }

    @FXML
    public void handleLogoutClick() {

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
