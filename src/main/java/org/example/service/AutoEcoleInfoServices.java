package org.example.service;

import org.example.entities.AutoEcoleInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AutoEcoleInfoServices {
    public boolean modify(AutoEcoleInfo autoEcoleInfo) {
        deleteAll();
        Connection cnx = null;
        PreparedStatement pst = null;
        try {
            cnx = ConxDB.getInstance();
            if (cnx == null) {
                System.err.println("Error: Unable to establish database connection in modify method");
                return false;
            }

            String querry = "insert into autoecoleinfo values(?,?,?,?,?,?,?)";
            pst = cnx.prepareStatement(querry);
            System.out.println("init good");
            pst.setString(1, autoEcoleInfo.getAutoEcoleName());
            pst.setString(2, autoEcoleInfo.getAutoEcoleDescription());
            pst.setInt(3, autoEcoleInfo.getAutoEcolePhone());
            pst.setString(4, autoEcoleInfo.getAutoEcoleWebsite());
            pst.setString(5, autoEcoleInfo.getAutoEcoleEmail());
            pst.setString(6, autoEcoleInfo.getAutoEcoleAddress());
            pst.setString(7, autoEcoleInfo.getAutoEcolelogo());
            System.out.println("all good");
            if (pst.executeUpdate() > 0) {
                System.out.println("success");
                return true;
            } else {
                System.out.println("Operation failed");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error in modify: " + e);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                // Don't close the connection - ConxDB manages it
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e);
            }
        }
    }

    public AutoEcoleInfo getAutoEcoleInfo(String autoEcoleName) {
        AutoEcoleInfo autoEcoleInfo = null;
        Connection cnx = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            cnx = ConxDB.getInstance();
            if (cnx == null) {
                System.err.println("Error: Unable to establish database connection in getAutoEcoleInfo method");
                return null;
            }

            String querry = "select * from autoecoleinfo";
            pst = cnx.prepareStatement(querry);
            rs = pst.executeQuery();
            if (rs.next()) {
                autoEcoleInfo = new AutoEcoleInfo();
                autoEcoleInfo.setAutoEcoleName(rs.getString("name"));
                autoEcoleInfo.setAutoEcoleDescription(rs.getString("description"));
                autoEcoleInfo.setAutoEcolePhone(rs.getInt("phoneNumber"));
                autoEcoleInfo.setAutoEcoleWebsite(rs.getString("website"));
                autoEcoleInfo.setAutoEcoleEmail(rs.getString("email"));
                autoEcoleInfo.setAutoEcoleAddress(rs.getString("address"));
                autoEcoleInfo.setAutoEcolelogo(rs.getString("logo"));

                System.out.println(autoEcoleInfo);
                return autoEcoleInfo;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error in getAutoEcoleInfo: " + e);
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
                System.err.println("Error closing resources: " + e);
            }
        }
    }

    private void deleteAll() {
        Connection cnx = null;
        PreparedStatement pst = null;
        try {
            cnx = ConxDB.getInstance();
            if (cnx == null) {
                System.err.println("Error: Unable to establish database connection in deleteAll method");
                return;
            }

            String querry = "delete from autoecoleinfo";
            pst = cnx.prepareStatement(querry);
            int rowsAffected = pst.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " rows from autoecoleinfo");
        } catch (Exception e) {
            System.err.println("Error in deleteAll: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                // Don't close the connection - ConxDB manages it
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e);
            }
        }
    }
}