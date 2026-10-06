package org.example.service;

import org.example.entities.Employee;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.myInterfaces.DBservices;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MoniteurService implements DBservices<Employee> {
    @Override
    public List<Employee> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Employee> Moniteurs = new ArrayList<>();
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM moniteur";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Employee Moni = new Employee();
                Moni.setIdMoniteur(rs.getInt("idMoniteur"));
                Moni.setNom(rs.getString("nom"));
                Moni.setPrenom(rs.getString("prenom"));
                Moni.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());
                Moni.setEmail(rs.getString("email"));
                Moni.setTelephone(rs.getInt("telephone"));
                Moni.setAdresse(rs.getString("adresse"));
                Moni.setVehiculeType(VehiculeType.valueOf(rs.getString("vehiculeType").toUpperCase()));
                Moni.setSalaire(rs.getFloat("salaire"));
                Moniteurs.add(Moni);
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
        return Moniteurs;

    }

    @Override
    public Employee getById(Integer id) {

        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Employee Moni = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM moniteur WHERE idMoniteur = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Moni = new Employee();
                Moni.setIdMoniteur(id);
                Moni.setNom(rs.getString("nom"));
                Moni.setPrenom(rs.getString("prenom"));
                Moni.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());
                Moni.setEmail(rs.getString("email"));
                Moni.setTelephone(rs.getInt("telephone"));
                Moni.setAdresse(rs.getString("adresse"));
                Moni.setVehiculeType(VehiculeType.valueOf(rs.getString("vehiculeType").toUpperCase()));
                Moni.setSalaire(rs.getFloat("salaire"));

            }
        } catch (Exception e) {
            System.out.println("Error in getById: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getById: " + e.getMessage());
            }
        }
        return Moni;

    }

    @Override
    public boolean create(Employee obj) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO  moniteur VALUES (?,?, ?, ?, ?, ?, ?, ?,?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setInt(1, obj.getIdMoniteur());
            ps.setString(2, obj.getNom());
            ps.setString(3, obj.getPrenom());
            if (obj.getDateNaissance() == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(obj.getDateNaissance()));
            }
            ps.setString(5, obj.getEmail());
            ps.setInt(6, obj.getTelephone());
            ps.setString(7, obj.getAdresse());
            ps.setString(8, obj.getVehiculeType().toString().toLowerCase());
            ps.setFloat(9, obj.getSalaire());


            System.out.println("query good");
            if(ps.executeUpdate() > 0) {
                System.out.println("Moniture created");
                return true;
            } else {
                System.out.println("Moniture not created, statement issue");
                return false;
            }
        } catch(Exception e) {
            System.out.println("Moniture not created, exception issue: " + e.getMessage());
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
    public boolean update(Integer id, Employee obj) {
        //id is matricule
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE moniteur SET  nom = ?, prenom = ?, dateNaissance = ?, email = ?, telephone = ? ,adresse=?, vehiculeType=? ,salaire=?  WHERE idMoniteur = ?";
            ps = cnx.prepareStatement(query);
            ps.setString(1, obj.getNom());
            ps.setString(2, obj.getPrenom());
            if (obj.getDateNaissance() == null) {
                ps.setNull(3, java.sql.Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(obj.getDateNaissance()));
            }
            ps.setString(4, obj.getEmail());
            ps.setInt(5, obj.getTelephone());
            ps.setString(6, obj.getAdresse());
            ps.setString(7, obj.getVehiculeType().toString().toLowerCase());
            ps.setFloat(8, obj.getSalaire());
            ps.setInt(9, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Employee updated");

                return true;
            } else {
                System.out.println("Employee not updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating Employee: " + e.getMessage());
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
            String query = "DELETE FROM moniteur WHERE idMoniteur = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if(ps.executeUpdate() == 1) {
                System.out.println("Employee deleted");
                return true;
            } else {
                System.out.println("Employee not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting Employee: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();

            } catch (Exception e) {
                System.out.println("Error closing resources in delete: " + e.getMessage());
            }
        }
    }
}
