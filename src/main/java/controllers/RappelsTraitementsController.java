package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RappelsTraitementsController {

    @FXML
    private VBox rappelsBox;

    @FXML
    public void initialize() {
        chargerRappels();
    }

    private void chargerRappels() {
        rappelsBox.getChildren().clear();

        ajouterRappel(
                "Ahmed Ben Ali",
                "Paracétamol 500mg",
                "14:00",
                "EN ATTENTE",
                "#C07820"
        );

        ajouterRappel(
                "Rania Trabelsi",
                "Amoxicilline 250mg",
                "14:30",
                "RETARD",
                "#C0392B"
        );

        ajouterRappel(
                "Karim Mansour",
                "Metformine 500mg",
                "18:00",
                "EN ATTENTE",
                "#C07820"
        );

        ajouterRappel(
                "Leila Belhaj",
                "Ibuprofène 400mg",
                "20:00",
                "EN ATTENTE",
                "#C07820"
        );
    }

    private void ajouterRappel(String patient,
                               String medicament,
                               String heure,
                               String statut,
                               String couleur) {

        VBox card = new VBox(12);
        card.setPrefWidth(1100);
        card.setStyle(
                "-fx-background-color:#fffaf5;" +
                        "-fx-background-radius:16;" +
                        "-fx-border-color:#d4b99a;" +
                        "-fx-border-radius:16;" +
                        "-fx-padding:18;"
        );

        HBox top = new HBox(14);

        Label avatar = new Label(getInitiales(patient));
        avatar.setMinWidth(46);
        avatar.setMinHeight(46);
        avatar.setStyle(
                "-fx-background-color:#ede0d4;" +
                        "-fx-background-radius:50;" +
                        "-fx-alignment:center;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#7A5535;"
        );

        VBox infos = new VBox(4);

        Label patientLabel = new Label(patient);
        patientLabel.setStyle(
                "-fx-font-size:18;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#3B1F0E;"
        );

        Label medicamentLabel = new Label("💊 " + medicament);
        medicamentLabel.setStyle(
                "-fx-font-size:15;" +
                        "-fx-text-fill:#7A6150;"
        );

        infos.getChildren().addAll(patientLabel, medicamentLabel);

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        Label heureLabel = new Label(heure);
        heureLabel.setStyle(
                "-fx-background-color:#E3F2FD;" +
                        "-fx-background-radius:20;" +
                        "-fx-padding:6 16;" +
                        "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#1565C0;"
        );

        Label statutLabel = new Label(statut);
        statutLabel.setStyle(
                "-fx-background-color:#fff3cd;" +
                        "-fx-background-radius:20;" +
                        "-fx-padding:6 16;" +
                        "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:" + couleur + ";"
        );

        Button ajouterSuiviBtn = new Button("📝 Ajouter Suivi");
        ajouterSuiviBtn.setStyle(
                "-fx-background-color:#A07850;" +
                        "-fx-text-fill:white;" +
                        "-fx-background-radius:22;" +
                        "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 20;"
        );

        ajouterSuiviBtn.setOnAction(event -> {
            SuiviMedicalController.setPatientDepuisRappel(patient);
            MainController.getInstance().openSuiviMedicalFromRappel();
        });

        top.getChildren().addAll(
                avatar,
                infos,
                espace,
                heureLabel,
                statutLabel,
                ajouterSuiviBtn
        );

        card.getChildren().add(top);
        rappelsBox.getChildren().add(card);
    }

    private String getInitiales(String nomComplet) {
        if (nomComplet == null || nomComplet.isBlank()) {
            return "?";
        }

        String[] parts = nomComplet.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
    }
}