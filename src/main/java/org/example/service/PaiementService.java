package org.example.service;

import org.example.entities.Candidat;
import org.example.entities.Paiement;
import org.example.service.myInterfaces.DBservices;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PaiementService implements DBservices<Paiement> {

    private CandidatService candidatService;
    private PDFGenerator pdfGenerator;

    public PaiementService() {
        this.candidatService = new CandidatService();
        this.pdfGenerator = new PDFGenerator();
    }

    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to get a fresh connection and validate it
     */
    private Connection getValidConnection() throws SQLException {
        Connection conn = ConxDB.getInstance();
        if (conn == null || conn.isClosed()) {
            // Force ConxDB to create a new connection by setting connection to null
            // This is a workaround since we can't modify ConxDB
            try {
                java.lang.reflect.Field field = ConxDB.class.getDeclaredField("connexion");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = ConxDB.getInstance();
        }

        // Verify the connection is valid
        if (!conn.isValid(5)) { // 5 second timeout
            // Force ConxDB to create a new connection
            try {
                java.lang.reflect.Field field = ConxDB.class.getDeclaredField("connexion");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = ConxDB.getInstance();
        }

        return conn;
    }

    @Override
    public List<Paiement> getAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement paiement = extractPaiementFromResultSet(rs);
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return paiements;
    }

    @Override
    public Paiement getById(Integer id) {
        String sql = "SELECT * FROM paiements WHERE id = ?";
        Paiement paiement = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                paiement = extractPaiementFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return paiement;
    }

    public List<Paiement> getPaiementsByCandidat(Integer candidatId) {
        String sql = "SELECT * FROM paiements WHERE candidat_id = ?";
        List<Paiement> paiements = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, candidatId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement paiement = extractPaiementFromResultSet(rs);
                paiements.add(paiement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return paiements;
    }

    @Override
    public boolean create(Paiement paiement) {
        String sql = "INSERT INTO paiements (candidat_id, montant, date_paiement, etat, recu, is_facilite, numero_tranche) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, paiement.getCandidatId());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getEtat());
            stmt.setBytes(5, paiement.getRecu());
            stmt.setBoolean(6, paiement.isFacilite());
            stmt.setInt(7, paiement.getNumeroTranche());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    paiement.setId(rs.getInt(1));
                }

                // Mettre à jour le montant restant pour le candidat
                if ("fait".equals(paiement.getEtat())) {
                    updateCandidatMontantAPayer(paiement.getCandidatId(), paiement.getMontant());
                }

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return false;
    }

    @Override
    public boolean update(Integer id, Paiement paiement) {
        Paiement oldPaiement = getById(id);
        if (oldPaiement == null) return false;

        String oldEtat = oldPaiement.getEtat();
        String sql = "UPDATE paiements SET candidat_id=?, montant=?, date_paiement=?, etat=?, recu=?, is_facilite=?, numero_tranche=? WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, paiement.getCandidatId());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getEtat());
            stmt.setBytes(5, paiement.getRecu());
            stmt.setBoolean(6, paiement.isFacilite());
            stmt.setInt(7, paiement.getNumeroTranche());
            stmt.setInt(8, id);

            int result = stmt.executeUpdate();

            // Si le statut est passé de "en attente" à "fait", mettre à jour le montant du candidat
            if (result > 0 && "en attente".equals(oldEtat) && "fait".equals(paiement.getEtat())) {
                updateCandidatMontantAPayer(paiement.getCandidatId(), paiement.getMontant());
            }

            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt);
        }

        return false;
    }

    @Override
    public boolean delete(Integer id) {
        Paiement paiement = getById(id);
        if (paiement == null) return false;

        String sql = "DELETE FROM paiements WHERE id=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int result = stmt.executeUpdate();

            // Si le paiement était confirmé, réajouter le montant au candidat
            if (result > 0 && "fait".equals(paiement.getEtat())) {
                updateCandidatMontantAPayer(paiement.getCandidatId(), -paiement.getMontant());
            }

            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt);
        }

        return false;
    }

    public int getMontantRestant(int candidatId) {
        Candidat candidat = candidatService.getById(candidatId);
        if (candidat != null) {
            return candidat.getMontantAPayer();
        }
        return 0;
    }

    public double getMontantTotalPaye(int candidatId) {
        String sql = "SELECT SUM(montant) as total FROM paiements WHERE candidat_id = ? AND etat = 'fait'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double total = 0.0;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, candidatId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return total;
    }

    private boolean updateCandidatMontantAPayer(int candidatId, double montantPaye) {
        Candidat candidat = candidatService.getById(candidatId);
        if (candidat == null) return false;

        double nouveauMontant = candidat.getMontantAPayer() - montantPaye;
        candidat.setMontantAPayer((int) Math.max(0, nouveauMontant));

        return candidatService.update(candidatId, candidat);
    }

    private Paiement extractPaiementFromResultSet(ResultSet rs) throws SQLException {
        return new Paiement(
                rs.getInt("id"),
                rs.getInt("candidat_id"),
                rs.getDouble("montant"),
                rs.getDate("date_paiement").toLocalDate(),
                rs.getString("etat"),
                rs.getBytes("recu"),
                rs.getBoolean("is_facilite"),
                rs.getInt("numero_tranche")
        );
    }

    public List<Paiement> creerPaiementsParFacilite(int candidatId, double montantTotal, int nombreTranches) {
        List<Paiement> tranches = new ArrayList<>();
        double montantParTranche = montantTotal / nombreTranches;
        LocalDate dateActuelle = LocalDate.now();

        for (int i = 0; i < nombreTranches; i++) {
            LocalDate dateTranche = dateActuelle.plusMonths(i);
            Paiement tranche = new Paiement(
                    0,
                    candidatId,
                    montantParTranche,
                    dateTranche,
                    "en attente",
                    null,
                    true,
                    i + 1
            );

            if (create(tranche)) {
                tranches.add(tranche);
            }
        }

        return tranches;
    }

    public byte[] genererRecuPDF(int paiementId) {
        Paiement paiement = getById(paiementId);
        if (paiement == null) {
            return null;
        }

        Candidat candidat = candidatService.getById(paiement.getCandidatId());
        if (candidat == null) {
            return null;
        }

        return pdfGenerator.generateReceipt(paiement, candidat);
    }

    public boolean envoyerRecuParEmail(int paiementId, String emailDestinataire) {
        return true;
    }

    public List<Candidat> getCandidatsSansPaiementRecent() {
        List<Candidat> candidats = candidatService.getAll();
        List<Candidat> candidatsSansPaiement = new ArrayList<>();
        LocalDate unMoisAvant = LocalDate.now().minusMonths(1);

        for (Candidat candidat : candidats) {
            if (candidat.getMontantAPayer() > 0) {
                boolean aPaiementRecent = false;

                List<Paiement> paiements = getPaiementsByCandidat(candidat.getCIN());
                for (Paiement paiement : paiements) {
                    if (paiement.getDatePaiement().isAfter(unMoisAvant)) {
                        aPaiementRecent = true;
                        break;
                    }
                }

                if (!aPaiementRecent) {
                    candidatsSansPaiement.add(candidat);
                }
            }
        }

        return candidatsSansPaiement;
    }

    public List<Paiement> rechercherPaiements(Map<String, Object> criteres) {
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM paiements WHERE 1=1");

        if (criteres.containsKey("candidatId")) {
            sql.append(" AND candidat_id = ?");
            parameters.add(criteres.get("candidatId"));
        }

        if (criteres.containsKey("etat")) {
            sql.append(" AND etat = ?");
            parameters.add(criteres.get("etat"));
        }

        if (criteres.containsKey("dateDebut")) {
            sql.append(" AND date_paiement >= ?");
            parameters.add(Date.valueOf((LocalDate) criteres.get("dateDebut")));
        }

        if (criteres.containsKey("dateFin")) {
            sql.append(" AND date_paiement <= ?");
            parameters.add(Date.valueOf((LocalDate) criteres.get("dateFin")));
        }

        if (criteres.containsKey("montantMin")) {
            sql.append(" AND montant >= ?");
            parameters.add(criteres.get("montantMin"));
        }

        if (criteres.containsKey("montantMax")) {
            sql.append(" AND montant <= ?");
            parameters.add(criteres.get("montantMax"));
        }

        if (criteres.containsKey("isFacilite")) {
            sql.append(" AND is_facilite = ?");
            parameters.add(criteres.get("isFacilite"));
        }

        List<Paiement> paiements = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getValidConnection();
            stmt = conn.prepareStatement(sql.toString());

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                paiements.add(extractPaiementFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }

        return paiements;
    }
}