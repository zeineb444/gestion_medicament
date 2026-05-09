package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Medicament;
import service.MedicamentService;

import java.sql.Date;
import java.sql.SQLException;

public class AjouterMedicamentController {

    @FXML private Button ajouterBtn;
    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField categorieField;
    @FXML private TextField dosageField;
    @FXML private TextField formeField;
    @FXML private TextField stockField;
    @FXML private TextField seuilField;
    @FXML private TextField lotField;
    @FXML private TextField quantiteLotField;
    @FXML private TextField dateField;
    @FXML private TextField serviceField;

    @FXML
    void ajouterMedicament(ActionEvent event) {
        ajouterMedicament();
    }

    public void ajouterMedicament() {
        try {
            Medicament medicament = new Medicament();

            medicament.setNom(nomField.getText());
            medicament.setDescription(descriptionField.getText());
            medicament.setCategorie(categorieField.getText());
            medicament.setDosage(dosageField.getText());
            medicament.setForme(formeField.getText());
            medicament.setQuantiteStock(Integer.parseInt(stockField.getText()));
            medicament.setSeuilMinimum(Integer.parseInt(seuilField.getText()));
            medicament.setNumeroLot(lotField.getText());
            medicament.setQuantiteLot(Integer.parseInt(quantiteLotField.getText()));
            medicament.setDateExpiration(Date.valueOf(dateField.getText()));
            medicament.setServiceNom(serviceField.getText());
            medicament.setEtatStock("OK");
            medicament.setAlerteExpiration("OK");

            MedicamentService medicamentService = new MedicamentService();
            medicamentService.ajouter(medicament);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setContentText("Médicament ajouté avec succès");
            alert.showAndWait();



        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Vérifie les champs : nombres et date yyyy-mm-dd");
            alert.showAndWait();
        }
    }
}