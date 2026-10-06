package org.example.entities;

import java.time.LocalDate;

public class Maintenance {
    private Integer id;
    private Integer matricule;
    private String description;
    private LocalDate dateMaintenance;
    private Float price;
    public Maintenance() {}
    public Maintenance(Integer id,Integer matricule, String description, LocalDate dateMaintenance, Float price) {
        this.id = id;
        this.matricule = matricule;
        this.description = description;
        this.dateMaintenance = dateMaintenance;
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
    }

    public void setMatricule(Integer matricule) {
        this.matricule = matricule;
    }

    public Integer getMatricule() {
        return matricule;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateMaintenance() {
        return dateMaintenance;
    }

    public Float getPrice() {
        return price;
    }
    public String toString() {
        return "Maintenance [id=" + id + ", description=" + description + ", dateMaintenance="+dateMaintenance+", price="+price+"]";
    }
}
