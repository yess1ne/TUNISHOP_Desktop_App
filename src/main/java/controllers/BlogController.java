package controllers;

import entities.Blog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.BlogService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class BlogController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nbLikesField;

    @FXML
    private TextField imageField;

    private final BlogService blogService = new BlogService();

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            imageField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleSubmit() {
        try {
            String titre = titreField.getText();
            String description = descriptionField.getText();
            int nbLikes = Integer.parseInt(nbLikesField.getText());
            String image = imageField.getText();

            if (titre.isEmpty() || description.isEmpty() || image.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.", Alert.AlertType.ERROR);
                return;
            }

            Blog blog = new Blog(titre, description, nbLikes, LocalDate.now(), image, 2); // 1 est un exemple d'ID utilisateur
            blogService.ajouter(blog);
            showAlert("Succès", "Blog ajouté avec succès.", Alert.AlertType.INFORMATION);
            clearFields();
            returnToHome();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Nombre de likes invalide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur base de données : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        nbLikesField.clear();
        imageField.clear();
    }
    public void returnToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titreField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            stage.setWidth(800);
            stage.setHeight(600);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
