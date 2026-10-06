package org.example.entities;

import java.util.regex.Pattern;

public class Centre {
    private String nom;
    private String password;
    private String contactGmail;
    private String lieu;
    private String logo;
    private String numeroTelephone;
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    private static final String GMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";


    public Centre(String nom, String password, String contactGmail, String lieu, String logo, String numeroTelephone) {
        this.nom = nom;
        setPassword(password);
        setContactGmail(contactGmail);
        this.lieu = lieu;
        this.logo = logo;
        this.numeroTelephone = numeroTelephone;
    }

    // Getter et setter pour le nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et setter pour le mot de passe avec validation
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new IllegalArgumentException();
        }
        this.password = password;
    }


    public String getContactGmail() {
        return contactGmail;
    }

    public void setContactGmail(String contactGmail) {
        if (!Pattern.matches(GMAIL_REGEX, contactGmail)) {
            throw new IllegalArgumentException();
        }
        this.contactGmail = contactGmail;
    }

    // Getter et setter pour le lieu
    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    // Getter et setter pour le logo
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    // Getter et setter pour le numéro de téléphone
    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }


}
