package controllers;

import entities.Blog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.BlogService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateBlogController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dateCreationPicker;

    @FXML
    private TextField imageField;

    @FXML
    private Spinner<Integer> likesSpinner;

    @FXML
    private Button updateButton;

    private Blog originalBlog;
    private final BlogService blogService = new BlogService();

    // Méthode appelée par le contrôleur précédent pour pré-remplir les champs
    public void setBlogData(Blog blog) {
        this.originalBlog = blog;

        titreField.setText(blog.getTitre());
        descriptionField.setText(blog.getDescription());
        dateCreationPicker.setValue(blog.getDateCreation());
        imageField.setText(blog.getImage());
        likesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, blog.getNbLikes()));
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        try {
            Blog updatedBlog = new Blog(
                    titreField.getText(),
                    descriptionField.getText(),
                    likesSpinner.getValue(),
                    dateCreationPicker.getValue(),
                    imageField.getText(),
                    originalBlog.getUtilisateur_id_id()  // garde l'utilisateur actuel
            );

            blogService.updateBlogByTitre(originalBlog.getTitre(), updatedBlog);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le blog a été mis à jour avec succès !");
            alert.showAndWait();

            // Fermer la fenêtre actuelle après la mise à jour
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de mettre à jour le blog : " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
