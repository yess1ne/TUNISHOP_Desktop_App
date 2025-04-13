package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeController {
    @FXML
    private TextField textField;
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

    public void handleAddBlogClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_blog.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleShowBlogsListClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/show_blogs_list.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
