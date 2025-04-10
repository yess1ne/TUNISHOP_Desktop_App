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
import java.sql.SQLException;

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
    void authentifier(ActionEvent event) {
        String enteredEmail = emailFx.getText();
        String enteredPassword = passwordFx.getText();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();
            emailFx.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
