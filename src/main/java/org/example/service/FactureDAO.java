package org.example.service;

import org.example.entities.Facture;
import org.example.service.ConxDB;
import org.example.service.myInterfaces.DBservices;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO implements DBservices<Facture> {

    private Connection connection;

    public FactureDAO() {
        this.connection = ConxDB.getInstance();
    }

    @Override
    public <T> List<T> getAll() {
        List<Facture> factures = new ArrayList<>();
        String query = "SELECT * FROM facture ORDER BY date_facture DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Facture facture = new Facture();
                facture.setIdFacture(rs.getInt("id_facture"));
                facture.setNumeroFacture(rs.getString("numero_facture"));

                Date dateFacture = rs.getDate("date_facture");
                if (dateFacture != null) {
                    facture.setDateFacture(dateFacture.toLocalDate());
                }

                facture.setDescription(rs.getString("description"));
                facture.setMontant(rs.getFloat("montant"));
                facture.setCategorie(rs.getString("categorie"));
                facture.setStatut(rs.getString("statut"));

                Date datePaiement = rs.getDate("date_paiement");
                if (datePaiement != null) {
                    facture.setDatePaiement(datePaiement.toLocalDate());
                }

                facture.setCheminScan(rs.getString("chemin_scan"));
                factures.add(facture);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des factures: " + e.getMessage());
        }
        return (List<T>) factures;
    }

    @Override
    public <T> T getById(Integer id) {
        String query = "SELECT * FROM facture WHERE id_facture = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Facture facture = new Facture();
                    facture.setIdFacture(rs.getInt("id_facture"));
                    facture.setNumeroFacture(rs.getString("numero_facture"));

                    Date dateFacture = rs.getDate("date_facture");
                    if (dateFacture != null) {
                        facture.setDateFacture(dateFacture.toLocalDate());
                    }

                    facture.setDescription(rs.getString("description"));
                    facture.setMontant(rs.getFloat("montant"));
                    facture.setCategorie(rs.getString("categorie"));
                    facture.setStatut(rs.getString("statut"));

                    Date datePaiement = rs.getDate("date_paiement");
                    if (datePaiement != null) {
                        facture.setDatePaiement(datePaiement.toLocalDate());
                    }

                    facture.setCheminScan(rs.getString("chemin_scan"));
                    return (T) facture;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la facture: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean create(Facture facture) {
        String query = "INSERT INTO facture (numero_facture, date_facture, description, montant, categorie, statut, date_paiement, chemin_scan) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, facture.getNumeroFacture());
            pstmt.setDate(2, facture.getDateFacture() != null ? Date.valueOf(facture.getDateFacture()) : null);
            pstmt.setString(3, facture.getDescription());
            pstmt.setFloat(4, facture.getMontant());
            pstmt.setString(5, facture.getCategorie());
            pstmt.setString(6, facture.getStatut());
            pstmt.setDate(7, facture.getDatePaiement() != null ? Date.valueOf(facture.getDatePaiement()) : null);
            pstmt.setString(8, facture.getCheminScan());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    facture.setIdFacture(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la facture: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Integer id, Facture facture) {
        String query = "UPDATE facture SET numero_facture = ?, date_facture = ?, description = ?, " +
                "montant = ?, categorie = ?, statut = ?, date_paiement = ?, chemin_scan = ? WHERE id_facture = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, facture.getNumeroFacture());
            pstmt.setDate(2, facture.getDateFacture() != null ? Date.valueOf(facture.getDateFacture()) : null);
            pstmt.setString(3, facture.getDescription());
            pstmt.setFloat(4, facture.getMontant());
            pstmt.setString(5, facture.getCategorie());
            pstmt.setString(6, facture.getStatut());
            pstmt.setDate(7, facture.getDatePaiement() != null ? Date.valueOf(facture.getDatePaiement()) : null);
            pstmt.setString(8, facture.getCheminScan());
            pstmt.setInt(9, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la facture: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM facture WHERE id_facture = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la facture: " + e.getMessage());
        }
        return false;
    }


    // Rechercher des factures par divers critères
    public List<Facture> rechercherFactures(String numeroFacture, String categorie, String statut) {
        List<Facture> factures = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM facture WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (numeroFacture != null && !numeroFacture.trim().isEmpty()) {
            queryBuilder.append(" AND numero_facture LIKE ?");
            parameters.add("%" + numeroFacture + "%");
        }

        if (categorie != null && !categorie.trim().isEmpty()) {
            queryBuilder.append(" AND categorie = ?");
            parameters.add(categorie);
        }

        if (statut != null && !statut.trim().isEmpty()) {
            queryBuilder.append(" AND statut = ?");
            parameters.add(statut);
        }

        queryBuilder.append(" ORDER BY date_facture DESC");

        try (PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Facture facture = new Facture();
                    facture.setIdFacture(rs.getInt("id_facture"));
                    facture.setNumeroFacture(rs.getString("numero_facture"));

                    Date dateFacture = rs.getDate("date_facture");
                    if (dateFacture != null) {
                        facture.setDateFacture(dateFacture.toLocalDate());
                    }

                    facture.setDescription(rs.getString("description"));
                    facture.setMontant(rs.getFloat("montant"));
                    facture.setCategorie(rs.getString("categorie"));
                    facture.setStatut(rs.getString("statut"));

                    Date datePaiement = rs.getDate("date_paiement");
                    if (datePaiement != null) {
                        facture.setDatePaiement(datePaiement.toLocalDate());
                    }

                    facture.setCheminScan(rs.getString("chemin_scan"));
                    factures.add(facture);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des factures: " + e.getMessage());
        }

        return factures;
    }

    // Récupérer le total des factures par statut
    public Float getTotalFacturesByStatut(String statut) {
        String query = "SELECT SUM(montant) FROM facture WHERE statut = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, statut);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du total des factures: " + e.getMessage());
        }

        return (float) 0;
    }
}