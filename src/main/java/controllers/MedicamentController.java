package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Medicament;
import service.MedicamentService;

import java.sql.Date;
import java.sql.SQLException;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MedicamentController {

    @FXML private TextField rechercheNomField;
    @FXML private ComboBox<String> rechercheCategorieField;
    @FXML private ComboBox<String> rechercheServiceField;

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

    @FXML private Label totalMedLabel;
    @FXML private Label okMedLabel;
    @FXML private Label faibleMedLabel;
    @FXML private Label expirationMedLabel;

    @FXML private VBox stockBarsBox;

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
    @FXML private TableColumn<Medicament, String> etatColumn;
    @FXML private TableColumn<Medicament, String> alerteColumn;


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
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etatStock"));
        alerteColumn.setCellValueFactory(new PropertyValueFactory<>("alerteExpiration"));

        rechercheCategorieField.getItems().addAll(
                "", "Analgesique", "Infection", "Anti-inflammatoire", "Autre"
        );

        rechercheServiceField.getItems().addAll(
                "", "Urgence", "Pédiatrie", "Chirurgie", "Cardiologie"
        );

        chargerMedicaments();

        medicamentTable.setOnMouseClicked(event -> remplirChamps());
    }

    @FXML
    private void ajouterMedicament() {
        try {
            Medicament m = lireChamps();
            medicamentService.ajouter(m);
            chargerMedicaments();
            resetForm();
        } catch (Exception e) {
            afficherErreur("Erreur ajout", e.getMessage());
        }
    }

    @FXML
    private void modifierMedicament() {
        try {
            Medicament selected = medicamentTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un médicament.");
                return;
            }

            Medicament m = lireChamps();
            m.setId(selected.getId());

            medicamentService.modifier(m);
            chargerMedicaments();
            resetForm();

        } catch (Exception e) {
            afficherErreur("Erreur modification", e.getMessage());
        }
    }

    @FXML
    private void supprimerMedicament() {
        try {
            Medicament selected = medicamentTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                afficherErreur("Erreur", "Veuillez sélectionner un médicament.");
                return;
            }

            medicamentService.supprimer(selected.getId());
            chargerMedicaments();
            resetForm();

        } catch (Exception e) {
            afficherErreur("Erreur suppression", e.getMessage());
        }
    }

    @FXML
    private void resetForm() {
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

        if (m.getQuantiteStock() <= m.getSeuilMinimum()) {
            m.setEtatStock("FAIBLE");
        } else {
            m.setEtatStock("OK");
        }

        Date aujourdHui = new Date(System.currentTimeMillis());
        long difference = m.getDateExpiration().getTime() - aujourdHui.getTime();
        long jours = difference / (1000 * 60 * 60 * 24);

        if (jours < 0) {
            m.setAlerteExpiration("EXPIRE");
        } else if (jours <= 30) {
            m.setAlerteExpiration("BIENTOT");
        } else {
            m.setAlerteExpiration("OK");
        }

        return m;
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

    @FXML
    private void rechercherMedicaments() {
        try {
            String nom = rechercheNomField.getText();

            String categorie = rechercheCategorieField.getValue();
            String service = rechercheServiceField.getValue();

            if (categorie == null) categorie = "";
            if (service == null) service = "";

            ObservableList<Medicament> list =
                    FXCollections.observableArrayList(
                            medicamentService.rechercher(nom, categorie, service)
                    );

            medicamentTable.setItems(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void afficherTousMedicaments() {
        rechercheNomField.clear();
        rechercheCategorieField.setValue(null);
        rechercheServiceField.setValue(null);
        chargerMedicaments();
    }

    @FXML
    private void filtrerStockFaible() {
        try {
            ObservableList<Medicament> list = FXCollections.observableArrayList();

            for (Medicament m : medicamentService.recuperer()) {
                if ("FAIBLE".equals(m.getEtatStock()) || "RUPTURE".equals(m.getEtatStock())) {
                    list.add(m);
                }
            }

            medicamentTable.setItems(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    @FXML
    private void filtrerExpiration() {
        try {
            ObservableList<Medicament> list = FXCollections.observableArrayList();

            for (Medicament m : medicamentService.recuperer()) {
                if ("BIENTOT".equals(m.getAlerteExpiration()) || "EXPIRE".equals(m.getAlerteExpiration())) {
                    list.add(m);
                }
            }

            medicamentTable.setItems(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void mettreAJourStatistiques() {
        try {
            int total = medicamentService.recuperer().size();
            int ok = 0;
            int faible = 0;
            int expiration = 0;

            for (Medicament m : medicamentService.recuperer()) {
                if ("OK".equals(m.getEtatStock())) {
                    ok++;
                }

                if ("FAIBLE".equals(m.getEtatStock()) || "RUPTURE".equals(m.getEtatStock())) {
                    faible++;
                }

                if ("BIENTOT".equals(m.getAlerteExpiration()) || "EXPIRE".equals(m.getAlerteExpiration())) {
                    expiration++;
                }
            }

            totalMedLabel.setText(String.valueOf(total));
            okMedLabel.setText(String.valueOf(ok));
            faibleMedLabel.setText(String.valueOf(faible));
            expirationMedLabel.setText(String.valueOf(expiration));

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void mettreAJourBarresStock() {
        stockBarsBox.getChildren().clear();

        try {
            int count = 0;
            java.util.HashSet<String> nomsAffiches = new java.util.HashSet<>();

            for (Medicament m : medicamentService.recuperer()) {

                if (nomsAffiches.contains(m.getNom())) {
                    continue;
                }

                nomsAffiches.add(m.getNom());

                if (count >= 4) {
                    break;
                }

                VBox container = new VBox(4);
                container.setPrefWidth(1120);

                HBox top = new HBox();

                Label nom = new Label(m.getNom());
                nom.setStyle("-fx-text-fill:#7A5535;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label valeur = new Label(m.getQuantiteStock() + " / min " + m.getSeuilMinimum());
                valeur.setStyle("-fx-text-fill:#7A5535;");

                top.getChildren().addAll(nom, spacer, valeur);

                double progress;
                if (m.getQuantiteStock() > m.getSeuilMinimum()) {
                    progress = 1.0;
                } else {
                    progress = (double) m.getQuantiteStock() / Math.max(m.getSeuilMinimum(), 1);
                }

                double width = 1120 * progress;

                Pane background = new Pane();
                background.setPrefWidth(1120);
                background.setPrefHeight(8);
                background.setStyle("-fx-background-color:#EFE3D6; -fx-background-radius:10;");

                Pane fill = new Pane();
                fill.setPrefWidth(width);
                fill.setPrefHeight(8);

                if ("BIENTOT".equals(m.getAlerteExpiration()) || "EXPIRE".equals(m.getAlerteExpiration())) {
                    fill.setStyle("-fx-background-color:#C0392B; -fx-background-radius:10;");
                } else if ("FAIBLE".equals(m.getEtatStock()) || "RUPTURE".equals(m.getEtatStock())) {
                    fill.setStyle("-fx-background-color:#A07850; -fx-background-radius:10;");
                } else {
                    fill.setStyle("-fx-background-color:#2E7D32; -fx-background-radius:10;");
                }

                StackPane bar = new StackPane();
                bar.setPrefWidth(1120);
                bar.setPrefHeight(8);
                bar.getChildren().addAll(background, fill);
                StackPane.setAlignment(fill, Pos.CENTER_LEFT);

                container.getChildren().addAll(top, bar);
                stockBarsBox.getChildren().add(container);

                count++;
            }

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }
    private void chargerMedicaments() {

        try {

            ObservableList<Medicament> list =
                    FXCollections.observableArrayList(
                            medicamentService.recuperer()
                    );

            medicamentTable.setItems(list);

            mettreAJourStatistiques();

            mettreAJourBarresStock();

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