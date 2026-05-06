package models;

import java.sql.Date;

public class Medicament {

    private int id;
    private String nom;
    private String description;
    private String categorie;
    private String dosage;
    private String forme;
    private int quantiteStock;
    private int seuilMinimum;
    private String numeroLot;
    private int quantiteLot;
    private Date dateExpiration;
    private String serviceNom;
    private String etatStock;
    private String alerteExpiration;

    public Medicament() {
    }

    public Medicament(int id, String nom, String description, String categorie,
                      String dosage, String forme, int quantiteStock,
                      int seuilMinimum, String numeroLot, int quantiteLot,
                      Date dateExpiration, String serviceNom,
                      String etatStock, String alerteExpiration) {

        this.id = id;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.dosage = dosage;
        this.forme = forme;
        this.quantiteStock = quantiteStock;
        this.seuilMinimum = seuilMinimum;
        this.numeroLot = numeroLot;
        this.quantiteLot = quantiteLot;
        this.dateExpiration = dateExpiration;
        this.serviceNom = serviceNom;
        this.etatStock = etatStock;
        this.alerteExpiration = alerteExpiration;
    }

    public Medicament(String nom, String description, String categorie,
                      String dosage, String forme, int quantiteStock,
                      int seuilMinimum, String numeroLot, int quantiteLot,
                      Date dateExpiration, String serviceNom,
                      String etatStock, String alerteExpiration) {

        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.dosage = dosage;
        this.forme = forme;
        this.quantiteStock = quantiteStock;
        this.seuilMinimum = seuilMinimum;
        this.numeroLot = numeroLot;
        this.quantiteLot = quantiteLot;
        this.dateExpiration = dateExpiration;
        this.serviceNom = serviceNom;
        this.etatStock = etatStock;
        this.alerteExpiration = alerteExpiration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public int getSeuilMinimum() {
        return seuilMinimum;
    }

    public void setSeuilMinimum(int seuilMinimum) {
        this.seuilMinimum = seuilMinimum;
    }

    public String getNumeroLot() {
        return numeroLot;
    }

    public void setNumeroLot(String numeroLot) {
        this.numeroLot = numeroLot;
    }

    public int getQuantiteLot() {
        return quantiteLot;
    }

    public void setQuantiteLot(int quantiteLot) {
        this.quantiteLot = quantiteLot;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getServiceNom() {
        return serviceNom;
    }

    public void setServiceNom(String serviceNom) {
        this.serviceNom = serviceNom;
    }

    public String getEtatStock() {
        return etatStock;
    }

    public void setEtatStock(String etatStock) {
        this.etatStock = etatStock;
    }

    public String getAlerteExpiration() {
        return alerteExpiration;
    }

    public void setAlerteExpiration(String alerteExpiration) {
        this.alerteExpiration = alerteExpiration;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", quantiteStock=" + quantiteStock +
                ", dateExpiration=" + dateExpiration +
                '}';
    }
}
