package controllers;

import entities.Checkout;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import services.CheckoutService;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCheckoutController {

    @FXML private TextField firstNameField;
    @FXML private TextField secondNameField;
    @FXML private TextField emailField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField countryField;
    @FXML private ComboBox<String> statusBox;

    private Checkout checkout;
    private ListView<Checkout> parentList;

    public void setCheckout(Checkout c) {
        this.checkout = c;

        firstNameField.setText(c.getFirstName());
        secondNameField.setText(c.getSecondName());
        emailField.setText(c.getEmail());
        streetField.setText(c.getStreet());
        cityField.setText(c.getCity());
        postalCodeField.setText(c.getPostalCode());
        countryField.setText(c.getCountry());

        statusBox.getItems().addAll("en attente", "confirmée", "expédiée", "livrée");
        statusBox.setValue(c.getStatus());
    }

    public void setParentList(ListView<Checkout> listView) {
        this.parentList = listView;
    }

    @FXML
    private void handleUpdate() {
        if (validateForm()) {
            try {
                checkout.setFirstName(firstNameField.getText());
                checkout.setSecondName(secondNameField.getText());
                checkout.setEmail(emailField.getText());
                checkout.setStreet(streetField.getText());
                checkout.setCity(cityField.getText());
                checkout.setPostalCode(postalCodeField.getText());
                checkout.setCountry(countryField.getText());
                checkout.setStatus(statusBox.getValue());

                new CheckoutService().modifier(checkout);

                // Refresh the list view
                if (parentList != null) {
                    parentList.refresh();
                }

                Alert alert = new Alert(AlertType.INFORMATION, "Commande mise à jour avec succès !");
                alert.showAndWait();

                // Close the window
                ((Stage) firstNameField.getScene().getWindow()).close();

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(AlertType.ERROR, "Échec de la mise à jour.").showAndWait();
            }
        }
    }

    private boolean validateForm() {
        // Check if any field is empty
        if (firstNameField.getText().isEmpty() || secondNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || streetField.getText().isEmpty() ||
                cityField.getText().isEmpty() || postalCodeField.getText().isEmpty() ||
                countryField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill all the fields.");
            return false;
        }

        // Check if the email format is valid
        if (!isValidEmail(emailField.getText())) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Regex for simple email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
