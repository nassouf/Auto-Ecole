package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.LocalDate;

public class Vehicule {
    private Integer matricule;
    private String marque;
    private LocalDate dateAssurance;
    private Float prixAssurance;
    private Float kilometer;
    private Float nbrKilometerRestant;
    private Integer status;
    private VehiculeType vehiculeType;
    private String documents;
    private String factures;
    public Vehicule() {}
    public Vehicule(Integer matricule, String marque, LocalDate dateAssurance,Float prixAssurance, Float kilometer, Float nbrKilometerRestant, Integer status, VehiculeType vehiculeType,String documents,String factures) {
        this.matricule = matricule;
        this.marque = marque;
        this.dateAssurance = dateAssurance;
        this.prixAssurance = prixAssurance;
        this.kilometer = kilometer;
        this.nbrKilometerRestant = nbrKilometerRestant;
        this.status = status;
        this.vehiculeType = vehiculeType;
        this.documents=documents;
        this.factures=factures;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public void setFactures(String factures) {
        this.factures = factures;
    }

    public String getDocuments() {
        return documents;
    }

    public String getFactures() {
        return factures;
    }

    public void setMatricule(Integer matricule) {
        this.matricule = matricule;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setDateAssurance(LocalDate dateAssurance) {
        this.dateAssurance = dateAssurance;
    }

    public void setPrixAssurance(Float prixAssurance) {
        this.prixAssurance = prixAssurance;
    }

    public void setKilometer(Float kilometer) {
        this.kilometer = kilometer;
    }

    public void setNbrKilometerRestant(Float nbrKilometerRestant) {
        this.nbrKilometerRestant = nbrKilometerRestant;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public Integer getMatricule() {
        return matricule;
    }

    public String getMarque() {
        return marque;
    }

    public LocalDate getDateAssurance() {
        return dateAssurance;
    }

    public Float getPrixAssurance() {
        return prixAssurance;
    }

    public Float getKilometer() {
        return kilometer;
    }

    public Float getNbrKilometerRestant() {
        return nbrKilometerRestant;
    }

    public Integer getStatus() {
        return status;
    }

    public VehiculeType getVehiculeType() {
        return vehiculeType;
    }

    public String toString() {
        return "Matricule: " + matricule +
                ", Marque: " + marque +
                ", DateService: " + dateAssurance +
                ", Kilometer: " + kilometer +
                "nbrKilometerRestant: " + nbrKilometerRestant+
                ", Status: " + status+
                ", VehiculeType: " + vehiculeType+
                ",documents: "+documents+
                ",facrures: "+factures;
    }
}
