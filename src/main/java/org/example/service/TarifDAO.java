package org.example.service;

import org.example.entities.Tarifs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifDAO {
    // Remove the connection field from the class

    public TarifDAO() {
        // Empty constructor - we'll get a fresh connection for each method call
    }

    public boolean update(Tarifs tarifs) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Get a fresh connection for each method call
            connection = ConxDB.getInstance();
            if (connection == null) {
                System.err.println("Error: Unable to establish database connection in update method");
                return false;
            }

            String query = "UPDATE Tarif SET codevoiture=?, codemoto=?, codecamion=?, conduitvoiture=?, conduitmoto=?, conduitcamion=?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, tarifs.getCodeSessionPricevoiture());
            pstmt.setInt(2, tarifs.getCodeSessionPricemoto());
            pstmt.setInt(3, tarifs.getCodeSessionPricecamion());
            pstmt.setInt(4, tarifs.getDrivingSessionPricevoiture());
            pstmt.setInt(5, tarifs.getDrivingSessionPricemoto());
            pstmt.setInt(6, tarifs.getDrivingSessionPricecamion());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("SQL Error in update: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            // Don't close the connection - ConxDB manages it as a singleton
        }
    }

    public Tarifs getTarifs() {
        Tarifs tarif = null;
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Get a fresh connection for this method call
            connection = ConxDB.getInstance();
            if (connection == null) {
                System.err.println("Error: Unable to establish database connection in getTarifs method");
                return null;
            }

            String query = "SELECT * FROM tarif";
            pst = connection.prepareStatement(query);
            rs = pst.executeQuery();

            if (rs.next()) {
                tarif = new Tarifs();
                tarif.setCodeSessionPricevoiture(rs.getInt("codevoiture"));
                tarif.setCodeSessionPricemoto(rs.getInt("codemoto"));
                tarif.setCodeSessionPricecamion(rs.getInt("codecamion"));
                tarif.setDrivingSessionPricevoiture(rs.getInt("conduitvoiture"));
                tarif.setDrivingSessionPricemoto(rs.getInt("conduitmoto"));
                tarif.setDrivingSessionPricecamion(rs.getInt("conduitcamion"));

                System.out.println("Tarifs loaded successfully: " + tarif);
                return tarif;
            } else {
                System.out.println("No tarifs found in database");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error in getTarifs: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                // Don't close the connection - ConxDB manages it
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}