package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Candidat {
    private int CIN;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private int telephone;
    private String adresse;
    private VehiculeType typePermis;
    private String status;
    private String repos;
    private int nbr_seance_code_atteint	;
    private int nbr_seance_conduit_atteint;
    private Integer idVehicule;
    private int montantAPayer;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    public Candidat(int CIN, String nom, String prenom, LocalDate dateNaissance, String email, int telephone, String adresse, VehiculeType typePermis, String status, String repos, int nbr_seance_code_atteint, int nbr_seance_conduit_atteint, Integer idVehicule, int montantAPayer) {
        this.CIN = CIN;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.typePermis = typePermis;
        this.status = status;
        this.repos = repos;
        this.nbr_seance_code_atteint = nbr_seance_code_atteint;
        this.nbr_seance_conduit_atteint = nbr_seance_conduit_atteint;

        this.idVehicule = idVehicule;
        this.montantAPayer = montantAPayer;
    }

    public Candidat() {

    }

    public void setCIN(int CIN) {
        if (CIN <= 0) throw new IllegalArgumentException("CIN invalide.");
        this.CIN = CIN;
    }

    public void setNom(String nom) {
        if (nom == null || nom.length() < 2) throw new IllegalArgumentException("Nom invalide.");
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        if (prenom == null || prenom.length() < 2) throw new IllegalArgumentException("Prénom invalide.");
        this.prenom = prenom;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        if (dateNaissance == null || dateNaissance.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Date de naissance invalide.");
        this.dateNaissance = dateNaissance;
    }

    public void setEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("Email invalide.");
        this.email = email;
    }

    public void setTelephone(int telephone) {
        if (String.valueOf(telephone).length() < 8)
            throw new IllegalArgumentException("Numéro de téléphone invalide.");
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        if (adresse == null || adresse.length() < 3)
            throw new IllegalArgumentException("Adresse invalide.");
        this.adresse = adresse;
    }

    public void setTypePermis(VehiculeType typePermis) {
        if (typePermis == null || typePermis.toString().isEmpty())
            throw new IllegalArgumentException("Type de permis invalide.");
        this.typePermis = typePermis;
    }

    public void setStatus(String status) {
        if ("Actif".equals(status) || "Inactif".equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Status invalide.");
        }
    }

    public void setNbr_seance_code_atteint(int nbr_seance_code_atteint) {
        this.nbr_seance_code_atteint = nbr_seance_code_atteint;
    }

    public void setNbr_seance_conduit_atteint(int nbr_seance_conduit_atteint) {
        this.nbr_seance_conduit_atteint = nbr_seance_conduit_atteint;
    }



    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public void setMontantAPayer(int montantAPayer) {
        if (montantAPayer < 0)
            throw new IllegalArgumentException("Montant à payer invalide.");
        this.montantAPayer = montantAPayer;
    }

    public void setRepos(String repos) {
        this.repos = repos;
    }

    // Getters (inchangés)
    public int getCIN() { return CIN; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getEmail() { return email; }
    public int getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public VehiculeType getTypePermis() { return typePermis; }
    public String getStatus() { return status; }
    public String getRepos() { return repos; }

    public int getNbr_seance_code_atteint() {
        return nbr_seance_code_atteint;
    }

    public int getNbr_seance_conduit_atteint() {
        return nbr_seance_conduit_atteint;
    }
    public Integer getIdVehicule() { return idVehicule; }
    public int getMontantAPayer() { return montantAPayer; }
    public String toString(){
        return "CIN: "+getCIN()+"| "+nom+" "+prenom+" "+typePermis;
    }
}
