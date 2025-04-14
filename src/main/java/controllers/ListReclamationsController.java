package controllers;

import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ReclamationService;

import java.io.IOException;
import java.util.List;

public class ListReclamationsController {

    @FXML private TableView<Reclamation> tableView;
    @FXML private TableColumn<Reclamation, Integer> colId;
    @FXML private TableColumn<Reclamation, Integer> colCommande;
    @FXML private TableColumn<Reclamation, Integer> colProduit;
    @FXML private TableColumn<Reclamation, String> colRaison;
    @FXML private TableColumn<Reclamation, String> colDate;
    @FXML private TableColumn<Reclamation, Void> colAction; // Nouvelle colonne

    private final ReclamationService service = new ReclamationService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colCommande.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdCommande()).asObject());
        colProduit.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdProduit()).asObject());
        colRaison.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRaison()));
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));

        loadReclamations();
        addActionButtonsToTable();
    }

    public void loadReclamations() {
        List<Reclamation> list = service.getAll();
        ObservableList<Reclamation> observableList = FXCollections.observableArrayList(list);
        tableView.setItems(observableList);
    }

    private void addActionButtonsToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                modifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                modifyButton.setOnAction(event -> {
                    Reclamation selected = getTableView().getItems().get(getIndex());
                    openEditWindow(selected.getId());
                });

                deleteButton.setOnAction(event -> {
                    Reclamation selected = getTableView().getItems().get(getIndex());
                    service.supprimerReclamation(selected.getId());
                    loadReclamations(); // Rafraîchir après suppression
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, modifyButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void openEditWindow(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editReclamation.fxml"));
            Parent root = loader.load();

            EditReclamationController controller = loader.getController();
            controller.loadReclamationById(id); // méthode à créer dans le contrôleur

            Stage stage = new Stage();
            stage.setTitle("Modifier Réclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }
}
