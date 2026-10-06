package org.example.service;

import org.example.entities.Maintenance;
import org.example.service.myInterfaces.DBservices;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceServices implements DBservices<Maintenance> {

    @Override
    public List<Maintenance> getAll() {
        Connection cnx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cnx = ConxDB.getInstance();
            st = cnx.createStatement();
            String query = "select * from maintenances";
            rs = st.executeQuery(query);
            List<Maintenance> maintenances = new ArrayList<>();
            while (rs.next()){
                Maintenance maintenance = new Maintenance();
                maintenance.setId(rs.getInt("id"));
                maintenance.setMatricule(rs.getInt("matricule"));
                // try/catch dans le cas ou getDate retun null (null.toLocalDate va creer un exception)
                try {
                    maintenance.setDateMaintenance(rs.getDate("date_maintenance").toLocalDate());
                }catch (Exception e){
                    maintenance.setDateMaintenance(null);
                }
                maintenance.setDescription(rs.getString("type_maintenance"));
                maintenance.setPrice(rs.getFloat("price"));
                maintenances.add(maintenance);
            }
            return maintenances;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
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

    @Override
    public Maintenance getById(Integer id) {
        Connection cnx = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            cnx = ConxDB.getInstance();
            st = cnx.createStatement();
            String query = "select * from maintenances where id=" + id;
            rs = st.executeQuery(query);
            if (rs.next()) {
                Maintenance maintenance = new Maintenance();
                maintenance.setId(rs.getInt("id"));
                maintenance.setMatricule(rs.getInt("matricule"));
                try {
                    maintenance.setDateMaintenance(rs.getDate("date_maintenance").toLocalDate());
                }catch (Exception e){
                    maintenance.setDateMaintenance(null);
                }
                maintenance.setDescription(rs.getString("type_maintenance"));
                maintenance.setPrice(rs.getFloat("price"));
                return maintenance;
            }
            return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private String createMaintenanceDirectory(int matricule) {
        String reposPath = "src/main/java/org/example/repository/vehicules/" + matricule + "/maintenance";
        File directory = new File(reposPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created: " + reposPath);
            } else {
                System.out.println("Failed to create directory: " + reposPath);
            }
        } else {
            System.out.println("Directory already exists: " + reposPath);
        }
        return reposPath;
    }

    @Override
    public boolean create(Maintenance maintenance) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO maintenances (matricule, date_maintenance, type_maintenance, price, repos) VALUES (?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setInt(1, maintenance.getMatricule());

            if (maintenance.getDateMaintenance() == null) {
                ps.setNull(2, java.sql.Types.DATE);
            } else {
                ps.setDate(2, java.sql.Date.valueOf(maintenance.getDateMaintenance()));
            }

            ps.setString(3, maintenance.getDescription());

            if (maintenance.getPrice() == null) {
                ps.setNull(4, java.sql.Types.FLOAT);
            } else {
                ps.setFloat(4, maintenance.getPrice());
            }

            // Créer le répertoire et définir le chemin repos
            String reposPath = createMaintenanceDirectory(maintenance.getMatricule());
            ps.setString(5, reposPath);

            System.out.println("query good");
            if (ps.executeUpdate() > 0) {
                System.out.println("Maintenance created");
                return true;
            } else {
                System.out.println("Maintenance not created st issue");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Maintenance not created exp issue: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean update(Integer id, Maintenance obj) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE maintenances SET matricule = ?, date_maintenance = ?, type_maintenance = ?, price = ?, repos = ? WHERE id = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, obj.getMatricule());
            if (obj.getDateMaintenance() == null) {
                ps.setNull(2, java.sql.Types.DATE);
            } else {
                ps.setDate(2, java.sql.Date.valueOf(obj.getDateMaintenance()));
            }
            ps.setString(3, obj.getDescription());
            if (obj.getPrice() == null) {
                ps.setNull(4, java.sql.Types.FLOAT);
            } else {
                ps.setFloat(4, obj.getPrice());
            }
            String reposPath = createMaintenanceDirectory(obj.getMatricule());
            ps.setString(5, reposPath);

            ps.setInt(6, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Maintenance updated");
                return true;
            } else {
                System.out.println("Maintenance not updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating maintenance: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }



    @Override
    public boolean delete(Integer id) {
        Connection cnx=null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "DELETE FROM maintenances WHERE id = ?";
            ps=cnx.prepareStatement(query);
            ps.setInt(1,id);
            if (ps.executeUpdate() == 1) {
                System.out.println("Maintenance deleted");
                return true;
            }else {
                System.out.println("Maintenance not deleted");
                return false;
            }
        }catch (Exception e){
            System.out.println("Error deleting maintenance: " + e.getMessage());
            return false;
        }finally {
            try {
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
