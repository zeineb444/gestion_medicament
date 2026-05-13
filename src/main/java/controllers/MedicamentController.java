package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.Medicament;
import service.MedicamentService;

import java.sql.Date;
import java.sql.SQLException;

public class MedicamentController {

    @FXML private TextField rechercheNomField;
    @FXML private ComboBox<String> rechercheCategorieField;
    @FXML private ComboBox<String> rechercheServiceField;

    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> categorieField;
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
    @FXML private TableColumn<Medicament, String> nomColumn;
    @FXML private TableColumn<Medicament, String> categorieColumn;
    @FXML private TableColumn<Medicament, String> dosageColumn;
    @FXML private TableColumn<Medicament, String> formeColumn;
    @FXML private TableColumn<Medicament, Integer> stockColumn;
    @FXML private TableColumn<Medicament, Integer> seuilColumn;
    @FXML private TableColumn<Medicament, Date> expirationColumn;
    @FXML private TableColumn<Medicament, String> serviceColumn;
    @FXML private TableColumn<Medicament, String> etatColumn;

    private final MedicamentService medicamentService = new MedicamentService();

    private static Integer medicamentIdASelectionner = null;

    public static void setMedicamentIdASelectionner(int id) {
        medicamentIdASelectionner = id;
    }

    @FXML
    public void initialize() {
        medicamentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        formeColumn.setCellValueFactory(new PropertyValueFactory<>("forme"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        seuilColumn.setCellValueFactory(new PropertyValueFactory<>("seuilMinimum"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("serviceNom"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etatStock"));

        categorieField.getItems().addAll(
                "Analgésique",
                "Antibiotique",
                "Anti-inflammatoire",
                "Infection",
                "Autre"
        );

        rechercheCategorieField.getItems().addAll(
                "",
                "Analgésique",
                "Antibiotique",
                "Anti-inflammatoire",
                "Infection",
                "Autre"
        );

        rechercheServiceField.getItems().addAll(
                "",
                "Urgence",
                "Pédiatrie",
                "Chirurgie",
                "Cardiologie"
        );

        chargerMedicaments();

        medicamentTable.setOnMouseClicked(event -> remplirChamps());

        Platform.runLater(() -> {
            if (medicamentIdASelectionner != null) {
                selectionnerMedicamentParId(medicamentIdASelectionner);
                medicamentIdASelectionner = null;
            }
        });
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
                afficherErreur("Erreur", "Sélectionnez un médicament.");
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
                afficherErreur("Erreur", "Sélectionnez un médicament.");
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
        categorieField.setValue(null);
        dosageField.clear();
        formeField.clear();
        stockField.clear();
        seuilField.clear();
        lotField.setText("LOT001");
        quantiteLotField.setText("0");
        dateField.clear();
        serviceField.clear();

        stockField.setStyle(
                "-fx-background-radius:20;" +
                        "-fx-border-radius:20;" +
                        "-fx-border-color:#D9B99A;"
        );

        medicamentTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void rechercherMedicaments() {
        try {
            String nom = rechercheNomField.getText();
            String categorie = rechercheCategorieField.getValue();
            String service = rechercheServiceField.getValue();

            if (nom == null) nom = "";
            if (categorie == null) categorie = "";
            if (service == null) service = "";

            ObservableList<Medicament> list =
                    FXCollections.observableArrayList(
                            medicamentService.rechercher(nom, categorie, service)
                    );

            medicamentTable.setItems(list);
            mettreAJourStatistiques(list);
            mettreAJourBarresStock(list);

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
    private void filtrerExpiration() {
        try {
            ObservableList<Medicament> list = FXCollections.observableArrayList();

            for (Medicament m : medicamentService.recuperer()) {
                if ("BIENTOT".equalsIgnoreCase(m.getAlerteExpiration()) ||
                        "EXPIRE".equalsIgnoreCase(m.getAlerteExpiration())) {
                    list.add(m);
                }
            }

            medicamentTable.setItems(list);
            mettreAJourStatistiques(list);
            mettreAJourBarresStock(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private void chargerMedicaments() {
        try {
            ObservableList<Medicament> list =
                    FXCollections.observableArrayList(medicamentService.recuperer());

            medicamentTable.setItems(list);
            mettreAJourStatistiques(list);
            mettreAJourBarresStock(list);

        } catch (SQLException e) {
            afficherErreur("Erreur SQL", e.getMessage());
        }
    }

    private Medicament lireChamps() {
        verifierChamps();

        Medicament m = new Medicament();

        m.setNom(nomField.getText().trim());
        m.setDescription(descriptionField.getText());
        m.setCategorie(categorieField.getValue());
        m.setDosage(dosageField.getText().trim());
        m.setForme(formeField.getText().trim());
        m.setQuantiteStock(Integer.parseInt(stockField.getText().trim()));
        m.setSeuilMinimum(Integer.parseInt(seuilField.getText().trim()));
        m.setNumeroLot(lotField.getText().trim());
        m.setQuantiteLot(Integer.parseInt(quantiteLotField.getText().trim()));
        m.setDateExpiration(Date.valueOf(dateField.getText().trim()));
        m.setServiceNom(serviceField.getText().trim());

        if (m.getQuantiteStock() == 0) {
            m.setEtatStock("RUPTURE");
        } else if (m.getQuantiteStock() <= m.getSeuilMinimum()) {
            m.setEtatStock("FAIBLE");
        } else {
            m.setEtatStock("OK");
        }

        Date aujourdHui = new Date(System.currentTimeMillis());
        long jours = (m.getDateExpiration().getTime() - aujourdHui.getTime()) / (1000 * 60 * 60 * 24);

        if (jours < 0) {
            m.setAlerteExpiration("EXPIRE");
        } else if (jours <= 30) {
            m.setAlerteExpiration("BIENTOT");
        } else {
            m.setAlerteExpiration("OK");
        }

        return m;
    }

    private void verifierChamps() {
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du médicament est obligatoire.");
        }

        if (stockField.getText() == null || stockField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le stock est obligatoire.");
        }

        if (seuilField.getText() == null || seuilField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le seuil minimum est obligatoire.");
        }

        if (dateField.getText() == null || dateField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("La date d'expiration est obligatoire. Format : yyyy-mm-dd");
        }

        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                throw new IllegalArgumentException("Le stock ne peut pas être négatif.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le stock doit être un nombre.");
        }

        try {
            int seuil = Integer.parseInt(seuilField.getText().trim());
            if (seuil < 0) {
                throw new IllegalArgumentException("Le seuil minimum ne peut pas être négatif.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le seuil minimum doit être un nombre.");
        }

        try {
            Integer.parseInt(quantiteLotField.getText().trim());
        } catch (NumberFormatException e) {
            quantiteLotField.setText("0");
        }

        try {
            Date.valueOf(dateField.getText().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Date invalide. Utilisez le format yyyy-mm-dd. Exemple : 2026-05-01");
        }
    }

    private void remplirChamps() {
        Medicament m = medicamentTable.getSelectionModel().getSelectedItem();

        if (m != null) {
            nomField.setText(m.getNom());
            descriptionField.setText(m.getDescription());
            categorieField.setValue(m.getCategorie());
            dosageField.setText(m.getDosage());
            formeField.setText(m.getForme());
            stockField.setText(String.valueOf(m.getQuantiteStock()));
            seuilField.setText(String.valueOf(m.getSeuilMinimum()));
            lotField.setText(m.getNumeroLot());
            quantiteLotField.setText(String.valueOf(m.getQuantiteLot()));
            dateField.setText(String.valueOf(m.getDateExpiration()));
            serviceField.setText(m.getServiceNom());

            stockField.setStyle(
                    "-fx-background-radius:20;" +
                            "-fx-border-radius:20;" +
                            "-fx-border-color:#D9B99A;"
            );
        }
    }

    private void selectionnerMedicamentParId(int id) {
        for (Medicament m : medicamentTable.getItems()) {
            if (m.getId() == id) {
                medicamentTable.getSelectionModel().select(m);
                medicamentTable.scrollTo(m);
                remplirChamps();

                stockField.requestFocus();
                stockField.setStyle(
                        "-fx-background-radius:20;" +
                                "-fx-border-radius:20;" +
                                "-fx-border-color:#c0392b;" +
                                "-fx-border-width:2;"
                );

                return;
            }
        }
    }

    private void mettreAJourStatistiques(ObservableList<Medicament> list) {
        int total = list.size();
        int ok = 0;
        int faible = 0;
        int expiration = 0;

        for (Medicament m : list) {
            if ("OK".equalsIgnoreCase(m.getEtatStock())) {
                ok++;
            }

            if ("FAIBLE".equalsIgnoreCase(m.getEtatStock()) ||
                    "RUPTURE".equalsIgnoreCase(m.getEtatStock())) {
                faible++;
            }

            if ("BIENTOT".equalsIgnoreCase(m.getAlerteExpiration()) ||
                    "EXPIRE".equalsIgnoreCase(m.getAlerteExpiration())) {
                expiration++;
            }
        }

        totalMedLabel.setText(String.valueOf(total));
        okMedLabel.setText(String.valueOf(ok));
        faibleMedLabel.setText(String.valueOf(faible));
        expirationMedLabel.setText(String.valueOf(expiration));
    }

    private void mettreAJourBarresStock(ObservableList<Medicament> list) {
        stockBarsBox.getChildren().clear();

        for (Medicament m : list) {
            HBox ligneText = new HBox();
            ligneText.setPrefWidth(1080);

            Label nom = new Label(m.getNom());
            nom.setStyle("-fx-text-fill:#7A5535; -fx-font-size:15; -fx-font-weight:bold;");

            Region espace = new Region();
            HBox.setHgrow(espace, Priority.ALWAYS);

            Label valeur = new Label(m.getQuantiteStock() + " / min " + m.getSeuilMinimum());
            valeur.setStyle("-fx-text-fill:#7A5535; -fx-font-size:15;");

            ligneText.getChildren().addAll(nom, espace, valeur);

            ProgressBar bar = new ProgressBar();

            double progress = (double) m.getQuantiteStock() / Math.max(m.getSeuilMinimum(), 1);
            bar.setProgress(Math.min(progress, 1));

            bar.setPrefWidth(1080);
            bar.setPrefHeight(12);
            bar.setMinHeight(12);

            if (m.getQuantiteStock() == 0) {
                bar.setStyle("-fx-accent:#8B1E1E;");
            } else if (m.getQuantiteStock() <= m.getSeuilMinimum()) {
                bar.setStyle("-fx-accent:#C0392B;");
            } else {
                bar.setStyle("-fx-accent:#1B7F35;");
            }

            VBox ligne = new VBox(3);
            ligne.getChildren().addAll(ligneText, bar);

            stockBarsBox.getChildren().add(ligne);
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