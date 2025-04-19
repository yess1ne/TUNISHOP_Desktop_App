package controllers;

import entities.Checkout;
import entities.Products;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.CheckoutService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField secondNameField;
    @FXML private TextField emailField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField countryField;

    @FXML private VBox productsContainer;

    private List<CartController.CartItem> cartItems;
    private User user;

    public void setProducts(List<CartController.CartItem> items) {
        this.cartItems = items;
        displayProductSummary();
    }

    public void setProduct(Products product) {
        this.cartItems = new ArrayList<>();
        this.cartItems.add(new CartController.CartItem(product));
        displayProductSummary();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Init si nécessaire
    }

    private void displayProductSummary() {
        double total = 0;
        double shipping = 10.0;
        productsContainer.getChildren().clear();

        for (CartController.CartItem item : cartItems) {
            Products product = item.getProduct();

            HBox productBox = new HBox(10);
            productBox.setStyle("-fx-background-color: #f1f5f9; -fx-background-radius: 8; -fx-padding: 10;");
            productBox.setAlignment(Pos.CENTER_LEFT);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            File imageFile = new File(product.getImage());
            imageView.setImage(imageFile.exists()
                    ? new Image(imageFile.toURI().toString())
                    : new Image("file:default_image.png"));

            VBox infoBox = new VBox(5);
            Label title = new Label("Produit: " + product.getTitle());
            Label price = new Label("Prix: " + product.getPrice() + " TND");
            Label qty = new Label("Quantité: " + item.getQuantity());

            infoBox.getChildren().addAll(title, price, qty);
            productBox.getChildren().addAll(imageView, infoBox);
            productsContainer.getChildren().add(productBox);

            total += product.getPrice() * item.getQuantity();
        }

        Label totalLabel = new Label("Total TTC (incl. livraison 10.0 TND) : " + (total + 10.0) + " TND");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10;");
        productsContainer.getChildren().add(totalLabel);
    }

    @FXML
    private void handleConfirmCheckout() {
        if (!validateForm()) return;

        // ✅ Vérification critique pour éviter erreur user.getId()
        if (user == null || user.getId() == 0) {
            showError("Utilisateur non identifié. Veuillez vous reconnecter.");
            return;
        }

        try {
            CheckoutService service = new CheckoutService();
            long groupCheckoutId = System.currentTimeMillis();

            for (CartController.CartItem item : cartItems) {
                Checkout checkout = new Checkout(
                        groupCheckoutId,
                        firstNameField.getText(),
                        secondNameField.getText(),
                        emailField.getText(),
                        streetField.getText(),
                        cityField.getText(),
                        postalCodeField.getText(),
                        countryField.getText(),
                        user,
                        item.getProduct(),
                        "en attente"
                );
                service.ajouter(checkout);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Commande enregistrée");
            alert.setContentText("Merci pour votre achat !");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'enregistrer la commande : " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (firstNameField.getText().isEmpty()) return showError("Le prénom est requis.");
        if (secondNameField.getText().isEmpty()) return showError("Le nom est requis.");
        if (emailField.getText().isEmpty() || !isValidEmail(emailField.getText()))
            return showError("Veuillez entrer un email valide.");
        if (streetField.getText().isEmpty() || cityField.getText().isEmpty()
                || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty())
            return showError("Tous les champs d'adresse sont requis.");
        if (cartItems == null || cartItems.isEmpty())
            return showError("Le panier est vide.");
        return true;
    }

    private boolean showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return false;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    private void handleShowMyCheckouts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkoutList.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Mes Checkouts");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la liste des checkouts : " + e.getMessage());
        }
    }
}
