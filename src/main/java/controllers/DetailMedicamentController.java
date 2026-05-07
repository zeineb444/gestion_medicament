package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Medicament;

public class DetailMedicamentController {

    @FXML
    private TextField categorieField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField dosageField;

    @FXML
    private TextField formeField;

    @FXML
    private TextField lotField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField quantiteLotField;

    @FXML
    private TextField serviceField;

    @FXML
    private TextField seuilField;

    @FXML
    private TextField stockField;

    public void setMedicament(Medicament medicament) {

        nomField.setText(medicament.getNom());
        descriptionField.setText(medicament.getDescription());
        categorieField.setText(medicament.getCategorie());
        dosageField.setText(medicament.getDosage());
        formeField.setText(medicament.getForme());

        stockField.setText(String.valueOf(medicament.getQuantiteStock()));
        seuilField.setText(String.valueOf(medicament.getSeuilMinimum()));
        lotField.setText(medicament.getNumeroLot());
        quantiteLotField.setText(String.valueOf(medicament.getQuantiteLot()));
        dateField.setText(String.valueOf(medicament.getDateExpiration()));
        serviceField.setText(medicament.getServiceNom());
    }
}