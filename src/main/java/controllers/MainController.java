package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    private static MainController instance;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox sidebar;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private Label logoLabel;

    @FXML
    private Label navigationLabel;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button medicamentBtn;

    @FXML
    private Button emailBtn;

    private boolean sidebarCollapsed = false;

    private final String ACTIVE_STYLE =
            "-fx-background-color:#c49a6c;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:10;" +
                    "-fx-font-size:15;" +
                    "-fx-font-weight:bold;" +
                    "-fx-alignment:center-left;" +
                    "-fx-padding:0 0 0 18;";

    private final String NORMAL_STYLE =
            "-fx-background-color:transparent;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:15;" +
                    "-fx-font-weight:bold;" +
                    "-fx-alignment:center-left;" +
                    "-fx-padding:0 0 0 18;";

    private final String ACTIVE_STYLE_COLLAPSED =
            "-fx-background-color:#c49a6c;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:10;" +
                    "-fx-font-size:17;" +
                    "-fx-font-weight:bold;" +
                    "-fx-alignment:center;" +
                    "-fx-padding:0;";

    private final String NORMAL_STYLE_COLLAPSED =
            "-fx-background-color:transparent;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:17;" +
                    "-fx-font-weight:bold;" +
                    "-fx-alignment:center;" +
                    "-fx-padding:0;";

    @FXML
    public void initialize() {
        instance = this;
        showDashboard();
    }

    public static MainController getInstance() {
        return instance;
    }

    @FXML
    void showDashboard() {
        chargerPage("/Dashboard.fxml");
        resetButtons();
        dashboardBtn.setStyle(sidebarCollapsed ? ACTIVE_STYLE_COLLAPSED : ACTIVE_STYLE);
    }

    @FXML
    void showMedicaments() {
        chargerPage("/Medicament.fxml");
        resetButtons();
        medicamentBtn.setStyle(sidebarCollapsed ? ACTIVE_STYLE_COLLAPSED : ACTIVE_STYLE);
    }

    @FXML
    void showEmails() {
        chargerPage("/Emails.fxml");
        resetButtons();
        emailBtn.setStyle(sidebarCollapsed ? ACTIVE_STYLE_COLLAPSED : ACTIVE_STYLE);
    }

    public void openMedicamentFromNotification(int medicamentId) {
        MedicamentController.setMedicamentIdASelectionner(medicamentId);
        showMedicaments();
    }

    public void openSuiviMedicalFromRappel() {
        chargerPage("/SuiviMedical.fxml");
    }

    @FXML
    void toggleSidebar() {
        if (!sidebarCollapsed) {
            sidebarCollapsed = true;

            sidebar.setPrefWidth(80.0);
            sidebar.setMinWidth(80.0);
            sidebar.setMaxWidth(80.0);

            AnchorPane.setLeftAnchor(mainScrollPane, 80.0);

            logoLabel.setVisible(false);
            logoLabel.setManaged(false);

            navigationLabel.setVisible(false);
            navigationLabel.setManaged(false);

            dashboardBtn.setText("📊");
            medicamentBtn.setText("💊");
            emailBtn.setText("🔔");

            dashboardBtn.setPrefWidth(52);
            medicamentBtn.setPrefWidth(52);
            emailBtn.setPrefWidth(52);

            resetButtonsCollapsed();

        } else {
            sidebarCollapsed = false;

            sidebar.setPrefWidth(250.0);
            sidebar.setMinWidth(250.0);
            sidebar.setMaxWidth(250.0);

            AnchorPane.setLeftAnchor(mainScrollPane, 250.0);

            logoLabel.setVisible(true);
            logoLabel.setManaged(true);

            navigationLabel.setVisible(true);
            navigationLabel.setManaged(true);

            dashboardBtn.setText("📊 Dashboard");
            medicamentBtn.setText("💊 Médicaments");
            emailBtn.setText("🔔 Notifications");

            dashboardBtn.setPrefWidth(200);
            medicamentBtn.setPrefWidth(200);
            emailBtn.setPrefWidth(200);

            resetButtons();
        }
    }

    private void resetButtons() {
        if (sidebarCollapsed) {
            resetButtonsCollapsed();
            return;
        }

        dashboardBtn.setStyle(NORMAL_STYLE);
        medicamentBtn.setStyle(NORMAL_STYLE);
        emailBtn.setStyle(NORMAL_STYLE);
    }

    private void resetButtonsCollapsed() {
        dashboardBtn.setStyle(NORMAL_STYLE_COLLAPSED);
        medicamentBtn.setStyle(NORMAL_STYLE_COLLAPSED);
        emailBtn.setStyle(NORMAL_STYLE_COLLAPSED);
    }

    private void chargerPage(String fxml) {
        try {
            AnchorPane page = FXMLLoader.load(getClass().getResource(fxml));

            double pageHeight = page.getPrefHeight();
            double pageWidth = page.getPrefWidth();

            if (pageHeight < 900) {
                pageHeight = 900;
            }

            contentPane.setPrefHeight(pageHeight);
            contentPane.setMinHeight(pageHeight);

            contentPane.setPrefWidth(pageWidth);
            contentPane.setMinWidth(pageWidth);

            page.setPrefHeight(pageHeight);
            page.setMinHeight(pageHeight);

            contentPane.getChildren().setAll(page);

            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}