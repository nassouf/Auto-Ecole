package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.example.service.ConxDB;

public class DashboardDAO {

    private Connection connection;

    public DashboardDAO() {
        connection = ConxDB.getInstance();
    }

    public String getLogoPath() {
        String logoPath = null;
        try {
            String query = "SELECT logo FROM autoecoleinfo LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logoPath = rs.getString("logo");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logoPath;
    }

    public double getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        double totalRevenue = 0;
        try {
            String query = "SELECT SUM(montant) as total FROM paiements WHERE date_paiement BETWEEN ? AND ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalRevenue = rs.getDouble("total");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    public int getTotalCandidates() {
        int totalCandidates = 0;
        try {
            String query = "SELECT COUNT(*) as total FROM candidat";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalCandidates = rs.getInt("total");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCandidates;
    }

    public double getSuccessRate(LocalDate startDate, LocalDate endDate) {
        double successRate = 0;
        try {
            // Query for total exams
            String totalExamsQuery = "SELECT COUNT(*) as total FROM examen_conduite WHERE date_time BETWEEN ? AND ?";
            PreparedStatement totalPs = connection.prepareStatement(totalExamsQuery);
            totalPs.setDate(1, java.sql.Date.valueOf(startDate));
            totalPs.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet totalRs = totalPs.executeQuery();

            int totalExams = 0;
            if (totalRs.next()) {
                totalExams = totalRs.getInt("total");
            }
            totalRs.close();
            totalPs.close();

            // Query for successful exams
            String successExamsQuery = "SELECT COUNT(*) as success FROM examen_conduite WHERE status = 1 AND date_time BETWEEN ? AND ?";
            PreparedStatement successPs = connection.prepareStatement(successExamsQuery);
            successPs.setDate(1, java.sql.Date.valueOf(startDate));
            successPs.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet successRs = successPs.executeQuery();

            int successExams = 0;
            if (successRs.next()) {
                successExams = successRs.getInt("success");
            }
            successRs.close();
            successPs.close();

            // Calculate success rate
            successRate = totalExams > 0 ? (double) successExams / totalExams * 100 : 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return successRate;
    }

    public int getTotalSessions(LocalDate startDate, LocalDate endDate) {
        int totalSessions = 0;
        try {
            // Count total sessions (code + driving)
            String query = "SELECT " +
                    "(SELECT COUNT(*) FROM seances_code WHERE date BETWEEN ? AND ?) + " +
                    "(SELECT COUNT(*) FROM seances_conduite WHERE date_time BETWEEN ? AND ?) as total";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ps.setDate(3, java.sql.Date.valueOf(startDate));
            ps.setDate(4, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalSessions = rs.getInt("total");
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalSessions;
    }

    public ObservableList<PieChart.Data> getRevenusByLicenseType(LocalDate startDate, LocalDate endDate) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            // Get revenue by license type
            String query = "SELECT c.typePermis, SUM(p.montant) as total " +
                    "FROM paiements p " +
                    "JOIN candidat c ON p.candidat_id = c.CIN " +
                    "WHERE p.date_paiement BETWEEN ? AND ? " +
                    "GROUP BY c.typePermis";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String licenseType = rs.getString("typePermis");
                double total = rs.getDouble("total");

                // Convert license type to French
                String displayType = "Autre";
                if ("voiture".equals(licenseType)) displayType = "Voiture";
                else if ("moto".equals(licenseType)) displayType = "Moto";
                else if ("camion".equals(licenseType)) displayType = "Camion";

                pieChartData.add(new PieChart.Data(displayType + " (" + String.format("%.2f DT", total) + ")", total));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pieChartData;
    }

    public XYChart.Series<String, Number> getSuccessRateByLicenseType(LocalDate startDate, LocalDate endDate) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Taux de réussite");

        try {
            // Load success rates by license type
            String query = "SELECT type, " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as success " +
                    "FROM examen_conduite " +
                    "WHERE date_time BETWEEN ? AND ? " +
                    "GROUP BY type";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String licenseType = rs.getString("type");
                int total = rs.getInt("total");
                int success = rs.getInt("success");

                double rate = total > 0 ? (double) success / total * 100 : 0;

                // Convert license type to French
                String displayType = "Autre";
                if ("voiture".equals(licenseType)) displayType = "Voiture";
                else if ("moto".equals(licenseType)) displayType = "Moto";
                else if ("camion".equals(licenseType)) displayType = "Camion";

                series.getData().add(new XYChart.Data<>(displayType, rate));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    public XYChart.Series<String, Number> getVehicleUsageData(String vehicleType, LocalDate startDate, LocalDate endDate) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre de séances");

        try {
            // Query to get vehicle usage (number of sessions per vehicle)
            String query = "SELECT v.matricule, COUNT(sc.idSeanceConduite) as sessionCount " +
                    "FROM vehicules v " +
                    "LEFT JOIN seances_conduite sc ON v.matricule = sc.matricule_vehicule " +
                    "WHERE v.type = ? AND (sc.date_time IS NULL OR sc.date_time BETWEEN ? AND ?) " +
                    "GROUP BY v.matricule " +
                    "ORDER BY sessionCount DESC " +
                    "LIMIT 10";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, vehicleType);
            ps.setDate(2, java.sql.Date.valueOf(startDate));
            ps.setDate(3, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String matricule = rs.getString("matricule");
                int sessionCount = rs.getInt("sessionCount");

                series.getData().add(new XYChart.Data<>(matricule, sessionCount));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    public XYChart.Series<String, Number> getPaymentsData(LocalDate startDate, LocalDate endDate) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Montant total");

        Map<String, String> monthNames = new HashMap<>();
        monthNames.put("01", "Jan");
        monthNames.put("02", "Fév");
        monthNames.put("03", "Mar");
        monthNames.put("04", "Avr");
        monthNames.put("05", "Mai");
        monthNames.put("06", "Jun");
        monthNames.put("07", "Jui");
        monthNames.put("08", "Aoû");
        monthNames.put("09", "Sep");
        monthNames.put("10", "Oct");
        monthNames.put("11", "Nov");
        monthNames.put("12", "Déc");

        try {
            // Query to get payments by month
            String query = "SELECT DATE_FORMAT(date_paiement, '%Y-%m') as month, " +
                    "SUM(montant) as total " +
                    "FROM paiements " +
                    "WHERE date_paiement BETWEEN ? AND ? " +
                    "GROUP BY month " +
                    "ORDER BY month";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total");

                // Format month for display
                String yearMonth = month.split("-")[1];
                String displayMonth = monthNames.getOrDefault(yearMonth, yearMonth);

                series.getData().add(new XYChart.Data<>(displayMonth, total));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }


}