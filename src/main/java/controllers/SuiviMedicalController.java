package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.SuiviMedical;
import service.SuiviMedicalService;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
public class SuiviMedicalController {

    private static String patientDepuisRappel = null;

    public static void setPatientDepuisRappel(String patient) {
        patientDepuisRappel = patient;
    }

    @FXML private ComboBox<String> medicamentBox;
    @FXML private TextField nomPatientField;
    @FXML private TextField dateSuiviField;
    @FXML private TextField observationField;
    @FXML private ComboBox<String> etatPatientField;

    @FXML private TextField recherchePatientField;
    @FXML private ComboBox<String> rechercheEtatField;

    @FXML private Label ameliorationLabel;
    @FXML private Label stableLabel;
    @FXML private Label critiqueLabel;

    @FXML private ComboBox<String> historiquePatientCombo;
    @FXML private VBox historiqueBox;

    @FXML private TableView<SuiviMedical> suiviTable;
    @FXML private TableColumn<SuiviMedical, Integer> idColumn;
    @FXML private TableColumn<SuiviMedical, String> medicamentNomColumn;
    @FXML private TableColumn<SuiviMedical, String> nomPatientColumn;
    @FXML private TableColumn<SuiviMedical, Date> dateSuiviColumn;
    @FXML private TableColumn<SuiviMedical, String> observationColumn;
    @FXML private TableColumn<SuiviMedical, String> etatPatientColumn;

    private final SuiviMedicalService suiviService = new SuiviMedicalService();

    private final Connection connection =
            MyDataBase.getInstance().getConnection();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        medicamentNomColumn.setCellValueFactory(new PropertyValueFactory<>("medicamentNom"));
        nomPatientColumn.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        dateSuiviColumn.setCellValueFactory(new PropertyValueFactory<>("dateSuivi"));
        observationColumn.setCellValueFactory(new PropertyValueFactory<>("observation"));
        etatPatientColumn.setCellValueFactory(new PropertyValueFactory<>("etatPatient"));

        etatPatientField.getItems().addAll(
                "AMELIORATION",
                "STABLE",
                "CRITIQUE"
        );

        rechercheEtatField.getItems().addAll(
                "Tous les états",
                "AMELIORATION",
                "STABLE",
                "CRITIQUE"
        );

        rechercheEtatField.setValue("Tous les états");

        chargerMedicamentsCombo();
        chargerSuivis();

        if (patientDepuisRappel != null) {
            nomPatientField.setText(patientDepuisRappel);
            patientDepuisRappel = null;
        }

        suiviTable.setOnMouseClicked(event -> remplirChamps());
        historiquePatientCombo.setOnAction(event -> afficherHistoriquePatient());
    }

    private void chargerMedicamentsCombo() {
        medicamentBox.getItems().clear();

        try {
            String sql = "SELECT nom FROM medicaments";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                medicamentBox.getItems().add(rs.getString("nom"));
            }

        } catch (Exception e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void chargerSuivis() {
        try {
            ObservableList<SuiviMedical> list =
                    FXCollections.observableArrayList(suiviService.recuperer());

            suiviTable.setItems(list);

            mettreAJourStatistiques();
            chargerPatientsHistorique();

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void ajouterSuivi() {
        try {
            SuiviMedical s = lireChamps();
            suiviService.ajouter(s);

            chargerSuivis();
            resetForm();

        } catch (Exception e) {
            afficherErreur("Erreur ajout", e.getMessage());
        }
    }

    @FXML
    private void modifierSuivi() {
        try {
            SuiviMedical selected = suiviTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un suivi.");
                return;
            }

            SuiviMedical s = lireChamps();
            s.setId(selected.getId());

            suiviService.modifier(s);

            chargerSuivis();
            resetForm();

        } catch (Exception e) {
            afficherErreur("Erreur modification", e.getMessage());
        }
    }

    @FXML
    private void supprimerSuivi() {
        try {
            SuiviMedical selected = suiviTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un suivi.");
                return;
            }

            suiviService.supprimer(selected.getId());

            chargerSuivis();
            resetForm();

        } catch (Exception e) {
            afficherErreur("Erreur suppression", e.getMessage());
        }
    }

    @FXML
    private void resetForm() {
        medicamentBox.setValue(null);
        nomPatientField.clear();
        dateSuiviField.clear();
        observationField.clear();
        etatPatientField.setValue(null);

        suiviTable.getSelectionModel().clearSelection();
    }

    private SuiviMedical lireChamps() throws SQLException {
        SuiviMedical s = new SuiviMedical();

        String nomMedicament = medicamentBox.getValue();

        if (nomMedicament == null || nomMedicament.isEmpty()) {
            throw new SQLException("Veuillez choisir un médicament.");
        }

        int medicamentId = getMedicamentIdByNom(nomMedicament);

        s.setMedicamentId(medicamentId);
        s.setMedicamentNom(nomMedicament);
        s.setNomPatient(nomPatientField.getText());
        s.setDateSuivi(Date.valueOf(dateSuiviField.getText()));
        s.setObservation(observationField.getText());
        s.setEtatPatient(etatPatientField.getValue());

        return s;
    }

    private int getMedicamentIdByNom(String nomMedicament) throws SQLException {
        String sql = "SELECT id FROM medicaments WHERE nom = '" + nomMedicament + "' LIMIT 1";

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            return rs.getInt("id");
        }

        throw new SQLException("Médicament introuvable.");
    }

    private String getMedicamentNomById(int id) {
        try {
            String sql = "SELECT nom FROM medicaments WHERE id = " + id;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("nom");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private void remplirChamps() {
        SuiviMedical s = suiviTable.getSelectionModel().getSelectedItem();

        if (s != null) {
            medicamentBox.setValue(s.getMedicamentNom());
            nomPatientField.setText(s.getNomPatient());
            dateSuiviField.setText(String.valueOf(s.getDateSuivi()));
            observationField.setText(s.getObservation());
            etatPatientField.setValue(s.getEtatPatient());
        }
    }

    @FXML
    private void rechercherSuivis() {
        try {
            String patient = recherchePatientField.getText();
            String etat = rechercheEtatField.getValue();

            ObservableList<SuiviMedical> list = FXCollections.observableArrayList();

            for (SuiviMedical s : suiviService.recuperer()) {

                boolean matchPatient = patient == null
                        || patient.isEmpty()
                        || s.getNomPatient().toLowerCase().contains(patient.toLowerCase());

                boolean matchEtat = etat == null
                        || etat.equals("Tous les états")
                        || s.getEtatPatient().equals(etat);

                if (matchPatient && matchEtat) {
                    list.add(s);
                }
            }

            suiviTable.setItems(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void afficherTousSuivis() {
        recherchePatientField.clear();
        rechercheEtatField.setValue("Tous les états");
        chargerSuivis();
    }

    private void mettreAJourStatistiques() {
        try {
            int amelioration = 0;
            int stable = 0;
            int critique = 0;

            for (SuiviMedical s : suiviService.recuperer()) {
                if ("AMELIORATION".equals(s.getEtatPatient())) {
                    amelioration++;
                } else if ("STABLE".equals(s.getEtatPatient())) {
                    stable++;
                } else if ("CRITIQUE".equals(s.getEtatPatient())) {
                    critique++;
                }
            }

            ameliorationLabel.setText(String.valueOf(amelioration));
            stableLabel.setText(String.valueOf(stable));
            critiqueLabel.setText(String.valueOf(critique));

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void chargerPatientsHistorique() {
        historiquePatientCombo.getItems().clear();

        try {
            for (SuiviMedical s : suiviService.recuperer()) {
                if (!historiquePatientCombo.getItems().contains(s.getNomPatient())) {
                    historiquePatientCombo.getItems().add(s.getNomPatient());
                }
            }

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void afficherHistoriquePatient() {
        historiqueBox.getChildren().clear();

        String patient = historiquePatientCombo.getValue();

        if (patient == null) {
            Label vide = new Label("Sélectionnez un patient pour voir son historique.");
            vide.setStyle("-fx-font-size:15; -fx-text-fill:#7A5535;");
            historiqueBox.getChildren().add(vide);
            return;
        }

        try {
            int count = 0;

            for (SuiviMedical s : suiviService.recuperer()) {
                if (patient.equals(s.getNomPatient())) {
                    count++;

                    VBox card = new VBox(6);
                    card.setPrefWidth(1020);
                    card.setStyle("-fx-background-color:#fffaf5;" +
                            "-fx-background-radius:12;" +
                            "-fx-border-color:#D9B99A;" +
                            "-fx-border-radius:12;" +
                            "-fx-padding:12;");

                    Label ligne1 = new Label("📅 " + s.getDateSuivi()
                            + "    💊 " + s.getMedicamentNom()
                            + "                         " + s.getEtatPatient());

                    ligne1.setStyle("-fx-font-size:15;" +
                            "-fx-font-weight:bold;" +
                            "-fx-text-fill:#7A5535;");

                    Label observation = new Label(s.getObservation());
                    observation.setStyle("-fx-font-size:15; -fx-text-fill:#3B1F0E;");

                    card.getChildren().addAll(ligne1, observation);
                    historiqueBox.getChildren().add(card);
                }
            }

            if (count == 0) {
                Label vide = new Label("Aucun historique pour ce patient.");
                vide.setStyle("-fx-font-size:15; -fx-text-fill:#7A5535;");
                historiqueBox.getChildren().add(vide);
            }

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}