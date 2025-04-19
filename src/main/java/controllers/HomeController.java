package controllers;

import entities.Products;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import midlleware.TokenManager;
import services.ProductService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private FlowPane productContainer;

    @FXML
    private ImageView appLogo;

    @FXML
    private ImageView cartIcon;
    @FXML
    private Label cartCountLabel;

    private final ProductService productService = new ProductService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAppLogo();
        loadProducts();
        setCartIcon();
        cartIcon.setOnMouseClicked(e -> openCartPage());
    }

    private void loadAppLogo() {
        try {
            Image logo = new Image("/TuniShop_Logo.jpg");
            appLogo.setImage(logo);
        } catch (Exception e) {
            System.out.println("Logo introuvable : " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            List<Products> products = productService.afficher();

            for (Products product : products) {
                VBox productCard = new VBox(10);
                productCard.setPrefWidth(240);
                productCard.setAlignment(Pos.CENTER);
                productCard.setStyle("""
                        -fx-background-color: #ffffff;
                        -fx-padding: 15;
                        -fx-border-color: #d1d5db;
                        -fx-border-radius: 10;
                        -fx-background-radius: 10;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 3);
                        """);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(200);
                imageView.setFitHeight(160);
                imageView.setPreserveRatio(true);

                File imageFile = new File(product.getImage());
                if (imageFile.exists()) {
                    imageView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    imageView.setImage(new Image("file:default_image.png"));
                }

                Label title = new Label(product.getTitle());
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111827;");

                Label price = new Label(product.getPrice() + " TND");
                price.setStyle("-fx-font-size: 14px; -fx-text-fill: #1f2937;");

                Button buyButton = new Button("Acheter");
                buyButton.setStyle("""
                        -fx-background-color: #2563eb;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-padding: 8 20;
                        -fx-background-radius: 5;
                        """);

                buyButton.setOnAction(e -> openCheckout(product));

                Button addToCartButton = new Button("Add to Cart");
                addToCartButton.setStyle("""
                        -fx-background-color: #34D399;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-padding: 8 20;
                        -fx-background-radius: 5;
                        """);

                addToCartButton.setOnAction(e -> addToCart(product));

                productCard.getChildren().addAll(imageView, title, price, buyButton, addToCartButton);
                productContainer.getChildren().add(productCard);
            }

        } catch (Exception e) {
            showError("Erreur lors du chargement des produits : " + e.getMessage());
        }
    }

    public void updateCartCounter(int count) {
        cartCountLabel.setText(String.valueOf(count));
    }

    private void addToCart(Products product) {
        CartController.addProductToCart(product);
        int totalItems = CartController.getCartItems().stream()
                .mapToInt(CartController.CartItem::getQuantity)
                .sum();
        updateCartCounter(totalItems);
    }

    private void openCheckout(Products product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkout.fxml"));
            Parent root = loader.load();

            CheckoutController controller = loader.getController();
            User user = new User();
            user.setId(TokenManager.decodeId());
            controller.setUser(user);
            controller.setProduct(product);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Finaliser la commande");
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du checkout : " + e.getMessage());
        }
    }

    private void setCartIcon() {
        cartIcon.setImage(new Image("/cart icon.png"));
    }

    private void openCartPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cart.fxml"));
            Parent root = loader.load();

            CartController.setHomeController(this);

            // ✅ Injecter l'utilisateur connecté dans le panier
            User user = new User();
            user.setId(TokenManager.decodeId());
            CartController.setCurrentUser(user); // ✅ IMPORTANT

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Shopping Cart");
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture du panier : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Échec de l’opération");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        TokenManager.clearToken();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) productContainer.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
