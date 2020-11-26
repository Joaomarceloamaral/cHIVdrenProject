package com.example.chivdrenproject.model;

public class ModelMedicamento {
    String pId, rNome,rHora, rMin, rDescr, pTime, uid, email, name ;

    public ModelMedicamento() {
    }

    public ModelMedicamento(String pId, String rNome, String rHora, String rMin, String rDescr, String pTime, String uid, String email, String name) {
        this.pId = pId;
        this.rNome = rNome;
        this.rHora = rHora;
        this.rMin = rMin;
        this.rDescr = rDescr;
        this.pTime = pTime;
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getrNome() {
        return rNome;
    }

    public void setrNome(String rNome) {
        this.rNome = rNome;
    }

    public String getrHora() {
        return rHora;
    }

    public void setrHora(String rHora) {
        this.rHora = rHora;
    }

    public String getrMin() {
        return rMin;
    }

    public void setrMin(String rMin) {
        this.rMin = rMin;
    }

    public String getrDescr() {
        return rDescr;
    }

    public void setrDescr(String rDescr) {
        this.rDescr = rDescr;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
