package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SceanceConduite extends Sceance {

    private Integer moniteurId;
    private Integer matriculeVehicule;
    private Float locationX;
    private Float locationY;
    private String locationName;
    public SceanceConduite() {}

    public SceanceConduite(Integer sceanceId, VehiculeType vehiculeType, Integer CondidatId, LocalDateTime sceanceDate, Duration duration, Float price, Integer moniteurId, Float locationX,Float locationY,String locationName, Integer matriculeVehicule) {
        super(sceanceId, vehiculeType, CondidatId,sceanceDate, duration, price);
        this.moniteurId = moniteurId;
        this.locationX = locationX;
        this.locationY = locationY;
        this.matriculeVehicule = matriculeVehicule;
        this.locationName = locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getLocationName() {
        return locationName;
    }
    public void setMoniteurId(Integer moniteurId) {
        this.moniteurId = moniteurId;
    }

    public void setMatriculeVehicule(Integer matriculeVehicule) {
        this.matriculeVehicule = matriculeVehicule;
    }

    public void setLocationX(Float locationX) {
        this.locationX = locationX;
    }
    public void setLocationY(Float locationY) { this.locationY = locationY; }
    public Integer getMoniteurId() {
        return moniteurId;
    }

    public Integer getMatriculeVehicule() {
        return matriculeVehicule;
    }

    public Float getLocationX() {
        return locationX;
    }
    public Float getLocationY() {return locationY;}
    public String toString() {
        return super.toString()+ " moniteurId: "+moniteurId+" matriculeVehicule: "+matriculeVehicule+" location: "+ locationName;
    }
}
