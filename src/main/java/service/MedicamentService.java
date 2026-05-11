package service;

import models.Medicament;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentService implements IService<Medicament> {

    private Connection connection;

    public MedicamentService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Medicament medicament) throws SQLException {
        String sql = "INSERT INTO medicaments " +
                "(nom, description, categorie, dosage, forme, quantite_stock, seuil_minimum, numero_lot, quantite_lot, date_expiration, service_nom, etat_stock, alerte_expiration) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, medicament.getNom());
        ps.setString(2, medicament.getDescription());
        ps.setString(3, medicament.getCategorie());
        ps.setString(4, medicament.getDosage());
        ps.setString(5, medicament.getForme());
        ps.setInt(6, medicament.getQuantiteStock());
        ps.setInt(7, medicament.getSeuilMinimum());
        ps.setString(8, medicament.getNumeroLot());
        ps.setInt(9, medicament.getQuantiteLot());
        ps.setDate(10, medicament.getDateExpiration());
        ps.setString(11, medicament.getServiceNom());
        ps.setString(12, medicament.getEtatStock());
        ps.setString(13, medicament.getAlerteExpiration());

        ps.executeUpdate();
    }

    @Override
    public void modifier(Medicament medicament) throws SQLException {
        String sql = "UPDATE medicaments SET nom=?, description=?, categorie=?, dosage=?, forme=?, quantite_stock=?, seuil_minimum=?, numero_lot=?, quantite_lot=?, date_expiration=?, service_nom=?, etat_stock=?, alerte_expiration=? WHERE id=?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, medicament.getNom());
        ps.setString(2, medicament.getDescription());
        ps.setString(3, medicament.getCategorie());
        ps.setString(4, medicament.getDosage());
        ps.setString(5, medicament.getForme());
        ps.setInt(6, medicament.getQuantiteStock());
        ps.setInt(7, medicament.getSeuilMinimum());
        ps.setString(8, medicament.getNumeroLot());
        ps.setInt(9, medicament.getQuantiteLot());
        ps.setDate(10, medicament.getDateExpiration());
        ps.setString(11, medicament.getServiceNom());
        ps.setString(12, medicament.getEtatStock());
        ps.setString(13, medicament.getAlerteExpiration());
        ps.setInt(14, medicament.getId());

        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM medicaments WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Medicament> recuperer() throws SQLException {
        List<Medicament> medicaments = new ArrayList<>();

        String sql = "SELECT * FROM medicaments";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Medicament m = new Medicament();
            m.setId(rs.getInt("id"));
            m.setNom(rs.getString("nom"));
            m.setDescription(rs.getString("description"));
            m.setCategorie(rs.getString("categorie"));
            m.setDosage(rs.getString("dosage"));
            m.setForme(rs.getString("forme"));
            m.setQuantiteStock(rs.getInt("quantite_stock"));
            m.setSeuilMinimum(rs.getInt("seuil_minimum"));
            m.setNumeroLot(rs.getString("numero_lot"));
            m.setQuantiteLot(rs.getInt("quantite_lot"));
            m.setDateExpiration(rs.getDate("date_expiration"));
            m.setServiceNom(rs.getString("service_nom"));
            m.setEtatStock(rs.getString("etat_stock"));
            m.setAlerteExpiration(rs.getString("alerte_expiration"));

            medicaments.add(m);
        }

        return medicaments;
    }
    public List<Medicament> rechercher(String nom,
                                       String categorie,
                                       String service) throws SQLException {

        List<Medicament> medicaments = new ArrayList<>();

        String sql = "SELECT * FROM medicaments " +
                "WHERE nom LIKE ? " +
                "AND categorie LIKE ? " +
                "AND service_nom LIKE ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, "%" + nom + "%");
        ps.setString(2, "%" + categorie + "%");
        ps.setString(3, "%" + service + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Medicament m = new Medicament();

            m.setId(rs.getInt("id"));
            m.setNom(rs.getString("nom"));
            m.setDescription(rs.getString("description"));
            m.setCategorie(rs.getString("categorie"));
            m.setDosage(rs.getString("dosage"));
            m.setForme(rs.getString("forme"));
            m.setQuantiteStock(rs.getInt("quantite_stock"));
            m.setSeuilMinimum(rs.getInt("seuil_minimum"));
            m.setNumeroLot(rs.getString("numero_lot"));
            m.setQuantiteLot(rs.getInt("quantite_lot"));
            m.setDateExpiration(rs.getDate("date_expiration"));
            m.setServiceNom(rs.getString("service_nom"));
            m.setEtatStock(rs.getString("etat_stock"));
            m.setAlerteExpiration(rs.getString("alerte_expiration"));

            medicaments.add(m);
        }

        return medicaments;
    }
}