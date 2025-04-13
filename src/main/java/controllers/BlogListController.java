package controllers;

import entities.Blog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.BlogService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BlogListController implements Initializable {

    @FXML
    private ListView<Blog> blogListView;

    private final BlogService blogService = new BlogService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get the list of blogs from the database
            List<Blog> blogs = blogService.getAllBlogs();
            ObservableList<Blog> observableBlogs = FXCollections.observableArrayList(blogs);
            blogListView.setItems(observableBlogs);

            // Set a custom cell factory for the ListView
            blogListView.setCellFactory(listView -> new ListCell<Blog>() {
                private final ImageView imageView = new ImageView();
                private final Label titleLabel = new Label();
                private final Label descriptionLabel = new Label();
                private final Label infoLabel = new Label();
                private final VBox vBox = new VBox(titleLabel, descriptionLabel, infoLabel);
                private final HBox hBox = new HBox(imageView, vBox);
                private final Button deleteButton = new Button("Delete");

                // À l'intérieur de blogListView.setCellFactory...
                private final Button updateButton = new Button("Update");

                {
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(true);
                    vBox.setSpacing(5);
                    hBox.setSpacing(10);

                    deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    updateButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                    deleteButton.setOnAction(event -> handleDeleteBlog(getItem()));
                    updateButton.setOnAction(event -> handleUpdateBlog(getItem(), event));
                }


                @Override
                protected void updateItem(Blog blog, boolean empty) {
                    super.updateItem(blog, empty);
                    if (empty || blog == null) {
                        setGraphic(null);
                    } else {
                        // Clear the existing content of the HBox to avoid duplicates
                        hBox.getChildren().clear();

                        try {
                            imageView.setImage(new Image("file:" + blog.getImage()));
                        } catch (Exception e) {
                            imageView.setImage(null); // Handle if the image doesn't exist
                        }

                        titleLabel.setText("📝 " + blog.getTitre());
                        descriptionLabel.setText("📖 " + blog.getDescription());
                        infoLabel.setText("❤️ Likes: " + blog.getNbLikes() + "   🗓 " + blog.getDateCreation());
                        // Add the imageView, VBox, and the delete button to the HBox
                        hBox.getChildren().addAll(imageView, vBox, deleteButton, updateButton);
                        setGraphic(hBox);
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer les blogs : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Method to delete a blog
    private void handleDeleteBlog(Blog blog) {
        try {
            blogService.deleteBlogByTitre(blog.getTitre()); // Delete the blog by its title
            blogListView.getItems().remove(blog); // Remove the deleted blog from the ListView
            showAlert("Succès", "Le blog a été supprimé avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de supprimer le blog : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Display alert messages
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Handle the "Retour" button to navigate back
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
    private void handleUpdateBlog(Blog blog, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_blog.fxml"));
            Parent root = loader.load();

            // Passe les données au contrôleur
            UpdateBlogController controller = loader.getController();
            controller.setBlogData(blog);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de mise à jour : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
