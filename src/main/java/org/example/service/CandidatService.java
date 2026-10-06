package org.example.service;

import org.example.entities.Candidat;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.myInterfaces.DBservices;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatService implements DBservices<Candidat> {
    private final Connection connection;

    public CandidatService() {
        this.connection = ConxDB.getInstance();
    }

    @Override
    public List<Candidat> getAll() {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM candidat";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("Candidat trouvé : " + rs.getString("nom") + " " + rs.getString("prenom"));
                Candidat candidat = new Candidat(
                        rs.getInt("CIN"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("dateNaissance").toLocalDate(),
                        rs.getString("email"),
                        rs.getInt("telephone"),
                        rs.getString("adresse"),
                        VehiculeType.valueOf(rs.getString("typePermis").toUpperCase()),
                        rs.getString("status"),
                        rs.getString("repos"),
                        rs.getInt("nbr_seance_code_atteint"),
                        rs.getInt("nbr_seance_conduit_atteint"),
                        rs.getObject("id_vehicule") != null ? rs.getInt("id_vehicule") : null,
                        rs.getInt("montant_a_payer")
                );
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidats;
    }

    @Override
    public Candidat getById(Integer id) {
        String query = "SELECT * FROM candidat WHERE CIN = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Candidat(
                        rs.getInt("CIN"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("dateNaissance").toLocalDate(),
                        rs.getString("email"),
                        rs.getInt("telephone"),
                        rs.getString("adresse"),
                        VehiculeType.valueOf(rs.getString("typePermis").toUpperCase()),
                        rs.getString("status"),
                        rs.getString("repos"),
                        rs.getInt("nbr_seance_code_atteint"),
                        rs.getInt("nbr_seance_conduit_atteint"),
                        rs.getObject("id_vehicule") != null ? rs.getInt("id_vehicule") : null,
                        rs.getInt("montant_a_payer")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Candidat candidat) {
        String query = "INSERT INTO candidat VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, candidat.getCIN());
            pstmt.setString(2, candidat.getNom());
            pstmt.setString(3, candidat.getPrenom());
            pstmt.setDate(4, Date.valueOf(candidat.getDateNaissance()));
            pstmt.setString(5, candidat.getEmail());
            pstmt.setInt(6, candidat.getTelephone());
            pstmt.setString(7, candidat.getAdresse());
            pstmt.setString(8, candidat.getTypePermis().toString());
            pstmt.setString(9, candidat.getStatus());
            pstmt.setString(10, candidat.getRepos());
            pstmt.setInt(11, 0);
            pstmt.setInt(12, 0);
            pstmt.setNull(13, Types.INTEGER); // id_vehicule NULL
            pstmt.setInt(14, 0); // montant_a_payer initialisé à 0
            pstmt.executeUpdate();
            createCandidatFolder(candidat.getCIN());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createCandidatFolder(int cin) {
        String path = "documents/candidats/" + cin + "_candidat";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public boolean update(Integer id, Candidat candidat) {
        String query = "UPDATE candidat SET nom=?, prenom=?, dateNaissance=?, email=?, telephone=?, adresse=?, " +
                "typePermis=?, status=?, repos=?, nbr_seance_code_atteint=?, nbr_seance_conduit_atteint=?, id_vehicule=?, montant_a_payer=? WHERE CIN=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, candidat.getNom());
            pstmt.setString(2, candidat.getPrenom());
            pstmt.setDate(3, Date.valueOf(candidat.getDateNaissance()));
            pstmt.setString(4, candidat.getEmail());
            pstmt.setInt(5, candidat.getTelephone());
            pstmt.setString(6, candidat.getAdresse());
            pstmt.setString(7, candidat.getTypePermis().toString());
            pstmt.setString(8, candidat.getStatus());
            pstmt.setString(9, candidat.getRepos());
            pstmt.setInt(10, candidat.getNbr_seance_code_atteint());
            pstmt.setInt(11, candidat.getNbr_seance_conduit_atteint());
            if (candidat.getIdVehicule() != null) {
                pstmt.setInt(12, candidat.getIdVehicule());
            } else {
                pstmt.setNull(12, Types.INTEGER);
            }
            pstmt.setInt(13, candidat.getMontantAPayer());
            pstmt.setInt(14, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM candidat WHERE CIN = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            deleteCandidatFolder(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteCandidatFolder(int cin) {
        String path = "documents/candidats/" + cin + "_candidat";
        File folder = new File(path);
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                file.delete();
            }
            folder.delete();
        }
    }
}
