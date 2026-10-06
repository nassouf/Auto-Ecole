package org.example.entities;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Tarifs {

    private int codeSessionPricevoiture;
    private int  codeSessionPricemoto;
    private int codeSessionPricecamion;

     private int drivingSessionPricevoiture;
    private int drivingSessionPricemoto;
     private int drivingSessionPricecamion;
    public Tarifs(){}
    public Tarifs(int codeSessionPricevoiture, int codeSessionPricemoto, int codeSessionPricecamion, int drivingSessionPricevoiture, int drivingSessionPricemoto, int drivingSessionPricecamion) {
        this.codeSessionPricevoiture = codeSessionPricevoiture;
        this.codeSessionPricemoto = codeSessionPricemoto;
        this.codeSessionPricecamion = codeSessionPricecamion;
        this.drivingSessionPricevoiture = drivingSessionPricevoiture;
        this.drivingSessionPricemoto = drivingSessionPricemoto;
        this.drivingSessionPricecamion = drivingSessionPricecamion;
    }

    public int getCodeSessionPricevoiture() {
        return codeSessionPricevoiture;
    }

    public int getCodeSessionPricemoto() {
        return codeSessionPricemoto;
    }

    public int getCodeSessionPricecamion() {
        return codeSessionPricecamion;
    }

    public int getDrivingSessionPricevoiture() {
        return drivingSessionPricevoiture;
    }

    public int getDrivingSessionPricemoto() {
        return drivingSessionPricemoto;
    }

    public int getDrivingSessionPricecamion() {
        return drivingSessionPricecamion;
    }

    public void setCodeSessionPricevoiture(int codeSessionPricevoiture) {
        this.codeSessionPricevoiture = codeSessionPricevoiture;
    }

    public void setCodeSessionPricemoto(int codeSessionPricemoto) {
        this.codeSessionPricemoto = codeSessionPricemoto;
    }

    public void setCodeSessionPricecamion(int codeSessionPricecamion) {
        this.codeSessionPricecamion = codeSessionPricecamion;
    }

    public void setDrivingSessionPricevoiture(int drivingSessionPricevoiture) {
        this.drivingSessionPricevoiture = drivingSessionPricevoiture;
    }

    public void setDrivingSessionPricemoto(int drivingSessionPricemoto) {
        this.drivingSessionPricemoto = drivingSessionPricemoto;
    }

    public void setDrivingSessionPricecamion(int drivingSessionPricecamion) {
        this.drivingSessionPricecamion = drivingSessionPricecamion;
    }
}
