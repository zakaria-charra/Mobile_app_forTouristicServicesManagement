package com.example.essaouiraapp;

import java.io.Serializable;

public class Activite implements Serializable {
    public String id;
    public String type;
    public String nom;
    public String adresse;
    public String description;
    public String debut;
    public String fin;
    public String tel;
    public String email;



    public Activite() {
    }

    public Activite(String id, String type, String nom, String adresse, String description, String debut, String fin, String tel, String email) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.debut = debut;
        this.fin = fin;
        this.tel = tel;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdreses() {
        return adresse;
    }

    public void setAdreses(String adreses) {
        this.adresse = adreses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
