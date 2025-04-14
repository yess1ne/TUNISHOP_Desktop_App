package controllers;

import entities.Products;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.ProductService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UpdateProductPageController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField priceField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField quantityField;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView currentImageView;

    private String imagePath;

    private final ProductService productService = new ProductService();
    private Products product;

    // Méthode appelée pour initialiser les champs avec les données du produit
    public void setProduct(Products product) {
        this.product = product;

        titleField.setText(product.getTitle());
        priceField.setText(String.valueOf(product.getPrice()));
        descriptionField.setText(product.getDescription());
        categoryField.setText(product.getCategorie());
        locationField.setText(product.getLocation());
        quantityField.setText(String.valueOf(product.getQuantity()));

        imagePath = product.getImage();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            currentImageView.setImage(new Image(imageFile.toURI().toString()));
        }
        imageLabel.setText(imageFile.getName());
    }

    // Gestion de l'importation d'image
    @FXML
    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            imageLabel.setText(file.getName());
            currentImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    // Méthode de mise à jour du produit
    @FXML
    public void handleUpdateProduct() {
        try {
            String title = titleField.getText();
            int price = Integer.parseInt(priceField.getText());
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String location = locationField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            product.setTitle(title);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategorie(category);
            product.setLocation(location);
            product.setQuantity(quantity);
            product.setImage(imagePath);

            // 🔁 Assure que l'utilisateur est bien défini (par exemple, simulé ici avec ID 1)
            if (product.getUtilisateur() == null) {
                User utilisateur = new User();
                utilisateur.setId(1); // Remplacer par l'ID réel de l'utilisateur connecté
                product.setUtilisateur(utilisateur);
            }

            productService.modifier(product);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Product Updated Successfully");
            alert.setContentText("The product has been updated.");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Price and Quantity must be valid numbers.");
        } catch (SQLException e) {
            showAlert("Database Error", "Could not update the product: " + e.getMessage());
        }
    }

    // Retourner à la liste des produits
    @FXML
    public void goBackToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/productListPage.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Product List");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) titleField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to return to product list.");
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
