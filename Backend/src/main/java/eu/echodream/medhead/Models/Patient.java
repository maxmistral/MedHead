package eu.echodream.medhead.Models;

import java.sql.Date;
import java.time.LocalDate;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String nomJF;
    private LocalDate dateNaissance;
    private String adresse;
    private String ville;
    private String codePostal;
    private String numINSEE;
    private String location;
    private int specialiteRequise;

    public Patient(int id, String nom, String prenom, String nomJF, Date dateNaissance, String adresse, String ville, String codePostal, String numINSEE, String location, String specialiteRequise) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nomJF = nomJF;
        this.dateNaissance = dateNaissance.toLocalDate();
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.numINSEE = numINSEE;
        this.location = location;
        this.specialiteRequise = Integer.parseInt(specialiteRequise);
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNomJF() { return nomJF; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getAdresse() { return adresse; }
    public String getVille() { return ville; }
    public String getCodePostal() { return codePostal; }
    public String getNumINSEE() { return numINSEE; }
    public String getLocation() { return location; }
    public int getSpecialiteRequise() { return specialiteRequise; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setNomJF(String nomJF) { this.nomJF = nomJF; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setVille(String ville) { this.ville = ville; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setNumINSEE(String numINSEE) { this.numINSEE = numINSEE; }
    public void setLocation(String location) { this.location = location; }
    public void setSpecialiteRequise(int specialiteRequise) { this.specialiteRequise = specialiteRequise; }
}
