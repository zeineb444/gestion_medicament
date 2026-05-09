package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Medicament;
import service.MedicamentService;

import java.sql.Date;
import java.sql.SQLException;

public class MedicamentController {

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

    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button resetBtn;

    @FXML private TableView<Medicament> medicamentTable;

    @FXML private TableColumn<Medicament, Integer> idColumn;
    @FXML private TableColumn<Medicament, String> nomColumn;
    @FXML private TableColumn<Medicament, String> categorieColumn;
    @FXML private TableColumn<Medicament, String> dosageColumn;
    @FXML private TableColumn<Medicament, String> formeColumn;
    @FXML private TableColumn<Medicament, Integer> stockColumn;
    @FXML private TableColumn<Medicament, Integer> seuilColumn;
    @FXML private TableColumn<Medicament, String> lotColumn;
    @FXML private TableColumn<Medicament, Integer> quantiteLotColumn;
    @FXML private TableColumn<Medicament, Date> dateColumn;
    @FXML private TableColumn<Medicament, String> serviceColumn;

    private final MedicamentService medicamentService = new MedicamentService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        formeColumn.setCellValueFactory(new PropertyValueFactory<>("forme"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        seuilColumn.setCellValueFactory(new PropertyValueFactory<>("seuilMinimum"));
        lotColumn.setCellValueFactory(new PropertyValueFactory<>("numeroLot"));
        quantiteLotColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteLot"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceNom"));

        chargerMedicaments();

        medicamentTable.setOnMouseClicked(event -> remplirChamps());
    }

    @FXML
    void ajouterMedicament() {
        try {
            Medicament m = lireChamps();

            medicamentService.ajouter(m);
            chargerMedicaments();
            viderChamps();

            afficherInfo("Succès", "Médicament ajouté avec succès");

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());

        } catch (Exception e) {
            afficherErreur("Erreur", "Vérifie les champs : nombres et date yyyy-mm-dd");
        }
    }

    @FXML
    void modifierMedicament() {
        Medicament selected = medicamentTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            afficherErreur("Erreur", "Sélectionne un médicament à modifier");
            return;
        }

        try {
            Medicament m = lireChamps();
            m.setId(selected.getId());

            medicamentService.modifier(m);
            chargerMedicaments();
            viderChamps();

            afficherInfo("Succès", "Médicament modifié avec succès");

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());

        } catch (Exception e) {
            afficherErreur("Erreur", "Vérifie les champs : nombres et date yyyy-mm-dd");
        }
    }

    @FXML
    void supprimerMedicament() {
        Medicament selected = medicamentTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            afficherErreur("Erreur", "Sélectionne un médicament à supprimer");
            return;
        }

        try {
            medicamentService.supprimer(selected.getId());
            chargerMedicaments();
            viderChamps();

            afficherInfo("Succès", "Médicament supprimé avec succès");

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    void resetForm() {
        viderChamps();
        medicamentTable.getSelectionModel().clearSelection();
    }

    private Medicament lireChamps() {
        Medicament m = new Medicament();

        m.setNom(nomField.getText());
        m.setDescription(descriptionField.getText());
        m.setCategorie(categorieField.getText());
        m.setDosage(dosageField.getText());
        m.setForme(formeField.getText());
        m.setQuantiteStock(Integer.parseInt(stockField.getText()));
        m.setSeuilMinimum(Integer.parseInt(seuilField.getText()));
        m.setNumeroLot(lotField.getText());
        m.setQuantiteLot(Integer.parseInt(quantiteLotField.getText()));
        m.setDateExpiration(Date.valueOf(dateField.getText()));
        m.setServiceNom(serviceField.getText());

        m.setEtatStock("OK");
        m.setAlerteExpiration("OK");

        return m;
    }

    private void chargerMedicaments() {
        try {
            ObservableList<Medicament> list =
                    FXCollections.observableArrayList(medicamentService.recuperer());

            medicamentTable.setItems(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void remplirChamps() {
        Medicament m = medicamentTable.getSelectionModel().getSelectedItem();

        if (m != null) {
            nomField.setText(m.getNom());
            descriptionField.setText(m.getDescription());
            categorieField.setText(m.getCategorie());
            dosageField.setText(m.getDosage());
            formeField.setText(m.getForme());
            stockField.setText(String.valueOf(m.getQuantiteStock()));
            seuilField.setText(String.valueOf(m.getSeuilMinimum()));
            lotField.setText(m.getNumeroLot());
            quantiteLotField.setText(String.valueOf(m.getQuantiteLot()));
            dateField.setText(String.valueOf(m.getDateExpiration()));
            serviceField.setText(m.getServiceNom());
        }
    }

    private void viderChamps() {
        nomField.clear();
        descriptionField.clear();
        categorieField.clear();
        dosageField.clear();
        formeField.clear();
        stockField.clear();
        seuilField.clear();
        lotField.clear();
        quantiteLotField.clear();
        dateField.clear();
        serviceField.clear();
    }

    private void afficherInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }
}