package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class ExamenConduit extends Examen{
    private Integer idMoniteur;
    private Integer matriculeVehicule;
    private Float locationX;
    private Float locationY;
    private String locationName;
    public ExamenConduit() {}
    public ExamenConduit(Integer id,boolean status, Integer idCandidat,Integer idMoniteur, Integer matriculeVehicule, VehiculeType vehiculeType, LocalDateTime dateExamen, Duration durationExamen, Float prix, Float locationX, Float locationY,String locationName) {
        super(id,status,idCandidat,vehiculeType,dateExamen,durationExamen,prix);
        this.idMoniteur = idMoniteur;
        this.matriculeVehicule = matriculeVehicule;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationName = locationName;
    }

    public void setIdMoniteur(Integer idMoniteur) {
        this.idMoniteur = idMoniteur;
    }

    public void setMatriculeVehicule(Integer matriculeVehicule) {
        this.matriculeVehicule = matriculeVehicule;
    }

    public void setLocationX(Float locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(Float locationY) {
        this.locationY = locationY;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getIdMoniteur() {
        return idMoniteur;
    }

    public Integer getMatriculeVehicule() {
        return matriculeVehicule;
    }

    public Float getLocationX() {
        return locationX;
    }

    public Float getLocationY() {
        return locationY;
    }

    public String getLocationName() {
        return locationName;
    }
}
