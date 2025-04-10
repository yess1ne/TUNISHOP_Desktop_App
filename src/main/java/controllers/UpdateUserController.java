package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;

import java.sql.SQLException;

public class UpdateUserController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    private User currentUser;
    private UserService userService;

    public UpdateUserController() {
        userService = new UserService();
    }

    public void initialize(User user) {
        currentUser = user;
        firstNameField.setText(user.getNom());
        lastNameField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        roleComboBox.getItems().addAll("ROLE_ADMIN", "ROLE_BUYER", "ROLE_SELLER");
        roleComboBox.setValue(user.getRole());
    }

    @FXML
    private void handleSave() {
        // Collect new values from fields
        String updatedFirstName = firstNameField.getText();
        String updatedLastName = lastNameField.getText();
        String updatedEmail = emailField.getText();
        String updatedRole = roleComboBox.getValue();

        // Validate fields
        if (updatedFirstName.isEmpty() || updatedLastName.isEmpty() || updatedEmail.isEmpty() || updatedRole == null) {
            showError("All fields must be filled in.");
            return;
        }

        // Update the user object
        currentUser.setNom(updatedFirstName);
        currentUser.setPrenom(updatedLastName);
        currentUser.setEmail(updatedEmail);
        currentUser.setRole(updatedRole);

        try {
            // Call service to update the user in the database
            userService.modifier(currentUser);

            // Close the update window after successful update
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to update user. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Any unsaved changes will be lost.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close the update window without saving changes
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();
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
