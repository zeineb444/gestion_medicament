package service;

import models.SuiviMedical;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuiviMedicalService implements IService<SuiviMedical> {

    private Connection connection;

    public SuiviMedicalService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(SuiviMedical s) throws SQLException {

        String sql = "INSERT INTO suivi_medical " +
                "(medicament_id, nom_patient, date_suivi, observation, etat_patient) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, s.getMedicamentId());
        ps.setString(2, s.getNomPatient());
        ps.setDate(3, s.getDateSuivi());
        ps.setString(4, s.getObservation());
        ps.setString(5, s.getEtatPatient());

        ps.executeUpdate();
    }

    @Override
    public void modifier(SuiviMedical s) throws SQLException {

        String sql = "UPDATE suivi_medical SET " +
                "medicament_id=?, nom_patient=?, date_suivi=?, observation=?, etat_patient=? " +
                "WHERE id=?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, s.getMedicamentId());
        ps.setString(2, s.getNomPatient());
        ps.setDate(3, s.getDateSuivi());
        ps.setString(4, s.getObservation());
        ps.setString(5, s.getEtatPatient());
        ps.setInt(6, s.getId());

        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {

        String sql = "DELETE FROM suivi_medical WHERE id=?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();
    }

    @Override
    public List<SuiviMedical> recuperer() throws SQLException {

        List<SuiviMedical> suivis = new ArrayList<>();

        String sql = "SELECT * FROM suivi_medical";

        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {

            SuiviMedical s = new SuiviMedical();

            s.setId(rs.getInt("id"));
            s.setMedicamentId(rs.getInt("medicament_id"));
            s.setNomPatient(rs.getString("nom_patient"));
            s.setDateSuivi(rs.getDate("date_suivi"));
            s.setObservation(rs.getString("observation"));
            s.setEtatPatient(rs.getString("etat_patient"));

            suivis.add(s);
        }

        return suivis;
    }
}