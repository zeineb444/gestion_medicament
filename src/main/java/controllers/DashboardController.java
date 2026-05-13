package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardController {

    @FXML private Label totalMedicamentsLabel;
    @FXML private Label stockTotalLabel;
    @FXML private Label stockFaibleLabel;
    @FXML private Label patientsSuivisLabel;

    @FXML private BarChart<String, Number> stockBarChart;
    @FXML private PieChart categoriePieChart;

    @FXML private VBox medicamentsParServiceBox;
    @FXML private VBox topMedicamentsBox;

    private final Connection connection =
            MyDataBase.getInstance().getConnection();

    @FXML
    public void initialize() {
        chargerStatistiques();
        chargerGraphiqueStock();
        chargerGraphiqueCategories();
        chargerMedicamentsParService();
        chargerTopMedicaments();
    }

    private void chargerStatistiques() {
        totalMedicamentsLabel.setText(String.valueOf(count(
                "SELECT COUNT(*) FROM medicaments"
        )));

        stockTotalLabel.setText(String.valueOf(count(
                "SELECT COALESCE(SUM(quantite_stock), 0) FROM medicaments"
        )));

        stockFaibleLabel.setText(String.valueOf(count(
                "SELECT COUNT(*) FROM medicaments WHERE quantite_stock <= seuil_minimum"
        )));

        patientsSuivisLabel.setText(String.valueOf(count(
                "SELECT COUNT(*) FROM suivi_medical"
        )));
    }

    private void chargerGraphiqueStock() {
        stockBarChart.getData().clear();
        stockBarChart.setTitle("");
        stockBarChart.setLegendVisible(true);

        XYChart.Series<String, Number> stockSeries = new XYChart.Series<>();
        stockSeries.setName("Stock");

        XYChart.Series<String, Number> seuilSeries = new XYChart.Series<>();
        seuilSeries.setName("Seuil min.");

        try {
            String sql = """
                    SELECT nom, quantite_stock, seuil_minimum
                    FROM medicaments
                    ORDER BY id DESC
                    LIMIT 5
                    """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String nom = rs.getString("nom");
                int stock = rs.getInt("quantite_stock");
                int seuil = rs.getInt("seuil_minimum");

                stockSeries.getData().add(new XYChart.Data<>(nom, stock));
                seuilSeries.getData().add(new XYChart.Data<>(nom, seuil));
            }

            stockBarChart.getData().addAll(stockSeries, seuilSeries);
            colorerGraphiqueStock();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void colorerGraphiqueStock() {
        if (stockBarChart.getData().size() < 2) {
            return;
        }

        for (XYChart.Data<String, Number> data : stockBarChart.getData().get(0).getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill:#2E7D32;");
            }
        }

        for (XYChart.Data<String, Number> data : stockBarChart.getData().get(1).getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill:#C0392B;");
            }
        }
    }

    private void chargerGraphiqueCategories() {
        categoriePieChart.getData().clear();
        categoriePieChart.setTitle("");
        categoriePieChart.setLegendVisible(true);

        try {
            String sql = """
                    SELECT categorie, COUNT(*) AS total
                    FROM medicaments
                    GROUP BY categorie
                    """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String categorie = rs.getString("categorie");
                int total = rs.getInt("total");

                if (categorie == null || categorie.isBlank()) {
                    categorie = "Autre";
                }

                categoriePieChart.getData().add(
                        new PieChart.Data(categorie + " (" + total + ")", total)
                );
            }

            if (categoriePieChart.getData().isEmpty()) {
                categoriePieChart.setData(FXCollections.observableArrayList(
                        new PieChart.Data("Aucune donnée", 1)
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerMedicamentsParService() {
        medicamentsParServiceBox.getChildren().clear();

        try {
            String sql = """
                    SELECT 
                        COALESCE(NULLIF(service_nom, ''), 'Service non défini') AS service,
                        GROUP_CONCAT(DISTINCT nom ORDER BY nom SEPARATOR ', ') AS medicaments,
                        COUNT(DISTINCT nom) AS total
                    FROM medicaments
                    GROUP BY COALESCE(NULLIF(service_nom, ''), 'Service non défini')
                    ORDER BY total DESC
                    LIMIT 3
                    """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                String service = rs.getString("service");
                String medicaments = rs.getString("medicaments");
                int total = rs.getInt("total");

                ajouterService(service, medicaments, total);
            }

            if (!hasData) {
                Label vide = new Label("Aucun médicament enregistré.");
                vide.setStyle("-fx-font-size:14; -fx-text-fill:#7A5535;");
                medicamentsParServiceBox.getChildren().add(vide);
            }

        } catch (Exception e) {
            e.printStackTrace();

            Label erreur = new Label("Erreur chargement médicaments par service.");
            erreur.setStyle("-fx-font-size:14; -fx-text-fill:#C0392B;");
            medicamentsParServiceBox.getChildren().add(erreur);
        }
    }

    private void ajouterService(String service, String medicaments, int total) {
        HBox ligne = new HBox(12);
        ligne.setPrefWidth(485);
        ligne.setStyle(
                "-fx-padding:6 0 6 0;" +
                        "-fx-border-color:transparent transparent #E8D8C8 transparent;"
        );

        Label serviceLabel = new Label(service);
        serviceLabel.setPrefWidth(115);
        serviceLabel.setStyle(
                "-fx-font-size:14;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#3B1F0E;"
        );

        Label medicamentsLabel = new Label(medicaments);
        medicamentsLabel.setWrapText(false);
        medicamentsLabel.setPrefWidth(280);
        medicamentsLabel.setStyle(
                "-fx-font-size:14;" +
                        "-fx-text-fill:#7A5535;"
        );

        Label totalLabel = new Label("(" + total + ")");
        totalLabel.setPrefWidth(40);
        totalLabel.setStyle(
                "-fx-font-size:13;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#A07850;"
        );

        ligne.getChildren().addAll(serviceLabel, medicamentsLabel, totalLabel);
        medicamentsParServiceBox.getChildren().add(ligne);
    }

    private void chargerTopMedicaments() {
        topMedicamentsBox.getChildren().clear();

        try {
            String maxSql = """
                    SELECT COALESCE(MAX(total_stock), 1)
                    FROM (
                        SELECT SUM(quantite_stock) AS total_stock
                        FROM medicaments
                        GROUP BY nom
                    ) AS t
                    """;

            Statement stMax = connection.createStatement();
            ResultSet rsMax = stMax.executeQuery(maxSql);

            int max = 1;

            if (rsMax.next()) {
                max = rsMax.getInt(1);
                if (max <= 0) {
                    max = 1;
                }
            }

            String sql = """
                    SELECT nom, SUM(quantite_stock) AS quantite_stock
                    FROM medicaments
                    GROUP BY nom
                    ORDER BY quantite_stock DESC
                    LIMIT 3
                    """;

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int rang = 1;
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                String nom = rs.getString("nom");
                int stock = rs.getInt("quantite_stock");

                ajouterTopMedicament(rang, nom, stock, max);
                rang++;
            }

            if (!hasData) {
                Label vide = new Label("Aucun médicament enregistré.");
                vide.setStyle("-fx-font-size:14; -fx-text-fill:#7A5535;");
                topMedicamentsBox.getChildren().add(vide);
            }

        } catch (Exception e) {
            e.printStackTrace();

            Label erreur = new Label("Erreur chargement top médicaments.");
            erreur.setStyle("-fx-font-size:14; -fx-text-fill:#C0392B;");
            topMedicamentsBox.getChildren().add(erreur);
        }
    }

    private void ajouterTopMedicament(int rang, String nom, int quantite, int max) {
        HBox ligne = new HBox(12);
        ligne.setPrefWidth(490);
        ligne.setStyle(
                "-fx-padding:4 0 4 0;" +
                        "-fx-border-color:transparent transparent #E8D8C8 transparent;"
        );

        Label rangLabel = new Label("#" + rang);
        rangLabel.setPrefWidth(35);
        rangLabel.setStyle(
                "-fx-font-size:14;" +
                        "-fx-text-fill:#7A5535;"
        );

        Label nomLabel = new Label(nom);
        nomLabel.setPrefWidth(180);
        nomLabel.setStyle(
                "-fx-font-size:15;" +
                        "-fx-font-weight:bold;" +
                        "-fx-text-fill:#3B1F0E;"
        );

        Region espace = new Region();
        HBox.setHgrow(espace, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(85);
        bar.setPrefHeight(8);
        bar.setProgress((double) quantite / max);
        bar.setStyle("-fx-accent:#C49A6C;");

        Label quantiteLabel = new Label(String.valueOf(quantite));
        quantiteLabel.setPrefWidth(45);
        quantiteLabel.setStyle(
                "-fx-font-size:14;" +
                        "-fx-text-fill:#7A5535;"
        );

        ligne.getChildren().addAll(rangLabel, nomLabel, espace, bar, quantiteLabel);
        topMedicamentsBox.getChildren().add(ligne);
    }

    private int count(String sql) {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}