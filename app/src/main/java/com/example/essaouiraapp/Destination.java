package com.example.essaouiraapp;

import java.io.Serializable;

public class Destination implements Serializable {
    public String id;
    public String nom;
    public String adresse;
    public String description;

    public Destination() {
    }

    public Destination(String id, String nom, String adresse, String description) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
