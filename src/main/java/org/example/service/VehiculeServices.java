package org.example.service;

import org.example.entities.Maintenance;
import org.example.entities.Vehicule;
import org.example.entities.interfaces.TimeToDuration;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.myInterfaces.DBservices;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class VehiculeServices implements DBservices<Vehicule> {

    @Override
    public List<Vehicule> getAll() {
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TimeToDuration timeToDuration=t-> Duration.ofMinutes(t.getHours()*60+t.getMinutes());
        List<Vehicule> vehicules = new ArrayList<>();
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM vehicules";
            ps = cnx.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Vehicule vehicule = new Vehicule();
                vehicule.setMatricule(rs.getInt("matricule"));
                vehicule.setMarque(rs.getString("marque"));
                vehicule.setDateAssurance(rs.getDate("date_assurance").toLocalDate());
                vehicule.setPrixAssurance(rs.getFloat("prix_assurance"));
                vehicule.setStatus(rs.getInt("status"));
                vehicule.setKilometer(rs.getFloat("kilometer"));
                vehicule.setNbrKilometerRestant(rs.getFloat("kilometer_restant"));
                vehicule.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                vehicule.setDocuments(rs.getString("documents"));
                vehicule.setFactures(rs.getString("factures"));
                vehicules.add(vehicule);
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
        vehicules.forEach(v->createVehiculeFolder(v.getMatricule()));// in case a folder gets deleted ;)
        return vehicules;
    }

    @Override
    public Vehicule getById(Integer id) {
        //id is matricule
        Connection cnx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vehicule vehicule = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "SELECT * FROM vehicules WHERE matricule = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                vehicule = new Vehicule();
                vehicule.setMatricule(id);
                vehicule.setMarque(rs.getString("marque"));
                vehicule.setDateAssurance(rs.getDate("date_assurance").toLocalDate());
                vehicule.setPrixAssurance(rs.getFloat("prix_assurance"));
                vehicule.setStatus(rs.getInt("status"));
                vehicule.setKilometer(rs.getFloat("kilometer"));
                vehicule.setNbrKilometerRestant(rs.getFloat("kilometer_restant"));
                vehicule.setVehiculeType(VehiculeType.valueOf(rs.getString("type").toUpperCase()));
                vehicule.setDocuments(rs.getString("documents"));
                vehicule.setFactures(rs.getString("factures"));
            }
        } catch (Exception e) {
            System.out.println("Error in getById: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                //if (cnx != null) cnx.close();
            } catch (Exception e) {
                System.out.println("Error closing resources in getById: " + e.getMessage());
            }
        }
        return vehicule;
    }

    @Override
    public boolean create(Vehicule vehicule) {
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "INSERT INTO vehicules (matricule, marque, kilometer, kilometer_restant, status, type, date_assurance, prix_assurance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = cnx.prepareStatement(query);
            System.out.println("init all good");

            ps.setInt(1, vehicule.getMatricule());
            ps.setString(2, vehicule.getMarque());
            ps.setFloat(3, vehicule.getKilometer());
            ps.setFloat(4, vehicule.getNbrKilometerRestant());
            ps.setInt(5, vehicule.getStatus());
            ps.setString(6, vehicule.getVehiculeType().toString().toLowerCase());
            if (vehicule.getDateAssurance() == null) {
                ps.setNull(7, java.sql.Types.DATE);
            } else {
                ps.setDate(7, Date.valueOf(vehicule.getDateAssurance()));
            }
            ps.setFloat(8, vehicule.getPrixAssurance());
            System.out.println("query good");
            if(ps.executeUpdate() > 0) {
                System.out.println("Vehicule created");
                vehiculeCheck(vehicule);//to set the correct the vehicle status according the the given data
                createVehiculeFolder(vehicule.getMatricule());
                return true;
            } else {
                System.out.println("Vehicule not created, statement issue");
                return false;
            }
        } catch(Exception e) {
            System.out.println("Vehicule not created, exception issue: " + e.getMessage());
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
    public boolean update(Integer id, Vehicule vehicule) {
        //id is matricule
        Connection cnx = null;
        PreparedStatement ps = null;
        try {
            cnx = ConxDB.getInstance();
            String query = "UPDATE vehicules SET marque = ?, date_assurance = ?, kilometer = ?, kilometer_restant = ?, status = ?, type = ? WHERE matricule = ?";
            ps = cnx.prepareStatement(query);
            ps.setString(1, vehicule.getMarque());
            if (vehicule.getDateAssurance() == null) {
                ps.setNull(2, java.sql.Types.DATE);
            } else {
                ps.setDate(2, Date.valueOf(vehicule.getDateAssurance()));
            }
            ps.setFloat(3, vehicule.getKilometer());
            ps.setFloat(4, vehicule.getNbrKilometerRestant());
            ps.setInt(5, vehicule.getStatus());
            ps.setString(6, vehicule.getVehiculeType().toString().toLowerCase());
            ps.setInt(7, id);

            if (ps.executeUpdate() == 1) {
                System.out.println("Vehicule updated");
                vehiculeCheck(vehicule);
                return true;
            } else {
                System.out.println("Vehicule not updated");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error updating vehicule: " + e.getMessage());
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
            String query = "DELETE FROM vehicules WHERE matricule = ?";
            ps = cnx.prepareStatement(query);
            ps.setInt(1, id);
            if(ps.executeUpdate() == 1) {
                System.out.println("Vehicule deleted");
                return true;
            } else {
                System.out.println("Vehicule not deleted");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error deleting vehicule: " + e.getMessage());
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
    public void vehiculeCheck(Vehicule vehicule) {
        if (vehicule.getNbrKilometerRestant()<=0){
            MaintenanceServices ms = new MaintenanceServices();
            Maintenance maintenance = new Maintenance();
            maintenance.setMatricule(vehicule.getMatricule());
            maintenance.setDateMaintenance(null);
            maintenance.setPrice(null);
            maintenance.setDescription("Oil change");
            ms.create(maintenance);
        }
    }
    public void createVehiculeFolder(int matricule) {
        // Get the current directory of the project
        String projectPath = new File("").getAbsolutePath();

        // Move up one level to reach the parent directory (where 'service' and 'repository' are located)
        File parentDir = new File(projectPath).getParentFile();

        // Define the base path for the vehicle folder
        String basePath = parentDir + File.separator+"ProjectJava"+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"org"+File.separator+"example"+File.separator + "repository" + File.separator + "vehicules" + File.separator + matricule;

        // Define the full paths for the two subfolders
        File documentsFolder = new File(basePath + File.separator + "documents");
        File facturesFolder = new File(basePath + File.separator + "factures");

        // Create the folders if they don't exist
        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs();
        }

        if (!facturesFolder.exists()) {
            facturesFolder.mkdirs();
        }
    }

}
