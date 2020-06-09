package com.example.essaouiraapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Produit implements Serializable {

    private String id;
    private String vendeur;
    public String nom;
    public String adresse;
    public String quantite;
    public String description;
    public String prix;
    public String date;
    public ArrayList<String> photo;

    public Produit() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Produit(String id, String vendeur, String nom, String adresse, String quantite, String prix, String description,String date) {
        this.id = id;
        this.vendeur = vendeur;
        this.nom = nom;
        this.adresse = adresse;
        this.quantite = quantite;
        this.prix = prix;
        this.description=description;
        this.date=date;
    }
    public Produit(String id, String vendeur, String nom, String adresse, String quantite, String prix,String description,String date, ArrayList<String> photo) {
        this.id = id;
        this.vendeur = vendeur;
        this.nom = nom;
        this.date=date;
        this.adresse = adresse;
        this.quantite = quantite;
        this.prix = prix;
        this.photo = photo;
        this.description=description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
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

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }
}
