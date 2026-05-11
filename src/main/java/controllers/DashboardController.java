package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardController {

    @FXML private Label totalMedicamentsLabel;
    @FXML private Label stockFaibleLabel;
    @FXML private Label expiresLabel;
    @FXML private Label suivisLabel;

    private final Connection connection =
            MyDataBase.getInstance().getConnection();

    @FXML
    public void initialize() {
        totalMedicamentsLabel.setText(String.valueOf(count("SELECT COUNT(*) FROM medicaments")));
        stockFaibleLabel.setText(String.valueOf(count("SELECT COUNT(*) FROM medicaments WHERE etat_stock='FAIBLE'")));
        expiresLabel.setText(String.valueOf(count("SELECT COUNT(*) FROM medicaments WHERE alerte_expiration='EXPIRE' OR alerte_expiration='BIENTOT'")));
        suivisLabel.setText(String.valueOf(count("SELECT COUNT(*) FROM suivi_medical")));
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