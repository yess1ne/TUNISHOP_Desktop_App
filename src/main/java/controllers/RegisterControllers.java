package controllers;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.UserService;

import java.io.IOException;

public class RegisterControllers {

    @FXML
    private TextField nomFx;  // First name field
    @FXML
    private TextField prenomFx;  // Last name field
    @FXML
    private TextField emailFx;  // Email field
    @FXML
    private PasswordField passwordFx;  // Password field
    @FXML
    private PasswordField newPasswordFx;  // Confirm password field
    @FXML
    private ComboBox<String> roleFx;  // ComboBox for role selection (SELLER or BUYER)

    private final UserService userService = new UserService();

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Retrieve the input values
            String firstName = nomFx.getText();
            String lastName = prenomFx.getText();
            String email = emailFx.getText();
            String password = passwordFx.getText();
            String confirmPassword = newPasswordFx.getText();
            String role = roleFx.getValue();

            // Validation checks
            if (firstName.isEmpty() || lastName.isEmpty()) {
                showAlert("Information", "Name and last name are required!");
                return;
            }

            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                showAlert("Information", "Email must contain '@' and be correctly formatted!");
                return;
            }

            if (role == null || role.isEmpty()) {
                showAlert("Information", "Role must be selected!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Error", "Passwords do not match");
                return;
            }

            // Create a new User object
            User newUser = new User(firstName, lastName, email, password, role);

            // Call service to add the new user
            userService.ajouter(newUser);

            // Show success message
            showAlert("Information", "User added successfully");

        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    void naviguer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            nomFx.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Utility method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Initialize the role ComboBox with available roles
        roleFx.getItems().addAll("ROLE_SELLER", "ROLE_BUYER");
    }
}
