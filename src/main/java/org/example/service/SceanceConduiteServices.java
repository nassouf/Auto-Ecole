package org.example.service;

import org.example.entities.SceanceConduite;
import org.example.entities.interfaces.TimeToDuration;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.myInterfaces.DBservices;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SceanceConduiteServices implements DBservices<SceanceConduite> {

    @Override
    public List<SceanceConduite> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SceanceConduite> sceanceConduites = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_conduite";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                SceanceConduite sceanceConduite=new SceanceConduite();
                sceanceConduite.setSceanceId(rs.getInt("idSeanceConduite"));
                sceanceConduite.setCondidatId(rs.getInt("candidat_id"));
                sceanceConduite.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceConduite.setSceanceDate(rs.getTimestamp("date_time").toLocalDateTime());
                sceanceConduite.setPrice(rs.getFloat("price"));
                sceanceConduite.setMoniteurId(rs.getInt("moniteur_id"));
                sceanceConduite.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                sceanceConduite.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                sceanceConduite.setLocationX(rs.getFloat("locationX"));
                sceanceConduite.setLocationY(rs.getFloat("locationY"));
                sceanceConduite.setLocationName(rs.getString("locationName"));
                sceanceConduites.add(sceanceConduite);
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
        return sceanceConduites;
    }
    @Override
    public SceanceConduite getById(Integer id) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SceanceConduite sceanceConduite = null;
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_conduite WHERE idSeanceConduite = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                sceanceConduite=new SceanceConduite();
                sceanceConduite.setSceanceId(rs.getInt("idSeanceConduite"));
                sceanceConduite.setCondidatId(rs.getInt("candidat_id"));
                sceanceConduite.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceConduite.setSceanceDate(rs.getTimestamp("date_time").toLocalDateTime());
                sceanceConduite.setPrice(rs.getFloat("price"));
                sceanceConduite.setMoniteurId(rs.getInt("moniteur_id"));
                sceanceConduite.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                sceanceConduite.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                sceanceConduite.setLocationX(rs.getFloat("locationX"));
                sceanceConduite.setLocationY(rs.getFloat("locationY"));
                sceanceConduite.setLocationName(rs.getString("locationName"));
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
        return sceanceConduite;
    }

    @Override
    public boolean create(SceanceConduite sceanceConduite) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO seances_conduite (type,date_time,duree,moniteur_id,candidat_id,matricule_vehicule,price,locationX,locationY,locationName) VALUES (?,?,?,?, ?, ?, ?,?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1,sceanceConduite.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(sceanceConduite.getSceanceDate()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(sceanceConduite.getDuration())));
            ps.setInt(4,sceanceConduite.getMoniteurId());
            ps.setInt(5,sceanceConduite.getCondidatId());
            ps.setInt(6,sceanceConduite.getMatriculeVehicule());
            ps.setFloat(7,sceanceConduite.getPrice());
            ps.setFloat(8,sceanceConduite.getLocationX());
            ps.setFloat(9,sceanceConduite.getLocationY());
            ps.setString(10,sceanceConduite.getLocationName());

            System.out.println("query good");
            if(ps.executeUpdate() > 0) {
                System.out.println("SceanceConduite created");

                return true;
            } else {
                System.out.println("SceanceConduite not created, statement issue");
                return false;
            }
        } catch(Exception e) {
            System.out.println("SceanceConduite not created, exception issue: " + e.getMessage());
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
    public boolean update(Integer id, SceanceConduite sceanceConduite) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE seances_conduite SET type = ?, date_time = ?, duree = ?, moniteur_id = ?, candidat_id = ?,matricule_vehicule=?, price = ?,locationX=?,locationY=?,locationName=? WHERE idSeanceConduite = ?";

            // Initialize ps with the query
            ps = cnx.prepareStatement(query);

            // Now you can set the parameters
            ps.setString(1, sceanceConduite.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(sceanceConduite.getSceanceDate()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(sceanceConduite.getDuration())));
            ps.setInt(4, sceanceConduite.getMoniteurId());
            ps.setInt(5, sceanceConduite.getCondidatId());
            ps.setInt(6, sceanceConduite.getMatriculeVehicule());
            ps.setFloat(7, sceanceConduite.getPrice());
            ps.setFloat(8, sceanceConduite.getLocationX());
            ps.setFloat(9, sceanceConduite.getLocationY());
            ps.setString(10, sceanceConduite.getLocationName());
            ps.setInt(11, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("scConduite updated");
                return true;
            } else {
                System.out.println("scConduite not updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating scCobduite: " + e.getMessage());
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
            String query = "DELETE FROM seances_conduite WHERE idSeanceConduite = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                System.out.println("sceanceConduite deleted");
                return true;
            } else {
                System.out.println("seanceConduite not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting sceanceConduite: " + e.getMessage());
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
    public List<SceanceConduite> getSceancesByCandidat(Integer cin) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SceanceConduite> sceanceConduites = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_conduite WHERE candidat_id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, cin);
            rs = ps.executeQuery();

            while (rs.next()) {
                SceanceConduite sceanceConduite = new SceanceConduite();
                sceanceConduite.setSceanceId(rs.getInt("idSeanceConduite"));
                sceanceConduite.setCondidatId(rs.getInt("candidat_id"));
                sceanceConduite.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceConduite.setSceanceDate(rs.getTimestamp("date_time").toLocalDateTime());
                sceanceConduite.setPrice(rs.getFloat("price"));
                sceanceConduite.setMoniteurId(rs.getInt("moniteur_id"));
                sceanceConduite.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                sceanceConduite.setMatriculeVehicule(rs.getInt("matricule_vehicule"));
                sceanceConduite.setLocationX(rs.getFloat("locationX"));
                sceanceConduite.setLocationY(rs.getFloat("locationY"));
                sceanceConduite.setLocationName(rs.getString("locationName"));

                sceanceConduites.add(sceanceConduite);
            }
        } catch (Exception e) {
            System.out.println("Error in getSceanceListByCandidat: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getSceanceListByCandidat: " + e.getMessage());
            }
        }
        return sceanceConduites;
    }
    public LocalDateTime getLastSeanceDateTime(Integer cin, Integer nbr) {
        List<SceanceConduite> sceanceConduites = getSceancesByCandidat(cin);

        // Check if the list is empty
        if (sceanceConduites.isEmpty()) {
            System.out.println("No seances found for candidat with CIN: " + cin);
            return null; // or throw an exception based on your design
        }

        // Sort the list by seance date in ascending order
        List<LocalDateTime> sortedDates = sceanceConduites.stream()
                .map(SceanceConduite::getSceanceDate) // Get the seance date from each SceanceConduite
                .sorted() // Sort in ascending order
                .collect(Collectors.toList());

        // Check if the requested nbr is within the bounds of the list
        if (nbr <= 0 || nbr > sortedDates.size()) {
            return null;
        }

        // Return the nbr-th last date (1-based index)
        return sortedDates.get(sortedDates.size() - nbr); // Get the nbr-th last date
    }

}

