package org.example.entities;


import java.time.LocalDate;

public class Facture {
    private int idFacture;
    private String numeroFacture;
    private LocalDate dateFacture;
    private String description;
    private float montant;
    private String categorie;
    private String statut;
    private LocalDate datePaiement;
    private String cheminScan;

    // Constructeur par défaut
    public Facture() {
    }

    // Constructeur avec paramètres
    public Facture(int idFacture, String numeroFacture, LocalDate dateFacture,
                   String description, float montant, String categorie,
                   String statut, LocalDate datePaiement, String cheminScan) {
        this.idFacture = idFacture;
        this.numeroFacture = numeroFacture;
        this.dateFacture = dateFacture;
        this.description = description;
        this.montant = montant;
        this.categorie = categorie;
        this.statut = statut;
        this.datePaiement = datePaiement;
        this.cheminScan = cheminScan;
    }

    // Getters et Setters
    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getCheminScan() {
        return cheminScan;
    }

    public void setCheminScan(String cheminScan) {
        this.cheminScan = cheminScan;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "idFacture=" + idFacture +
                ", numeroFacture='" + numeroFacture + '\'' +
                ", dateFacture=" + dateFacture +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                ", categorie='" + categorie + '\'' +
                ", statut='" + statut + '\'' +
                ", datePaiement=" + datePaiement +
                '}';
    }
}