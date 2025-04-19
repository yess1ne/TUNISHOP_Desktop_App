package controllers;

import entities.Products;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartController {

    @FXML
    private VBox cartContainer;

    private static List<CartItem> cartItems = new ArrayList<>();
    private static double totalPrice = 0.0;

    private static HomeController homeController;
    private static User currentUser;

    public static void setHomeController(HomeController controller) {
        homeController = controller;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static void addProductToCart(Products product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().equals(product)) {
                item.incrementQuantity();
                totalPrice += product.getPrice();
                updateHomeCartCounter();
                return;
            }
        }

        CartItem newItem = new CartItem(product);
        cartItems.add(newItem);
        totalPrice += product.getPrice();
        updateHomeCartCounter();
    }

    @FXML
    public void initialize() {
        cartContainer.getChildren().clear();

        for (CartItem cartItem : cartItems) {
            VBox productCard = new VBox(10);
            productCard.setStyle("""
                    -fx-background-color: #ffffff;
                    -fx-padding: 15;
                    -fx-border-color: #d1d5db;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                    """);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            File imageFile = new File(cartItem.getProduct().getImage());
            imageView.setImage(imageFile.exists() ?
                    new Image(imageFile.toURI().toString()) :
                    new Image("file:default_image.png"));

            Label title = new Label(cartItem.getProduct().getTitle());
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111827;");

            Label price = new Label(cartItem.getProduct().getPrice() + " TND");
            price.setStyle("-fx-font-size: 14px; -fx-text-fill: #1f2937;");

            Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
            Button minusButton = new Button("-");
            Button plusButton = new Button("+");

            minusButton.setOnAction(e -> decreaseQuantity(cartItem, quantityLabel));
            plusButton.setOnAction(e -> increaseQuantity(cartItem, quantityLabel));

            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> removeProductFromCart(cartItem));

            productCard.getChildren().addAll(imageView, title, price, minusButton, quantityLabel, plusButton, removeButton);
            cartContainer.getChildren().add(productCard);
        }

        displayCartSummary();
    }

    private void removeProductFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        totalPrice -= cartItem.getProduct().getPrice() * cartItem.getQuantity();
        cartContainer.getChildren().clear();
        initialize();
        updateHomeCartCounter();
    }

    private void increaseQuantity(CartItem cartItem, Label quantityLabel) {
        cartItem.incrementQuantity();
        totalPrice += cartItem.getProduct().getPrice();
        quantityLabel.setText(String.valueOf(cartItem.getQuantity()));
        displayCartSummary();
        updateHomeCartCounter();
    }

    private void decreaseQuantity(CartItem cartItem, Label quantityLabel) {
        if (cartItem.getQuantity() > 1) {
            cartItem.decrementQuantity();
            totalPrice -= cartItem.getProduct().getPrice();
            quantityLabel.setText(String.valueOf(cartItem.getQuantity()));
            displayCartSummary();
            updateHomeCartCounter();
        }
    }

    private void displayCartSummary() {
        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-padding: 20;");

        Label summaryTitle = new Label("Cart Summary");
        summaryTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Label subtotalLabel = new Label("Subtotal: " + totalPrice + " TND");
        subtotalLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1f2937;");

        double shippingCost = 10.0;
        Label shippingLabel = new Label("Shipping: " + shippingCost + " TND");
        shippingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1f2937;");

        double totalAmount = totalPrice + shippingCost;
        Label totalLabel = new Label("Total: " + totalAmount + " TND");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Button proceedButton = new Button("Proceed to Checkout");
        proceedButton.setStyle("""
                -fx-background-color: #2563eb;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 20;
                -fx-background-radius: 5;
                """);

        proceedButton.setOnAction(e -> proceedToCheckout());
        summaryBox.getChildren().addAll(summaryTitle, subtotalLabel, shippingLabel, totalLabel, proceedButton);
        cartContainer.getChildren().add(summaryBox);
    }

    private static void updateHomeCartCounter() {
        if (homeController != null) {
            int totalItems = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
            homeController.updateCartCounter(totalItems);
        }
    }

    @FXML
    public void proceedToCheckout() {
        try {
            if (currentUser == null || currentUser.getId() == 0) {
                showError("Utilisateur non identifié. Veuillez vous reconnecter.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkout.fxml"));
            Parent root = loader.load();

            CheckoutController controller = loader.getController();
            controller.setProducts(cartItems);
            controller.setUser(currentUser);

            Stage stage = new Stage();
            stage.setTitle("Finaliser la commande");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLogout() {
        System.out.println("Logging out...");
    }

    public static class CartItem {
        private final Products product;
        private int quantity;

        public CartItem(Products product) {
            this.product = product;
            this.quantity = 1;
        }

        public Products getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void incrementQuantity() {
            quantity++;
        }

        public void decrementQuantity() {
            if (quantity > 1) quantity--;
        }
    }
}
