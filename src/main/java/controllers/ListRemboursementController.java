package controllers;

import entities.Remboursement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.RemboursementService;

import java.io.IOException;
import java.util.List;

public class ListRemboursementController {

    @FXML private TableView<Remboursement> tableView;
    @FXML private TableColumn<Remboursement, Integer> colId;
    @FXML private TableColumn<Remboursement, Double> colMontant;
    @FXML private TableColumn<Remboursement, String> colMode;
    @FXML private TableColumn<Remboursement, String> colDate;
    @FXML private TableColumn<Remboursement, Integer> colIdReclamation;

    @FXML private TableColumn<Remboursement, Void> colAction;

    private final RemboursementService service = new RemboursementService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdRemboursement()).asObject());
        colIdReclamation.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colMontant.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMontant()).asObject());
        colMode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModeRemboursement()));
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDate().toString()));

        loadRemboursements();
        addActionButtonsToTable();
    }

    public void loadRemboursements() {
        List<Remboursement> list = service.getAll();
        ObservableList<Remboursement> observableList = FXCollections.observableArrayList(list);
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
                    Remboursement selected = getTableView().getItems().get(getIndex());
                    openEditWindow(selected.getIdRemboursement());
                });

                deleteButton.setOnAction(event -> {
                    Remboursement selected = getTableView().getItems().get(getIndex());
                    service.supprimerRemboursement(selected.getIdRemboursement());
                    loadRemboursements();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editRemboursement.fxml"));
            Parent root = loader.load();

            EditRemboursementController controller = loader.getController();
            controller.loadRemboursementById(id); // À implémenter dans EditRemboursementController

            Stage stage = new Stage();
            stage.setTitle("Modifier Remboursement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
        }
    }
}
