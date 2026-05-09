package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.SuiviMedical;
import service.SuiviMedicalService;

import java.sql.Date;
import java.sql.SQLException;

public class SuiviMedicalController {

    @FXML private TextField medicamentIdField;
    @FXML private TextField nomPatientField;
    @FXML private TextField dateSuiviField;
    @FXML private TextField observationField;
    @FXML private TextField etatPatientField;

    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button supprimerBtn;
    @FXML private Button resetBtn;

    @FXML private TableView<SuiviMedical> suiviTable;

    @FXML private TableColumn<SuiviMedical, Integer> idColumn;
    @FXML private TableColumn<SuiviMedical, Integer> medicamentIdColumn;
    @FXML private TableColumn<SuiviMedical, String> nomPatientColumn;
    @FXML private TableColumn<SuiviMedical, Date> dateSuiviColumn;
    @FXML private TableColumn<SuiviMedical, String> observationColumn;
    @FXML private TableColumn<SuiviMedical, String> etatPatientColumn;

    private final SuiviMedicalService suiviService = new SuiviMedicalService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        medicamentIdColumn.setCellValueFactory(new PropertyValueFactory<>("medicamentId"));
        nomPatientColumn.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        dateSuiviColumn.setCellValueFactory(new PropertyValueFactory<>("dateSuivi"));
        observationColumn.setCellValueFactory(new PropertyValueFactory<>("observation"));
        etatPatientColumn.setCellValueFactory(new PropertyValueFactory<>("etatPatient"));

        chargerSuivis();

        suiviTable.setOnMouseClicked(event -> remplirChamps());
    }

    @FXML
    void ajouterSuivi() {

        try {

            SuiviMedical s = lireChamps();

            suiviService.ajouter(s);

            chargerSuivis();

            viderChamps();

            afficherInfo("Succès", "Suivi médical ajouté avec succès");

        } catch (SQLException e) {

            afficherErreur("Erreur SQL", e.getMessage());

        } catch (Exception e) {

            afficherErreur("Erreur", "Vérifie les champs et la date yyyy-mm-dd");
        }
    }

    @FXML
    void modifierSuivi() {

        SuiviMedical selected = suiviTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            afficherErreur("Erreur", "Sélectionne un suivi à modifier");

            return;
        }

        try {

            SuiviMedical s = lireChamps();

            s.setId(selected.getId());

            suiviService.modifier(s);

            chargerSuivis();

            viderChamps();

            afficherInfo("Succès", "Suivi modifié avec succès");

        } catch (SQLException e) {

            afficherErreur("Erreur SQL", e.getMessage());

        } catch (Exception e) {

            afficherErreur("Erreur", "Vérifie les champs et la date yyyy-mm-dd");
        }
    }

    @FXML
    void supprimerSuivi() {

        SuiviMedical selected = suiviTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            afficherErreur("Erreur", "Sélectionne un suivi à supprimer");

            return;
        }

        try {

            suiviService.supprimer(selected.getId());

            chargerSuivis();

            viderChamps();

            afficherInfo("Succès", "Suivi supprimé avec succès");

        } catch (SQLException e) {

            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    void resetForm() {

        viderChamps();

        suiviTable.getSelectionModel().clearSelection();
    }

    private SuiviMedical lireChamps() {

        SuiviMedical s = new SuiviMedical();

        s.setMedicamentId(Integer.parseInt(medicamentIdField.getText()));
        s.setNomPatient(nomPatientField.getText());
        s.setDateSuivi(Date.valueOf(dateSuiviField.getText()));
        s.setObservation(observationField.getText());
        s.setEtatPatient(etatPatientField.getText());

        return s;
    }

    private void chargerSuivis() {

        try {

            ObservableList<SuiviMedical> list =
                    FXCollections.observableArrayList(suiviService.recuperer());

            suiviTable.setItems(list);

        } catch (SQLException e) {

            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void remplirChamps() {

        SuiviMedical s = suiviTable.getSelectionModel().getSelectedItem();

        if (s != null) {

            medicamentIdField.setText(String.valueOf(s.getMedicamentId()));
            nomPatientField.setText(s.getNomPatient());
            dateSuiviField.setText(String.valueOf(s.getDateSuivi()));
            observationField.setText(s.getObservation());
            etatPatientField.setText(s.getEtatPatient());
        }
    }

    private void viderChamps() {

        medicamentIdField.clear();
        nomPatientField.clear();
        dateSuiviField.clear();
        observationField.clear();
        etatPatientField.clear();
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