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
import services.PDFServiceCheckout;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CheckoutListController implements Initializable {

    @FXML
    private ListView<Long> checkoutListView;

    private Map<Long, List<Checkout>> groupedCheckouts = new HashMap<>();

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
            groupedCheckouts = list.stream().collect(Collectors.groupingBy(Checkout::getCheckoutId));
            checkoutListView.getItems().setAll(groupedCheckouts.keySet());

            checkoutListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Long checkoutId, boolean empty) {
                    super.updateItem(checkoutId, empty);
                    if (empty || checkoutId == null) {
                        setGraphic(null);
                        return;
                    }

                    List<Checkout> group = groupedCheckouts.get(checkoutId);
                    Checkout representative = group.get(0);

                    VBox card = new VBox(12);
                    card.setStyle("""
                        -fx-background-color: linear-gradient(to bottom right, #f0f4f8, #ffffff);
                        -fx-background-radius: 14;
                        -fx-border-color: #cbd5e1;
                        -fx-border-radius: 14;
                        -fx-padding: 20;
                        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0.3, 0, 4);
                    """);

                    Label name = new Label("👤 " + representative.getFirstName() + " " + representative.getSecondName());
                    name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

                    Label email = new Label("📧 " + representative.getEmail());
                    email.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

                    Label ville = new Label("📍 Ville: " + representative.getCity() + ", " + representative.getCountry());
                    ville.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

                    Label codePostal = new Label("🏠 Code Postal: " + representative.getPostalCode());
                    codePostal.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

                    VBox productsBox = new VBox(5);
                    for (Checkout c : group) {
                        String title = (c.getProduit() != null ? c.getProduit().getTitle() : "(supprimé)");
                        Label productInfo = new Label("📦 Produit : " + title);
                        productInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569;");
                        productsBox.getChildren().add(productInfo);
                    }

                    Label status = new Label("📅 Statut: " + representative.getStatus());
                    status.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

                    Button deleteBtn = new Button("🗑️ Supprimer");
                    deleteBtn.setStyle("""
                        -fx-background-color: #dc2626;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-font-size: 13px;
                        -fx-background-radius: 8;
                        -fx-padding: 8 16;
                    """);

                    deleteBtn.setOnAction(e -> {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmation de suppression");
                        confirm.setHeaderText("Supprimer cette commande ?");
                        confirm.setContentText("Cette action est irréversible.");
                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    for (Checkout c : group) {
                                        new CheckoutService().supprimer(c.getId());
                                    }
                                    refreshCheckoutList(TokenManager.decodeId());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    new Alert(Alert.AlertType.ERROR, "Échec de suppression").showAndWait();
                                }
                            }
                        });
                    });

                    Button updateBtn = new Button("✏️ Modifier");
                    updateBtn.setStyle("""
                        -fx-background-color: #2563eb;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-font-size: 13px;
                        -fx-background-radius: 8;
                        -fx-padding: 8 16;
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
                            controller.setCheckout(representative);

                            stage.setOnHidden(ev -> refreshCheckoutList(TokenManager.decodeId()));

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir la page de modification.").showAndWait();
                        }
                    });

                    Button pdfBtn = new Button("📄 Télécharger PDF");
                    pdfBtn.setStyle("""
                        -fx-background-color: #10b981;
                        -fx-text-fill: white;
                        -fx-font-weight: bold;
                        -fx-font-size: 13px;
                        -fx-background-radius: 8;
                        -fx-padding: 8 16;
                    """);

                    pdfBtn.setOnAction(e -> {
                        try {
                            PDFServiceCheckout.exportCommandeToPDF(group);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du PDF").showAndWait();
                        }
                    });

                    HBox buttons = new HBox(10, updateBtn, deleteBtn, pdfBtn);
                    card.getChildren().addAll(name, email, ville, codePostal, productsBox, status, buttons);
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
