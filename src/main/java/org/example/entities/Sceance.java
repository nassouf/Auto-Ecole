package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Sceance {
    private Integer sceanceId;
    private VehiculeType vehiculeType;
    private Integer CondidatId;
    private LocalDateTime sceanceDate;
    private Duration duration;
    private Float price;
    public Sceance() {}
    public Sceance(Integer sceanceId,VehiculeType vehiculeType, Integer CondidatId,LocalDateTime sceanceDate, Duration duration, Float price) {
        this.sceanceId = sceanceId;
        this.sceanceDate = sceanceDate;
        this.vehiculeType = vehiculeType;
        this.CondidatId = CondidatId;
        this.duration = duration;
        this.price = price;
    }
    public void setSceanceDate(LocalDateTime sceanceDate) {
        this.sceanceDate = sceanceDate;
    }
    public LocalDateTime getSceanceDate() {
        return sceanceDate;
    }
    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
    }
    public VehiculeType getVehiculeType() {
        return vehiculeType;
    }

    public void setSceanceId(Integer sceanceId) {
        this.sceanceId = sceanceId;
    }

    public void setCondidatId(Integer condidatId) {
        CondidatId = condidatId;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getSceanceId() {
        return sceanceId;
    }

    public Integer getCondidatId() {
        return CondidatId;
    }

    public Duration getDuration() {
        return duration;
    }

    public Float getPrice() {
        return price;
    }
    public LocalDateTime getEndTime(){
        return sceanceDate.plus(duration);
    }
    public String toString() {
        return "sceanceId: "+sceanceId+" condidatId: "+CondidatId+"seance date: "+sceanceDate+" duration: "+duration+" price: "+price;
    }
}
