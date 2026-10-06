package org.example.service;

import org.example.entities.ExamenConduit;
import org.example.entities.interfaces.TimeToDuration;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.myInterfaces.DBservices;

import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ExamenConduiteServices implements DBservices<ExamenConduit> {
    @Override
    public List<ExamenConduit> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ExamenConduit> examensConduits = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_conduite";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                ExamenConduit examenConduit=new ExamenConduit();
                examenConduit.setId(rs.getInt("id"));
                examenConduit.setIdCandidat(rs.getInt("candidat_id"));
                examenConduit.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenConduit.setDateExamen(rs.getTimestamp("date_time").toLocalDateTime());
                examenConduit.setPrix(rs.getFloat("price"));
                examenConduit.setIdMoniteur(rs.getInt("moniteur_id"));
                examenConduit.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenConduit.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                examenConduit.setLocationX(rs.getFloat("locationX"));
                examenConduit.setLocationY(rs.getFloat("locationY"));
                examenConduit.setLocationName(rs.getString("locationName"));
                examenConduit.setStatus(rs.getBoolean("status"));
                examensConduits.add(examenConduit);
            }
        } catch (Exception e) {
            System.out.println("Error in getAll: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getAll: " + e.getMessage());
            }
        }
        return examensConduits;
    }

    @Override
    public ExamenConduit getById(Integer id) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ExamenConduit examenConduit = null;
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_conduite WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                examenConduit = new ExamenConduit();
                examenConduit.setId(rs.getInt("id"));
                examenConduit.setIdCandidat(rs.getInt("candidat_id"));
                examenConduit.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenConduit.setDateExamen(rs.getTimestamp("date_time").toLocalDateTime());
                examenConduit.setPrix(rs.getFloat("price"));
                examenConduit.setIdMoniteur(rs.getInt("moniteur_id"));
                examenConduit.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenConduit.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                examenConduit.setLocationX(rs.getFloat("locationX"));
                examenConduit.setLocationY(rs.getFloat("locationY"));
                examenConduit.setLocationName(rs.getString("locationName"));
                examenConduit.setStatus(rs.getBoolean("status"));
            }
        } catch (Exception e) {
            System.out.println("Error in getById: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getById: " + e.getMessage());
            }
        }
        return examenConduit;
    }

    @Override
    public boolean create(ExamenConduit examenConduit) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO examen_conduite (type,date_time,duree,moniteur_id,candidat_id,price,matricule_vehicule,locationX,locationY,locationName,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1, examenConduit.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(examenConduit.getDateExamen()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(examenConduit.getDurationExamen())));
            ps.setInt(4, examenConduit.getIdMoniteur());
            ps.setInt(5, examenConduit.getIdCandidat());
            ps.setFloat(6, examenConduit.getPrix());
            ps.setInt(7, examenConduit.getMatriculeVehicule());
            ps.setFloat(8, examenConduit.getLocationX());
            ps.setFloat(9, examenConduit.getLocationY());
            ps.setString(10, examenConduit.getLocationName());
            ps.setBoolean(11, examenConduit.getStatus());

            System.out.println("query good");
            if(ps.executeUpdate() > 0) {
                System.out.println("ExamenConduit created");
                return true;
            } else {
                System.out.println("ExamenConduit not created, statement issue");
                return false;
            }
        } catch(Exception e) {
            System.out.println("ExamenConduit not created, exception issue: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in create: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean update(Integer id, ExamenConduit examenConduit) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE examen_conduite SET type = ?, date_time = ?, duree = ?, moniteur_id = ?, candidat_id = ?, price = ?, matricule_vehicule = ?, locationX = ?, locationY = ?, locationName = ?, status = ? WHERE id = ?";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1, examenConduit.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(examenConduit.getDateExamen()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(examenConduit.getDurationExamen())));
            ps.setInt(4, examenConduit.getIdMoniteur());
            ps.setInt(5, examenConduit.getIdCandidat());
            ps.setFloat(6, examenConduit.getPrix());
            ps.setInt(7, examenConduit.getMatriculeVehicule());
            ps.setFloat(8, examenConduit.getLocationX());
            ps.setFloat(9, examenConduit.getLocationY());
            ps.setString(10, examenConduit.getLocationName());
            ps.setBoolean(11, examenConduit.getStatus());
            ps.setInt(12, id); // Set the id for the WHERE clause

            System.out.println("query good");
            if (ps.executeUpdate() > 0) {
                System.out.println("ExamenConduit updated");
                return true;
            } else {
                System.out.println("ExamenConduit not updated, statement issue");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ExamenConduit not updated, exception issue: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in update: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean delete(Integer id) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "DELETE FROM examen_conduite WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                System.out.println("ExamenConduit deleted");
                return true;
            } else {
                System.out.println("ExamenConduit not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting ExamenConduit: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in delete: " + e.getMessage());
            }
        }
    }

    public List<ExamenConduit> getExamensByCandidat(Integer cin) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ExamenConduit> examensConduits = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_conduite WHERE candidat_id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, cin);
            rs = ps.executeQuery();
            while (rs.next()) {
                ExamenConduit examenConduit = new ExamenConduit();
                examenConduit.setId(rs.getInt("id"));
                examenConduit.setIdCandidat(rs.getInt("candidat_id"));
                examenConduit.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenConduit.setDateExamen(rs.getTimestamp("date_time").toLocalDateTime());
                examenConduit.setPrix(rs.getFloat("price"));
                examenConduit.setIdMoniteur(rs.getInt("moniteur_id"));
                examenConduit.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenConduit.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                examenConduit.setLocationX(rs.getFloat("locationX"));
                examenConduit.setLocationY(rs.getFloat("locationY"));
                examenConduit.setLocationName(rs.getString("locationName"));
                examenConduit.setStatus(rs.getBoolean("status"));
                examensConduits.add(examenConduit);
            }
        } catch (Exception e) {
            System.out.println("Error in getExamensByCandidat: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getExamensByCandidat: " + e.getMessage());
            }
        }
        return examensConduits;
    }
}
