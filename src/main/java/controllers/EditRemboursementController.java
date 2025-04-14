package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import services.RemboursementService;
import entities.Remboursement;

public class EditRemboursementController {

    @FXML
    private TextField idField;

    @FXML
    private TextField montantField;

    @FXML
    private TextField modeRemboursementField;

    private RemboursementService service = new RemboursementService();

    private int remboursementId;

    // Méthode pour charger un remboursement par son ID
    public void loadRemboursementById(int id) {
        this.remboursementId = id;
        Remboursement r = service.getRemboursementById(id);
        if (r != null) {
            idField.setText(String.valueOf(r.getIdRemboursement()));
            montantField.setText(String.valueOf(r.getMontant()));
            modeRemboursementField.setText(r.getModeRemboursement());
        } else {
            new Alert(Alert.AlertType.ERROR, "Remboursement introuvable avec l'ID: " + id).show();
        }
    }

    // Méthode pour mettre à jour le remboursement
    @FXML
    private void handleUpdate() {
        try {
            // Vérification si les champs sont vides
            if (montantField.getText().isEmpty() || modeRemboursementField.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Tous les champs doivent être remplis !").show();
                return;
            }

            // Validation des données
            int id = Integer.parseInt(idField.getText());

            // Vérifier que le montant est un nombre et ne dépasse pas 8 chiffres
            String montantText = montantField.getText();
            if (!montantText.matches("\\d{1,8}")) {
                new Alert(Alert.AlertType.ERROR, "Le montant doit être un nombre et ne doit pas dépasser 8 chiffres.").show();
                return;
            }
            double newMontant = Double.parseDouble(montantText);

            // Vérifier que le mode ne contient pas de chiffres
            String newModeRemboursement = modeRemboursementField.getText();
            if (newModeRemboursement.matches(".*\\d.*")) {
                new Alert(Alert.AlertType.ERROR, "Le mode de remboursement ne doit pas contenir de chiffres.").show();
                return;
            }

            // Appel au service pour modifier le remboursement
            service.modifierRemboursement(id, newMontant, newModeRemboursement);

            new Alert(Alert.AlertType.INFORMATION, "Remboursement mis à jour avec succès !").show();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Format incorrect pour l'ID ou le Montant !").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }

    // Méthode pour supprimer le remboursement
    @FXML
    private void handleDelete() {
        try {
            int id = Integer.parseInt(idField.getText());
            service.supprimerRemboursement(id);

            new Alert(Alert.AlertType.INFORMATION, "Remboursement supprimé avec succès !").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }
}
