package controllers;

import entities.Remboursement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.RemboursementService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class AddRemboursementController {

    @FXML
    private ComboBox<Integer> idReclamationComboBox;

    @FXML
    private TextField montantField;

    @FXML
    private TextField modeField;

    @FXML
    private DatePicker datePicker;

    private final RemboursementService remboursementService = new RemboursementService();


    @FXML
    public void initialize() {
        ObservableList<Integer> reclamationIds = FXCollections.observableArrayList();

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunishop_db", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM reclamation");

            while (resultSet.next()) {
                reclamationIds.add(resultSet.getInt("id"));
            }

            idReclamationComboBox.setItems(reclamationIds);

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les réclamations : " + e.getMessage());
        }
    }

    @FXML
    public void handleAddRemboursement(ActionEvent event) {
        try {
            Integer idReclamation = idReclamationComboBox.getValue();
            if (idReclamation == null) {
                showAlert("Erreur", "Veuillez sélectionner une réclamation.");
                return;
            }

            String montantText = montantField.getText();
            String mode = modeField.getText();
            LocalDate date = datePicker.getValue();

            if (montantText.isEmpty() || mode.isEmpty() || date == null) {
                showAlert("Validation Error", "Veuillez remplir tous les champs.");
                return;
            }

            if (!montantText.matches("\\d{1,8}")) {
                showAlert("Erreur", "Le montant doit être un nombre et ne doit pas dépasser 8 chiffres.");
                return;
            }

            if (mode.matches(".*\\d.*")) {
                showAlert("Erreur", "Le mode ne doit pas contenir de chiffres.");
                return;
            }

            double montant = Double.parseDouble(montantText);

            Remboursement remboursement = new Remboursement(idReclamation, montant, mode, date);
            remboursementService.ajouterRemboursement(remboursement);

            showAlert("Succès", "Remboursement ajouté avec succès !");
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void handleModify(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editRemboursement.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Modifier Remboursement");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d’ouvrir la page de modification : " + e.getMessage());
        }
    }

    @FXML
    public void handleShow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listRemboursement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Remboursements");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture : " + e.getMessage());
        }
    }

    @FXML
    public void handleGoToAddReclamation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addReclamation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors du changement de page : " + e.getMessage());
        }
    }

    private void clearFields() {
        montantField.clear();
        modeField.clear();
        idReclamationComboBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
