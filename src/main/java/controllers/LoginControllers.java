package controllers;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import midlleware.TokenManager;
import services.UserService;

import java.io.IOException;

public class LoginControllers {

    @FXML
    private TextField emailFx;

    @FXML
    private PasswordField passwordFx;

    @FXML
    private Button btnForgetPassword;

    @FXML
    private Button btnLogBack;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private AnchorPane forgetPassword;

    private final UserService userService = new UserService();

    @FXML
<<<<<<< HEAD
=======
    public void initialize() {
        // Check if token exists and is valid when the application starts
        checkTokenAndNavigate();
    }

    private void checkTokenAndNavigate() {
        if (TokenManager.hasToken()) {
            // If the token exists, check if it is valid and navigate accordingly
            if (TokenManager.verifToken()) {
                String userRole = TokenManager.getRoleFromToken();
                switch (userRole) {
                    case "ROLE_ADMIN":
                        loadFXML("/adminDashboard.fxml");
                        break;
                    case "ROLE_BUYER":
                        loadFXML("/home.fxml");
                        break;
                    case "ROLE_SELLER":
                        loadFXML("/sellerPage.fxml");
                        break;
                    default:
                        showAlert("Login Failed", "Unknown role.");
                        break;
                }
            } else {
                TokenManager.clearToken();  // Clear invalid token if it expired
                showAlert("Login", "Session expired. Please log in again.");
            }
        }
    }

    @FXML
>>>>>>> users
    void authentifier(ActionEvent event) {
        String enteredEmail = emailFx.getText().trim();  // Trimming whitespace
        String enteredPassword = passwordFx.getText().trim();  // Trimming whitespace

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            showAlert("Login Failed", "Email and Password cannot be empty.");
            return;
        }

        User user = new User();
        user.setEmail(enteredEmail);
        user.setPassword(enteredPassword);

        try {
            // Authenticate the user and get the token
            String token = userService.authentifier(user);
            if (token != null) {
                System.out.println("Authentication successful. Token: " + token);

                // Get the role of the user from the token (or from the database)
                String userRole = TokenManager.getRoleFromToken();

                // Load appropriate FXML based on the role
                if ("ROLE_ADMIN".equals(userRole)) {
                    loadFXML("/adminDashboard.fxml");  // Admin dashboard
                } else if ("ROLE_BUYER".equals(userRole)) {
                    loadFXML("/home.fxml");  // Home page for buyer
                } else if ("ROLE_SELLER".equals(userRole)) {
                    loadFXML("/sellerPage.fxml");  // Seller page
                } else {
                    showAlert("Login Failed", "Unknown role assigned to user.");
                }

            } else {
                System.out.println("Authentication failed. Incorrect email or password.");
                showAlert("Login Failed", "Incorrect email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            showAlert("Error", "An error occurred during authentication.");
        }
    }

    private void loadFXML(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();
            emailFx.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the page.");
        }
    }

    @FXML
    void naviguer(ActionEvent event) {
        loadFXML("/Register.fxml");
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnForgetPassword) {
            loginForm.setVisible(false);
            forgetPassword.setVisible(true);
        }
        if (actionEvent.getSource() == btnLogBack) {
            loginForm.setVisible(true);
            forgetPassword.setVisible(false);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
