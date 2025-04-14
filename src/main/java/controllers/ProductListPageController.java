package controllers;

import entities.Products;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.ProductService;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductListPageController {

    @FXML
    private ListView<Products> productListView;

    private final ProductService productService = new ProductService();

    public void initialize() {
        try {
            List<Products> productList = productService.afficher();
            ObservableList<Products> observableProductList = FXCollections.observableArrayList(productList);
            productListView.setItems(observableProductList);

            productListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Products product, boolean empty) {
                    super.updateItem(product, empty);

                    if (empty || product == null) {
                        setGraphic(null);
                        return;
                    }

                    // Image
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                    try {
                        File imageFile = new File(product.getImage());
                        if (imageFile.exists()) {
                            imageView.setImage(new Image(imageFile.toURI().toString()));
                        } else {
                            imageView.setImage(new Image("file:default_image.png"));
                        }
                    } catch (Exception e) {
                        imageView.setImage(new Image("file:default_image.png"));
                    }

                    // Informations
                    VBox infoBox = new VBox(5);
                    infoBox.getChildren().addAll(
                            new Label("Title: " + product.getTitle()),
                            new Label("Price: " + product.getPrice() + " DT"),
                            new Label("Category: " + product.getCategorie()),
                            new Label("Location: " + product.getLocation()),
                            new Label("Quantity: " + product.getQuantity())
                    );

                    // 🔁 Ajout du nom de l’utilisateur si disponible
                    if (product.getUtilisateur() != null) {
                        infoBox.getChildren().add(
                                new Label("Seller: " + product.getUtilisateur().getNom() + " " + product.getUtilisateur().getPrenom())
                        );
                    } else {
                        infoBox.getChildren().add(
                                new Label("Seller: Unknown")
                        );
                    }

                    // Boutons
                    Button btnDetails = new Button("Show Details");
                    btnDetails.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");

                    Button btnUpdate = new Button("Edit");
                    btnUpdate.setStyle("-fx-background-color: #5A78F0; -fx-text-fill: white;");
                    btnUpdate.setOnAction(e -> handleUpdateProduct(product));

                    Button btnDelete = new Button("Delete");
                    btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                    btnDelete.setOnAction(e -> handleDeleteProduct(product));

                    VBox actionsBox = new VBox(5, btnDetails, btnUpdate, btnDelete);
                    actionsBox.setAlignment(Pos.CENTER);

                    // Mise en page finale
                    HBox row = new HBox(20, imageView, infoBox, actionsBox);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(10));
                    row.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");

                    setGraphic(row);
                }
            });

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load products: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleUpdateProduct(Products product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateProductPage.fxml"));
            Scene scene = new Scene(loader.load());

            UpdateProductPageController updateController = loader.getController();
            updateController.setProduct(product);

            Stage stage = new Stage();
            stage.setTitle("Update Product");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) productListView.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            showAlert("Error", "Could not open the update form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleDeleteProduct(Products product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setHeaderText("Are you sure you want to delete this product?");
        confirm.setContentText("This action is irreversible.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    productService.supprimer(product.getId());
                    productListView.getItems().remove(product);
                    showAlert("Success", "Product deleted successfully.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    showAlert("Database Error", "Failed to delete product: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
