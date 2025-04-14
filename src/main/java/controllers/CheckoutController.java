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
import javafx.stage.Stage;
import services.CheckoutService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CheckoutController implements Initializable {

    @FXML private TextField firstNameField;
    @FXML private TextField secondNameField;
    @FXML private TextField emailField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField countryField;

    @FXML private Label productTitle;
    @FXML private Label productPrice;
    @FXML private Label productCategory;
    @FXML private Label productLocation;
    @FXML private ImageView productImage;

    private Products selectedProduct;
    private User user;  // ✅ Objet User au lieu d’un simple ID

    public void setProduct(Products product) {
        this.selectedProduct = product;
        productTitle.setText("Produit: " + product.getTitle());
        productPrice.setText("Prix: " + product.getPrice() + " TND");
        productCategory.setText("Catégorie: " + product.getCategorie());
        productLocation.setText("Lieu: " + product.getLocation());

        File imageFile = new File(product.getImage());
        if (imageFile.exists()) {
            productImage.setImage(new Image(imageFile.toURI().toString()));
        } else {
            productImage.setImage(new Image("file:default_image.png"));
        }
    }

    // ✅ Nouvelle méthode pour passer l’objet complet
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize if needed
    }

    @FXML
    private void handleConfirmCheckout() {
        if (!validateForm()) return;

        try {
            Checkout checkout = new Checkout(
                    System.currentTimeMillis(),
                    firstNameField.getText(),
                    secondNameField.getText(),
                    emailField.getText(),
                    streetField.getText(),
                    cityField.getText(),
                    postalCodeField.getText(),
                    countryField.getText(),
                    user,  // ✅ on utilise l’objet User
                    selectedProduct,
                    "en attente"
            );

            CheckoutService service = new CheckoutService();
            service.ajouter(checkout);

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
        if (emailField.getText().isEmpty() || !isValidEmail(emailField.getText())) return showError("Veuillez entrer un email valide.");
        if (streetField.getText().isEmpty() || cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() || countryField.getText().isEmpty())
            return showError("Tous les champs d'adresse sont requis.");
        if (selectedProduct == null) return showError("Le produit est requis.");
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
