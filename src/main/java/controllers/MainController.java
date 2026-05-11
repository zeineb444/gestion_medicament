package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox sidebar;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button medicamentBtn;

    @FXML
    private Button suiviBtn;

    @FXML
    private Button emailBtn;

    @FXML private VBox stockBarsBox;
    private final String ACTIVE_STYLE =
            "-fx-background-color:#c49a6c;" +
                    "-fx-text-fill:white;" +
                    "-fx-background-radius:10;" +
                    "-fx-font-size:15;";

    private final String NORMAL_STYLE =
            "-fx-background-color:transparent;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-size:15;";

    @FXML
    void showDashboard() {

        chargerPage("/Dashboard.fxml");

        resetButtons();
        dashboardBtn.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void showMedicaments() {
        chargerPage("/Medicament.fxml");
        resetButtons();
        medicamentBtn.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void showSuiviMedical() {
        chargerPage("/SuiviMedical.fxml");
        resetButtons();
        suiviBtn.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void showEmails() {
        chargerPage("/Emails.fxml");
        resetButtons();
        emailBtn.setStyle(ACTIVE_STYLE);
    }

    @FXML
    void toggleSidebar() {
        if (sidebar.getPrefWidth() == 250.0) {
            sidebar.setPrefWidth(80.0);
            contentPane.setLayoutX(80.0);
            contentPane.setPrefWidth(1420.0);
        } else {
            sidebar.setPrefWidth(250.0);
            contentPane.setLayoutX(250.0);
            contentPane.setPrefWidth(1250.0);
        }
    }
    @FXML
    public void initialize() {
        showDashboard();
    }

    private void resetButtons() {
        dashboardBtn.setStyle(NORMAL_STYLE);
        medicamentBtn.setStyle(NORMAL_STYLE);
        suiviBtn.setStyle(NORMAL_STYLE);
        emailBtn.setStyle(NORMAL_STYLE);
    }

    private void chargerPage(String fxml) {
        try {
            AnchorPane page = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}