package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import services.ReclamationService;
import entities.Reclamation;

public class EditReclamationController {

    @FXML
    private TextField idField;

    @FXML
    private TextField raisonField;

    private ReclamationService service = new ReclamationService();

    private int reclamationId;

    public void loadReclamationById(int id) {
        this.reclamationId = id;
        Reclamation r = service.getReclamationById(id);
        if (r != null) {
            idField.setText(String.valueOf(r.getId()));
            raisonField.setText(r.getRaison());
        } else {
            new Alert(Alert.AlertType.ERROR, "Réclamation introuvable avec l'ID: " + id).show();
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            // Vérification que l'ID est un nombre
            String idText = idField.getText();
            if (!idText.matches("\\d+")) {
                new Alert(Alert.AlertType.ERROR, "L'ID doit être un nombre valide.").show();
                return;
            }
            int id = Integer.parseInt(idText);

            // Vérification que la raison ne contient pas de chiffres
            String newRaison = raisonField.getText();
            if (newRaison.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Veuillez remplir la raison.").show();
                return;
            }
            if (newRaison.matches(".*\\d.*")) { // Vérifie si la raison contient des chiffres
                new Alert(Alert.AlertType.ERROR, "La raison ne doit pas contenir de chiffres.").show();
                return;
            }

            // Modifier la réclamation
            service.modifierRaison(id, newRaison);

            new Alert(Alert.AlertType.INFORMATION, "Réclamation mise à jour avec succès !").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleDelete() {
        try {
            // Vérification que l'ID est un nombre
            String idText = idField.getText();
            if (!idText.matches("\\d+")) {
                new Alert(Alert.AlertType.ERROR, "L'ID doit être un nombre valide.").show();
                return;
            }
            int id = Integer.parseInt(idText);

            // Supprimer la réclamation
            service.supprimerReclamation(id);

            new Alert(Alert.AlertType.INFORMATION, "Réclamation supprimée avec succès !").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }
}
