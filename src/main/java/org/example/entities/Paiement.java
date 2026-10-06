package org.example.entities;


import java.util.Date;
import java.time.LocalDate;

public class Paiement {
    private int id;
    private int candidatId;
    private double montant;
    private LocalDate datePaiement;
    private String etat; // "en attente" ou "fait"
    private byte[] recu;
    private boolean isFacilite; // Indique si ce paiement fait partie d'un plan de facilité
    private int numeroTranche; // Numéro de tranche si c'est un paiement par facilité

    public Paiement() {}

    public Paiement(int id, int candidatId, double montant, LocalDate datePaiement, String etat, byte[] recu) {
        this.id = id;
        this.candidatId = candidatId;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.etat = etat;
        this.recu = recu;
        this.isFacilite = false;
        this.numeroTranche = 0;
    }

    public Paiement(int id, int candidatId, double montant, LocalDate datePaiement, String etat, byte[] recu, boolean isFacilite, int numeroTranche) {
        this.id = id;
        this.candidatId = candidatId;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.etat = etat;
        this.recu = recu;
        this.isFacilite = isFacilite;
        this.numeroTranche = numeroTranche;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(int candidatId) {
        this.candidatId = candidatId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public byte[] getRecu() {
        return recu;
    }

    public void setRecu(byte[] recu) {
        this.recu = recu;
    }

    public boolean isFacilite() {
        return isFacilite;
    }

    public void setFacilite(boolean facilite) {
        isFacilite = facilite;
    }

    public int getNumeroTranche() {
        return numeroTranche;
    }

    public void setNumeroTranche(int numeroTranche) {
        this.numeroTranche = numeroTranche;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", candidatId=" + candidatId +
                ", montant=" + montant +
                ", datePaiement=" + datePaiement +
                ", etat='" + etat + '\'' +
                ", isFacilite=" + isFacilite +
                ", numeroTranche=" + numeroTranche +
                '}';
    }
}



