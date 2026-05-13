package service;

import models.SuiviMedical;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuiviMedicalService {

    private final Connection connection;

    public SuiviMedicalService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public void ajouter(SuiviMedical s) throws SQLException {
        String req = """
                INSERT INTO suivi_medical
                (medicament_id, nom_patient, date_suivi, observation, etat_patient)
                VALUES (?, ?, ?, ?, ?)
                """;

        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, s.getMedicamentId());
        ps.setString(2, s.getNomPatient());
        ps.setDate(3, s.getDateSuivi());
        ps.setString(4, s.getObservation());
        ps.setString(5, s.getEtatPatient());

        ps.executeUpdate();
    }

    public void modifier(SuiviMedical s) throws SQLException {
        String req = """
                UPDATE suivi_medical
                SET medicament_id = ?,
                    nom_patient = ?,
                    date_suivi = ?,
                    observation = ?,
                    etat_patient = ?
                WHERE id = ?
                """;

        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, s.getMedicamentId());
        ps.setString(2, s.getNomPatient());
        ps.setDate(3, s.getDateSuivi());
        ps.setString(4, s.getObservation());
        ps.setString(5, s.getEtatPatient());
        ps.setInt(6, s.getId());

        ps.executeUpdate();
    }

    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM suivi_medical WHERE id = ?";

        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public List<SuiviMedical> recuperer() throws SQLException {
        List<SuiviMedical> list = new ArrayList<>();

        String req = """
                SELECT sm.*, m.nom AS medicament_nom
                FROM suivi_medical sm
                JOIN medicaments m ON sm.medicament_id = m.id
                """;

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            SuiviMedical s = new SuiviMedical();

            s.setId(rs.getInt("id"));
            s.setMedicamentId(rs.getInt("medicament_id"));
            s.setMedicamentNom(rs.getString("medicament_nom"));
            s.setNomPatient(rs.getString("nom_patient"));
            s.setDateSuivi(rs.getDate("date_suivi"));
            s.setObservation(rs.getString("observation"));
            s.setEtatPatient(rs.getString("etat_patient"));

            list.add(s);
        }

        return list;
    }
}