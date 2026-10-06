package org.example.service;

import org.example.entities.SceanceCode;
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

public class SceanceCodeServices implements DBservices<SceanceCode> {

    @Override
    public List<SceanceCode> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SceanceCode> sceanceCodes = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_code";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                SceanceCode sceanceCode=new SceanceCode();
                sceanceCode.setSceanceId(rs.getInt("id"));
                sceanceCode.setCondidatId(rs.getInt("candidat_id"));
                sceanceCode.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceCode.setSceanceDate(rs.getTimestamp("date").toLocalDateTime());
                sceanceCode.setPrice(rs.getFloat("price"));
                sceanceCode.setIngenieurId(rs.getInt("ingenieur_id"));
                sceanceCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));

                sceanceCodes.add(sceanceCode);
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
        return sceanceCodes;
    }
    @Override
    public SceanceCode getById(Integer id) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SceanceCode sceanceCode = null;
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_code WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                sceanceCode=new SceanceCode();
                sceanceCode.setSceanceId(rs.getInt("id"));
                sceanceCode.setCondidatId(rs.getInt("candidat_id"));
                sceanceCode.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceCode.setSceanceDate(rs.getTimestamp("date").toLocalDateTime());
                sceanceCode.setPrice(rs.getFloat("price"));
                sceanceCode.setIngenieurId(rs.getInt("ingenieur_id"));
                sceanceCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
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
        return sceanceCode;
    }

    @Override
    public boolean create(SceanceCode sceanceCode) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO seances_code (type,date,duree,ingenieur_id,candidat_id,price) VALUES (?, ?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1,sceanceCode.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(sceanceCode.getSceanceDate()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(sceanceCode.getDuration())));
            ps.setInt(4,sceanceCode.getIngenieurId());
            ps.setInt(5,sceanceCode.getCondidatId());
            ps.setFloat(6,sceanceCode.getPrice());

            System.out.println("query good");
            if(ps.executeUpdate() > 0) {
                System.out.println("SceanceCode created");

                return true;
            } else {
                System.out.println("SceanceCode not created, statement issue");
                return false;
            }
        } catch(Exception e) {
            System.out.println("SceanceCode not created, exception issue: " + e.getMessage());
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
    public boolean update(Integer id, SceanceCode sceanceCode) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE seances_code SET type = ?, date = ?, duree = ?, ingenieur_id = ?, candidat_id = ?, price = ? WHERE id = ?";

            // Initialize ps with the query
            ps = cnx.prepareStatement(query);

            // Now you can set the parameters
            ps.setString(1, sceanceCode.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(sceanceCode.getSceanceDate()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(sceanceCode.getDuration())));
            ps.setInt(4, sceanceCode.getIngenieurId());
            ps.setInt(5, sceanceCode.getCondidatId());
            ps.setFloat(6, sceanceCode.getPrice());
            ps.setInt(7, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("scCode updated");
                return true;
            } else {
                System.out.println("scCode not updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating scCode: " + e.getMessage());
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
            String query = "DELETE FROM seances_code WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                System.out.println("sceanceCode deleted");
                return true;
            } else {
                System.out.println("seanceCode not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting sceanceCode: " + e.getMessage());
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
    public List<SceanceCode> getSceancesByCandidat(Integer cin) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SceanceCode> sceanceCodes = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM seances_code WHERE candidat_id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, cin);
            rs = ps.executeQuery();

            while (rs.next()) {
                SceanceCode sceanceCode = new SceanceCode();
                sceanceCode.setSceanceId(rs.getInt("id"));
                sceanceCode.setCondidatId(rs.getInt("candidat_id"));
                sceanceCode.setDuration(timeToDuration.transform(rs.getTime("duree")));
                sceanceCode.setSceanceDate(rs.getTimestamp("date").toLocalDateTime());
                sceanceCode.setPrice(rs.getFloat("price"));
                sceanceCode.setIngenieurId(rs.getInt("ingenieur_id"));
                sceanceCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));

                sceanceCodes.add(sceanceCode);
            }
        } catch (Exception e) {
            System.out.println("Error in getSceancesByCandidat: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getSceancesByCandidat: " + e.getMessage());
            }
        }
        return sceanceCodes;
    }
    public LocalDateTime getLastSeanceDateTime(Integer cin, Integer nbr) {
        List<SceanceCode> sceanceCodes = getSceancesByCandidat(cin);

        // Check if the list is empty
        if (sceanceCodes.isEmpty()) {
            System.out.println("No seances found for candidat with CIN: " + cin);
            return null; // or throw an exception based on your design
        }

        // Sort the list by seance date in ascending order
        List<LocalDateTime> sortedDates = sceanceCodes.stream()
                .map(SceanceCode::getSceanceDate) // Get the seance date from each SceanceCode
                .sorted() // Sort in ascending order
                .collect(Collectors.toList());

        // Check if the requested nbr is within the bounds of the list
        if (nbr <= 0 || nbr > sortedDates.size()) {
            // If nbr is out of bounds
            return null;
        }

        // Return the nbr-th last date (1-based index)
        return sortedDates.get(sortedDates.size() - nbr); // Get the nbr-th last date
    }

}
