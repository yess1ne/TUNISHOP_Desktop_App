package controllers;

import entities.Checkout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import midlleware.TokenManager;
import services.CheckoutService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CheckoutListController implements Initializable {

    @FXML
    private ListView<Checkout> checkoutListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int userId = TokenManager.decodeId();
        if (userId != -1) {
            refreshCheckoutList(userId);
        } else {
            showAlert("Erreur", "Identifiant utilisateur invalide dans le token.");
        }
    }

    public void refreshCheckoutList(int userId) {
        try {
            List<Checkout> list = new CheckoutService().getByUserId(userId);
            checkoutListView.getItems().clear();
            checkoutListView.getItems().addAll(list);
            checkoutListView.refresh();

            checkoutListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Checkout c, boolean empty) {
                    super.updateItem(c, empty);
                    if (empty || c == null) {
                        setGraphic(null);
                        return;
                    }

                    VBox card = new VBox(10);
                    card.setStyle("""
                        -fx-background-color: white;
                        -fx-background-radius: 16;
                        -fx-border-radius: 16;
                        -fx-border-color: #d1d5db;
                        -fx-padding: 20;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.3, 0, 4);
                    """);

                    Label name = new Label("👤 " + c.getFirstName() + " " + c.getSecondName());
                    name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

                    Label email = new Label("✉️ " + c.getEmail());
                    email.setStyle("-fx-font-size: 16px; -fx-text-fill: #374151;");

                    Label userInfo = new Label("🧑 Utilisateur : " + (c.getUser() != null ? c.getUser().getNom() : "inconnu"));
                    userInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #4b5563;");

                    Label productInfo = new Label("🎁 Produit : " + (c.getProduit() != null ? c.getProduit().getTitle() : "(supprimé)"));
                    productInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #374151;");

                    Label ville = new Label("📍 Ville: " + c.getCity() + ", " + c.getCountry());
                    ville.setStyle("-fx-font-size: 16px; -fx-text-fill: #374151;");

                    Label codePostal = new Label("🏡 Code Postal: " + c.getPostalCode());
                    codePostal.setStyle("-fx-font-size: 16px; -fx-text-fill: #374151;");

                    Label status = new Label("⏱️ Statut: " + c.getStatus());
                    status.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");

                    Button deleteBtn = new Button("🗑️ Supprimer");
                    deleteBtn.setStyle("""
                        -fx-background-color: #ef4444;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-font-size: 14px;
                        -fx-background-radius: 8;
                        -fx-padding: 6 14;
                    """);

                    deleteBtn.setOnAction(e -> {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmation de suppression");
                        confirm.setHeaderText("Supprimer cette commande ?");
                        confirm.setContentText("Cette action est irréversible.");
                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    new CheckoutService().supprimer(c.getId());
                                    checkoutListView.getItems().remove(c);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    new Alert(Alert.AlertType.ERROR, "Échec de suppression").showAndWait();
                                }
                            }
                        });
                    });

                    Button updateBtn = new Button("✏️ Modifier");
                    updateBtn.setStyle("""
                        -fx-background-color: #3b82f6;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-font-size: 14px;
                        -fx-background-radius: 8;
                        -fx-padding: 6 14;
                    """);

                    updateBtn.setOnAction(e -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateCheckout.fxml"));
                            Scene scene = new Scene(loader.load());
                            Stage stage = new Stage();
                            stage.setTitle("Modifier Checkout");
                            stage.setScene(scene);
                            stage.show();

                            UpdateCheckoutController controller = loader.getController();
                            controller.setCheckout(c);
                            controller.setParentList(checkoutListView);

                            stage.setOnHidden(ev -> {
                                checkoutListView.refresh();
                                checkoutListView.getSelectionModel().select(c);
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la page de modification.").showAndWait();
                        }
                    });

                    HBox buttons = new HBox(10, updateBtn, deleteBtn);
                    card.getChildren().addAll(name, email, userInfo, productInfo, ville, codePostal, status, buttons);
                    setGraphic(card);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}