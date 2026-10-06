/*package org.example.service;

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
                        rs.getString("repos")
                );
                candidats.add(candidat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        candidats.forEach(c->createCandidatFolder(c.getCIN()));
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
                        rs.getString("repos")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Candidat candidat) {
        String query = "INSERT INTO candidat VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, candidat.getCIN());
            pstmt.setString(2, candidat.getNom());
            pstmt.setString(3, candidat.getPrenom());
            pstmt.setDate(4, Date.valueOf(candidat.getDateNaissance()));
            pstmt.setString(5, candidat.getEmail());
            pstmt.setInt(6, candidat.getTelephone());
            pstmt.setString(7, candidat.getAdresse());
            pstmt.setString(8, candidat.getTypePermis().toString().toLowerCase());
            pstmt.setString(9, candidat.getStatus());
            pstmt.setString(10, candidat.getDocument());
            pstmt.setInt(11,0);
            pstmt.setInt(12,0);
            pstmt.setNull(13,Types.INTEGER);
            pstmt.executeUpdate();
            createCandidatFolder(candidat.getCIN());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createCandidatFolder(int cin) {
        // Get the current directory of the project
        String projectPath = new File("").getAbsolutePath();

        // Move up one level to reach the parent directory (where 'service' and 'repository' are located)
        File parentDir = new File(projectPath).getParentFile();

        // Define the base path for the vehicle folder
        String basePath = parentDir + File.separator+"ProjectJava"+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"org"+File.separator+"example"+File.separator + "repository" + File.separator + "condidats" + File.separator + cin;

        // Define the full paths for the two subfolders
        File documentsFolder = new File(basePath + File.separator + "documents");

        // Create the folders if they don't exist
        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs();
        }
    }
    @Override
    public boolean update(Integer id, Candidat candidat) {
        String query = "UPDATE candidat SET nom=?, prenom=?, dateNaissance=?, email=?, telephone=?, adresse=?, typePermis=?, status=?, repos=? WHERE CIN=?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, candidat.getNom());
            pstmt.setString(2, candidat.getPrenom());
            pstmt.setDate(3, Date.valueOf(candidat.getDateNaissance()));
            pstmt.setString(4, candidat.getEmail());
            pstmt.setInt(5, candidat.getTelephone());
            pstmt.setString(6, candidat.getAdresse());
            pstmt.setString(7, candidat.getTypePermis().toString().toLowerCase());
            pstmt.setString(8, candidat.getStatus());
            pstmt.setString(9, candidat.getDocument());
            pstmt.setInt(10, id);
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
        String path = "C:/Autoecole/DataBase/" + cin + "candidat";
        File folder = new File(path);
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                file.delete();
            }
            folder.delete();
        }
    }
}

 */