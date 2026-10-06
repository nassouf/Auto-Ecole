package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SceanceCode extends Sceance {
    private Integer ingenieurId;
    public SceanceCode() {}
    public SceanceCode(Integer sceanceId, VehiculeType vehiculeType, Integer CondidatId, LocalDateTime sceanceDate, Duration duration, Float price, Integer ingenieurId) {
        super(sceanceId, vehiculeType, CondidatId,sceanceDate, duration, price);
        this.ingenieurId = ingenieurId;
    }
    public Integer getIngenieurId() {
        return ingenieurId;
    }
    public void setIngenieurId(Integer ingenieurId) {
        this.ingenieurId = ingenieurId;
    }
    public String toString() {
        return super.toString() + " Ingenieur Id: " + ingenieurId;
    }

}
