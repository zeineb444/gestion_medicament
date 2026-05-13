package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class NotificationsController {

    @FXML private Label totalNotificationsLabel;
    @FXML private Label stockFaibleLabel;
    @FXML private Label ruptureLabel;
    @FXML private Label expirationLabel;

    @FXML private VBox notificationsBox;

    private final Connection connection =
            MyDataBase.getInstance().getConnection();

    @FXML
    public void initialize() {
        chargerNotifications();
    }

    private void chargerNotifications() {
        notificationsBox.getChildren().clear();

        int total = 0;
        int stockFaible = 0;
        int rupture = 0;
        int expiration = 0;

        try {
            String sql = """
                    SELECT id, nom, dosage, quantite_stock, seuil_minimum,
                           date_expiration, service_nom, alerte_expiration
                    FROM medicaments
                    WHERE quantite_stock <= seuil_minimum
                       OR alerte_expiration IN ('BIENTOT', 'EXPIRE')
                    ORDER BY
                        CASE
                            WHEN quantite_stock = 0 THEN 0
                            WHEN quantite_stock <= seuil_minimum THEN 1
                            WHEN alerte_expiration = 'EXPIRE' THEN 2
                            WHEN alerte_expiration = 'BIENTOT' THEN 3
                            ELSE 4
                        END,
                        date_expiration ASC
                    """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String dosage = rs.getString("dosage");
                int stock = rs.getInt("quantite_stock");
                int seuil = rs.getInt("seuil_minimum");
                String service = rs.getString("service_nom");
                String alerteExpiration = rs.getString("alerte_expiration");
                String dateExpiration = String.valueOf(rs.getDate("date_expiration"));

                if (dosage == null || dosage.isBlank()) {
                    dosage = "Dosage non défini";
                }

                if (service == null || service.isBlank()) {
                    service = "Service non défini";
                }

                if (stock == 0) {
                    total++;
                    rupture++;

                    ajouterNotification(
                            id,
                            "🚨",
                            nom + " — Rupture de stock",
                            "Rupture stock",
                            "#c0392b",
                            "Médicament : " + nom + " (" + dosage + ")\n" +
                                    "Service : " + service + "\n" +
                                    "Stock actuel : 0 unité(s) / Seuil : " + seuil + "\n" +
                                    "Action : Réapprovisionnement urgent requis."
                    );

                } else if (stock <= seuil) {
                    total++;
                    stockFaible++;

                    ajouterNotification(
                            id,
                            "⚠️",
                            nom + " — Stock faible",
                            "Stock faible",
                            "#c07820",
                            "Médicament : " + nom + " (" + dosage + ")\n" +
                                    "Service : " + service + "\n" +
                                    "Stock actuel : " + stock + " unité(s) / Seuil : " + seuil + "\n" +
                                    "Action : Commander des unités supplémentaires."
                    );

                } else if ("EXPIRE".equalsIgnoreCase(alerteExpiration)) {
                    total++;
                    expiration++;

                    ajouterNotification(
                            id,
                            "⏳",
                            nom + " — Médicament expiré",
                            "Médicament expiré",
                            "#d35400",
                            "Médicament : " + nom + " (" + dosage + ")\n" +
                                    "Service : " + service + "\n" +
                                    "Date expiration : " + dateExpiration + "\n" +
                                    "Action : Vérifier le lot ou retirer du stock."
                    );

                } else if ("BIENTOT".equalsIgnoreCase(alerteExpiration)) {
                    total++;
                    expiration++;

                    ajouterNotification(
                            id,
                            "⏳",
                            nom + " — Expiration proche",
                            "Expiration proche",
                            "#d35400",
                            "Médicament : " + nom + " (" + dosage + ")\n" +
                                    "Service : " + service + "\n" +
                                    "Date expiration : " + dateExpiration + "\n" +
                                    "Action : Vérifier le lot ou retirer du stock."
                    );
                }
            }

            if (total == 0) {
                Label vide = new Label("Aucune notification pour le moment.");
                vide.setStyle(
                        "-fx-font-size:16;" +
                                "-fx-text-fill:#7a5535;" +
                                "-fx-padding:30;"
                );
                notificationsBox.getChildren().add(vide);
            }

            totalNotificationsLabel.setText(String.valueOf(total));
            stockFaibleLabel.setText(String.valueOf(stockFaible));
            ruptureLabel.setText(String.valueOf(rupture));
            expirationLabel.setText(String.valueOf(expiration));

        } catch (Exception e) {
            e.printStackTrace();

            Label erreur = new Label("Erreur chargement notifications.");
            erreur.setStyle(
                    "-fx-font-size:16;" +
                            "-fx-text-fill:#c0392b;" +
                            "-fx-padding:30;"
            );
            notificationsBox.getChildren().add(erreur);
        }
    }

    private void ajouterNotification(int medicamentId,
                                     String icon,
                                     String titre,
                                     String badge,
                                     String couleur,
                                     String details) {

        VBox card = new VBox(12);
        card.setPrefWidth(1120);
        card.setMaxWidth(1120);
        card.setStyle(
                "-fx-background-color:#fffaf5;" +
                        "-fx-background-radius:15;" +
                        "-fx-border-color:#d4b99a;" +
                        "-fx-border-radius:15;" +
                        "-fx-padding:18;"
        );

        HBox header = new HBox(12);

        Label iconLabel = new Label(icon);
        iconLabel.setMinWidth(58);
        iconLabel.setMinHeight(58);
        iconLabel.setStyle(
                "-fx-font-size:24;" +
                        "-fx-background-color:#fdecef;" +
                        "-fx-background-radius:50;" +
                        "-fx-alignment:center;" +
                        "-fx-padding:12;"
        );

        VBox titleBox = new VBox(4);

        Label titleLabel = new Label(titre);
        titleLabel.setStyle(
                "-fx-font-size:18;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#3b1f0e;"
        );

        Label metaLabel = new Label("📧 admin@hopital.tn  ·  📅 " + LocalDate.now());
        metaLabel.setStyle("-fx-font-size:13; -fx-text-fill:#7a6150;");

        titleBox.getChildren().addAll(titleLabel, metaLabel);

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        Label badgeLabel = new Label(badge);
        badgeLabel.setStyle(
                "-fx-background-color:#f5ede4;" +
                        "-fx-background-radius:20;" +
                        "-fx-padding:5 14;" +
                        "-fx-text-fill:#7a5535;" +
                        "-fx-font-size:13;"
        );

        header.getChildren().addAll(iconLabel, titleBox, espace, badgeLabel);

        Label body = new Label(details);
        body.setWrapText(true);
        body.setPrefWidth(1060);
        body.setMinHeight(90);
        body.setStyle(
                "-fx-background-color:#ede0d4;" +
                        "-fx-background-radius:10;" +
                        "-fx-padding:14;" +
                        "-fx-font-size:15;" +
                        "-fx-text-fill:#3b1f0e;"
        );

        HBox footer = new HBox(12);

        Label typeLabel = new Label(icon + " " + badge);
        typeLabel.setStyle(
                "-fx-background-color:#fff0f0;" +
                        "-fx-border-color:#f2b6b6;" +
                        "-fx-border-radius:20;" +
                        "-fx-background-radius:20;" +
                        "-fx-padding:6 16;" +
                        "-fx-font-size:13;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:" + couleur + ";"
        );

        Label emailLabel = new Label("✅ Email envoyé");
        emailLabel.setStyle(
                "-fx-background-color:#e8f5e9;" +
                        "-fx-background-radius:20;" +
                        "-fx-padding:6 16;" +
                        "-fx-font-size:13;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#2e7d32;"
        );

        Region footerSpace = new Region();
        HBox.setHgrow(footerSpace, Priority.ALWAYS);

        Button modifierBtn = new Button("✎ Modifier le stock");
        modifierBtn.setStyle(
                "-fx-background-color:#a07850;" +
                        "-fx-text-fill:white;" +
                        "-fx-background-radius:22;" +
                        "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 22;"
        );

        modifierBtn.setOnAction(event -> {
            MainController.getInstance().openMedicamentFromNotification(medicamentId);
        });

        footer.getChildren().addAll(typeLabel, emailLabel, footerSpace, modifierBtn);

        card.getChildren().addAll(header, body, footer);
        notificationsBox.getChildren().add(card);
    }
}