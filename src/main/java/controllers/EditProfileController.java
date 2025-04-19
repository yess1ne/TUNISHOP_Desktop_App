package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import services.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;

    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    private int userId;
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Optional: Additional setup can be added here
    }

    public void setUserId(int userId) {
        this.userId = userId;

        if (userId == -1) {
            showError("Invalid user session. Please log in again.");
            return;
        }

        loadUserData();
    }

    private void loadUserData() {
        System.out.println("Loading user with ID: " + userId);  // Debug print

        User user = userService.getUserById(userId);
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
        } else {
            showError("User not found.");
        }
    }

    @FXML
    private void handleUpdateProfile() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email.");
            return;
        }

        User user = userService.getUserById(userId);
        if (user != null) {
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            try {
                userService.modifier(user);
                loadUserData();
                showSuccess("Profile updated successfully.");
            } catch (Exception e) {
                showError("Failed to update profile: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleChangePassword() {
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showError("All password fields are required.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showError("New passwords do not match.");
            return;
        }

        if (!userService.checkPassword(userId, oldPass)) {
            showError("Old password is incorrect.");
            return;
        }

        try {
            User user = userService.getUserById(userId);
            userService.updatePassword(user, newPass);
            showSuccess("Password updated successfully.");
            oldPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        } catch (Exception e) {
            showError("Failed to update password: " + e.getMessage());
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
