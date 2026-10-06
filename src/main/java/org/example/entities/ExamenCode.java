package org.example.entities;

import org.example.entities.myEnums.VehiculeType;

import java.time.Duration;
import java.time.LocalDateTime;

public class ExamenCode extends Examen{
    private Integer idIngenieur;
    public ExamenCode(){

    }
    public ExamenCode(Integer id,boolean status, Integer idCandidat,Integer idIngenieur, VehiculeType vehiculeType, LocalDateTime dateExamen, Duration durationExamen, Float prix){
        super(id,status,idCandidat,vehiculeType,dateExamen,durationExamen,prix);
        this.idIngenieur = idIngenieur;
    }
    public Integer getIdIngenieur() {
        return idIngenieur;
    }
    public void setIdIngenieur(Integer idIngenieur) {
        this.idIngenieur = idIngenieur;
    }
}
