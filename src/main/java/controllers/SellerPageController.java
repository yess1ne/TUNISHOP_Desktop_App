package controllers;

import entities.Products;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import services.ProductService;

import java.io.File;
import java.sql.SQLException;
import java.io.IOException;

public class SellerPageController {

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

    private String imagePath;
    private ProductService productService;

    public SellerPageController() {
        productService = new ProductService();
    }

    @FXML
    public void handleAddProduct() {
        try {
            String title = titleField.getText();
            int price = Integer.parseInt(priceField.getText());
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String location = locationField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            // 🔁 Création d’un objet User (à remplacer plus tard par une récupération réelle)
            User utilisateur = new User();
            utilisateur.setId(1); // Remplacer 1 par l'ID de l'utilisateur connecté

            // 🔁 Création du produit avec objet utilisateur
            Products newProduct = new Products(title, price, description, category, location, imagePath, quantity, utilisateur);

            productService.ajouter(newProduct);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Product Added Successfully");
            alert.setContentText("The product has been added to the database.");
            alert.showAndWait();

            clearFields();

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid numbers for price and quantity.");
        } catch (SQLException e) {
            showError("Product Addition Failed", "An error occurred while adding the product. Please try again.");
        }
    }

    @FXML
    public void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imagePath = file.getAbsolutePath();
            imageLabel.setText("Selected Image: " + file.getName());
        } else {
            imageLabel.setText("No image selected");
        }
    }

    @FXML
    public void handleListProducts() {
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
            e.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.clear();
        priceField.clear();
        descriptionField.clear();
        categoryField.clear();
        locationField.clear();
        quantityField.clear();
        imageLabel.setText("No image selected");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
