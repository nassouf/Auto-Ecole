package org.example.service.verification;

import org.example.entities.exceptions.WrongSecurityCodeException;
import org.example.service.ConxDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupLoginServices {
    public static boolean login(String username, String password) {
        Connection cnx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cnx = ConxDB.getInstance();
            st = cnx.createStatement();
            String query = "select count(*) from users where binary user_name = '" + username + "' and binary password = '" + password + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
                return false;
            }
            return false;

        } catch (Exception e) {
            return false;
        }finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    public static boolean signup(String username, String password, String securityCode) {
        Connection cnx = null;
        Statement st = null;
        int rs;
        try {
            if (!(securityCode.equalsIgnoreCase("abc123"))) throw new WrongSecurityCodeException();
            cnx = ConxDB.getInstance();
            st=cnx.createStatement();
            String querry="insert into users values('"+username+"','"+password+"') ";
            rs=st.executeUpdate(querry);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }finally {
            try {
                //if (rs != null) rs.close();
                if (st != null) st.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
