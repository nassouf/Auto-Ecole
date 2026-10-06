package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
        private int idMoniteur;
        private String nom;
        private String prenom;
        private LocalDate dateNaissance;
        private String email;
        private int telephone;
        private String adresse;
        private VehiculeType vehiculeType;
        private Float salaire;

        public Employee(int idMoniteur, String nom, String prenom, LocalDate dateNaissance, String email, String adresse, VehiculeType vehiculeType, Float salaire, int telephone) {
        this.idMoniteur = idMoniteur;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.adresse = adresse;
        this.vehiculeType = vehiculeType;
        this.salaire = salaire;
        this.telephone = telephone;
        }

    public Employee() {

    }

    public Float getSalaire() {
        return salaire;
    }

    public void setIdMoniteur(int idMoniteur) {
        this.idMoniteur = idMoniteur;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public void setSalaire(Float salaire) {
        this.salaire = salaire;
    }

    public int getIdMoniteur() { return idMoniteur; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public LocalDate getDateNaissance() { return dateNaissance; }
        public String getEmail() { return email; }
        public int getTelephone() { return telephone; }
        public String getAdresse() { return adresse; }
        public VehiculeType getVehiculeType() { return vehiculeType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee moniteur = (Employee) o;
        return idMoniteur == moniteur.idMoniteur &&
                Objects.equals(email, moniteur.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMoniteur, email);
    }
        @Override
        public String toString() {
            return "CIN: "+idMoniteur+" | "+nom+" "+prenom+" "+ vehiculeType;
        }


}


