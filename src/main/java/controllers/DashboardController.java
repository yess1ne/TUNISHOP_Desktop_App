package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import midlleware.TokenManager;
import services.UserService;
import entities.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> actionsColumn;

    @FXML
    private Button loadUsersButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button editProfileButton;  // New button for Edit Profile

    private UserService userService;

    public DashboardController() {
        userService = new UserService();  // Initialize UserService
    }

    @FXML
    private void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().getRoleProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());

        // Set the action buttons
        actionsColumn.setCellFactory(param -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button updateButton = new Button("Update");
                    Button deleteButton = new Button("Delete");

                    updateButton.setOnAction(event -> updateUser(getTableRow().getItem()));
                    deleteButton.setOnAction(event -> deleteUser(getTableRow().getItem().getId()));

                    HBox buttonsBox = new HBox(updateButton, deleteButton);
                    buttonsBox.setSpacing(10);
                    setGraphic(buttonsBox);
                }
            }
        });

        // Load Users on start
        loadUsers();
    }

    @FXML
    private void loadUsers() {
        // Fetch users from the database using UserService
        List<User> users = userService.afficher();
        userTable.getItems().setAll(users);  // Populate the table
    }

    private void updateUser(User user) {
        try {
            // Load the update page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_user.fxml"));
            VBox updatePage = loader.load();

            // Get the UpdateController and pass the user to it
            UpdateUserController updateController = loader.getController();
            updateController.initialize(user);

            // Create a new Stage for the update page
            Stage stage = new Stage();
            stage.setTitle("Update User");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(updatePage));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load update page.");
        }
    }

    private void deleteUser(int userId) {
        // Delete user from the database using UserService
        userService.supprimer(userId);
        loadUsers();  // Reload the users after deletion
    }

    // Utility method to show an error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        // Clear the token to log out the user
        TokenManager.clearToken();

        // Show a successful logout alert
        showAlert("Logout Successful", "You have been logged out successfully.", Alert.AlertType.INFORMATION);

        // Close the current dashboard window
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        // Load the login page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent loginPage = loader.load();
            Scene loginScene = new Scene(loginPage);
            Stage loginStage = new Stage();
            loginStage.setScene(loginScene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load login page.");
        }
    }

    // New method to open the Edit Profile page
    @FXML
    public void handleEditProfile() {
        try {
            int userId = TokenManager.decodeId();

            if (userId == -1) {
                showError("You are not logged in or your session has expired.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfile.fxml"));
            Parent editProfileRoot = loader.load();

            // Set the user ID in the controller
            EditProfileController editProfileController = loader.getController();
            editProfileController.setUserId(userId);

            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(editProfileRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Edit Profile page.");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
