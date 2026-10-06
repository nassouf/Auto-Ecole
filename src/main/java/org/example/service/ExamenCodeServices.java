package org.example.service;

import org.example.entities.ExamenCode;
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

public class ExamenCodeServices implements DBservices<ExamenCode> {
    @Override
    public List<ExamenCode> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ExamenCode> examenCodes = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_code";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ExamenCode examenCode = new ExamenCode();
                examenCode.setId(rs.getInt("id"));
                examenCode.setIdCandidat(rs.getInt("candidat_id"));
                examenCode.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenCode.setDateExamen(rs.getTimestamp("date").toLocalDateTime());
                examenCode.setPrix(rs.getFloat("price"));
                examenCode.setIdIngenieur(rs.getInt("ingenieur_id"));
                examenCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenCode.setStatus(rs.getBoolean("status"));
                examenCodes.add(examenCode);
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
        return examenCodes;
    }

    @Override
    public ExamenCode getById(Integer id) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ExamenCode examenCode = null;
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_code WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                examenCode = new ExamenCode();
                examenCode.setId(rs.getInt("id"));
                examenCode.setIdCandidat(rs.getInt("candidat_id"));
                examenCode.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenCode.setDateExamen(rs.getTimestamp("date").toLocalDateTime());
                examenCode.setPrix(rs.getFloat("price"));
                examenCode.setIdIngenieur(rs.getInt("ingenieur_id"));
                examenCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenCode.setStatus(rs.getBoolean("status"));
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
        return examenCode;
    }

    @Override
    public boolean create(ExamenCode examenCode) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO examen_code (type,date,duree,ingenieur_id,candidat_id,price,status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1, examenCode.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(examenCode.getDateExamen()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(examenCode.getDurationExamen())));
            ps.setInt(4, examenCode.getIdIngenieur());
            ps.setInt(5, examenCode.getIdCandidat());
            ps.setFloat(6, examenCode.getPrix());
            ps.setBoolean(7, examenCode.getStatus());

            System.out.println("query good");
            if (ps.executeUpdate() > 0) {
                System.out.println("ExamenCode created");
                return true;
            } else {
                System.out.println("ExamenCode not created, statement issue");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ExamenCode not created, exception issue: " + e.getMessage());
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
    public boolean update(Integer id, ExamenCode examenCode) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE examen_code SET type = ?, date = ?, duree = ?, ingenieur_id = ?, candidat_id = ?, price = ?, status = ? WHERE id = ?";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setString(1, examenCode.getVehiculeType().toString().toLowerCase());
            ps.setTimestamp(2, Timestamp.valueOf(examenCode.getDateExamen()));
            ps.setTime(3, Time.valueOf(LocalTime.MIDNIGHT.plus(examenCode.getDurationExamen())));
            ps.setInt(4, examenCode.getIdIngenieur());
            ps.setInt(5, examenCode.getIdCandidat());
            ps.setFloat(6, examenCode.getPrix());
            ps.setBoolean(7, examenCode.getStatus());
            ps.setInt(8, id); // Set the id for the WHERE clause

            System.out.println("query good");
            if (ps.executeUpdate() > 0) {
                System.out.println("ExamenCode updated");
                return true;
            } else {
                System.out.println("ExamenCode not updated, statement issue");
                return false;
            }
        } catch (Exception e) {
            System.out.println("ExamenCode not updated, exception issue: " + e.getMessage());
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
            String query = "DELETE FROM examen_code WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if (ps.executeUpdate() == 1) {
                System.out.println("ExamenCode deleted");
                return true;
            } else {
                System.out.println("ExamenCode not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting ExamenCode: " + e.getMessage());
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
    public List<ExamenCode> getExamensByCandidat(Integer candidatId) {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ExamenCode> examenCodes = new ArrayList<>();
        TimeToDuration timeToDuration = t -> Duration.ofMinutes(t.getHours() * 60 + t.getMinutes());

        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM examen_code WHERE candidat_id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, candidatId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExamenCode examenCode = new ExamenCode();
                examenCode.setId(rs.getInt("id"));
                examenCode.setIdCandidat(rs.getInt("candidat_id"));
                examenCode.setDurationExamen(timeToDuration.transform(rs.getTime("duree")));
                examenCode.setDateExamen(rs.getTimestamp("date").toLocalDateTime());
                examenCode.setPrix(rs.getFloat("price"));
                examenCode.setIdIngenieur(rs.getInt("ingenieur_id"));
                examenCode.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                examenCode.setStatus(rs.getBoolean("status"));

                examenCodes.add(examenCode);
            }
        } catch (Exception e) {
            System.out.println("Error in getExamenListByCandidat: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getExamenListByCandidat: " + e.getMessage());
            }
        }
        return examenCodes;
    }

    public LocalDateTime getLastExamenDateTime(Integer candidatId, Integer nbr) {
        List<ExamenCode> examenCodes = getExamensByCandidat(candidatId);

        // Check if the list is empty
        if (examenCodes.isEmpty()) {
            System.out.println("No examens found for candidat with ID: " + candidatId);
            return null; // or throw an exception based on your design
        }

        // Sort the list by examen date in ascending order
        List<LocalDateTime> sortedDates = examenCodes.stream()
                .map(ExamenCode::getDateExamen) // Get the examen date from each ExamenCode
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
