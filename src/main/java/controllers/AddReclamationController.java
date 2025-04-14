package controllers;

import entities.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;


import java.io.IOException;
import java.time.LocalDate;

public class AddReclamationController {

    @FXML
    private TextField commandeIdField;

    @FXML
    private TextField produitIdField;

    @FXML
    private TextField raisonField;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    void handleAdd() {
        try {

            String commandeIdText = commandeIdField.getText();
            if (!commandeIdText.matches("\\d+")) {
                showAlert("Erreur de validation", "L'ID de la commande doit être un nombre.");
                return;
            }
            int idCommande = Integer.parseInt(commandeIdText);


            String produitIdText = produitIdField.getText();
            if (!produitIdText.matches("\\d+")) {
                showAlert("Erreur de validation", "L'ID du produit doit être un nombre.");
                return;
            }
            int idProduit = Integer.parseInt(produitIdText);


            String raison = raisonField.getText();
            if (raison.isEmpty()) {
                showAlert("Erreur de validation", "Veuillez remplir la raison.");
                return;
            }
            if (raison.matches(".*\\d.*")) { // Vérifie si la raison contient des chiffres
                showAlert("Erreur de validation", "La raison ne doit pas contenir de chiffres.");
                return;
            }

            int userId = 1;


            Reclamation reclamation = new Reclamation(idCommande, idProduit, raison, LocalDate.now(), userId);
            reclamationService.ajouterReclamation(reclamation);


            showAlert("Succès", "Réclamation ajoutée avec succès !");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les IDs doivent être des nombres.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur : " + e.getMessage());
        }
    }

    @FXML
    void handleModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editReclamation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Modifier Réclamation");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d’ouvrir la page de modification : " + e.getMessage());
        }
    }

    @FXML
    void handleShow() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/listReclamation.fxml"));
            Parent root = fxmlLoader.load();


            Stage stage = new Stage();
            stage.setTitle("List of Reclamations");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ouverture : " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleGoToAddRemboursement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/addRemboursement.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void clearFields() {
        commandeIdField.clear();
        produitIdField.clear();
        raisonField.clear();
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
