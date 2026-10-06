package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Examen {
    private Integer id;
    private Boolean status;
    private Integer idCandidat;
    private LocalDateTime dateExamen;
    private Duration durationExamen;
    private VehiculeType vehiculeType;
    private Float prix;
    public Examen() {}
    public Examen(Integer id,Boolean status, Integer idCandidat,VehiculeType vehiculeType,LocalDateTime dateExamen,Duration durationExamen,Float prix) {
        this.id = id;
        this.status = status;
        this.idCandidat = idCandidat;
        this.dateExamen = dateExamen;
        this.prix = prix;
        this.vehiculeType=vehiculeType;
        this.durationExamen=durationExamen;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Boolean getStatus() {
        return status;
    }

    public void setIdCandidat(Integer idCandidat) {
        this.idCandidat = idCandidat;
    }

    public void setDateExamen(LocalDateTime dateExamen) {
        this.dateExamen = dateExamen;
    }

    public void setDurationExamen(Duration durationExamen) {
        this.durationExamen = durationExamen;
    }

    public void setVehiculeType(VehiculeType vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdCandidat() {
        return idCandidat;
    }

    public LocalDateTime getDateExamen() {
        return dateExamen;
    }

    public Duration getDurationExamen() {
        return durationExamen;
    }

    public VehiculeType getVehiculeType() {
        return vehiculeType;
    }

    public LocalDateTime getEndTime(){
        return dateExamen.plus(durationExamen);
    }

    public Float getPrix() {
        return prix;
    }
}
