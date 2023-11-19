package eu.echodream.medhead.Models;

import java.util.ArrayList;
import java.util.List;

public class Hopital {
    private int id;
    private String nom;
    private String adresse;
    private String ville;
    private String codePostal;
    private int litsDisponibles;
    private List<Specialisation> specialisations;

    public Hopital() { }
    public Hopital(int id, String nom, String adresse, String ville, String codePostal, int litsDisponibles) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.litsDisponibles = litsDisponibles;
        this.specialisations = new ArrayList<>();
    }

    public Hopital(int id, String nom, String adresse, String ville, String codePostal, int litsDisponibles, ArrayList<Specialisation> specialisations) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.litsDisponibles = litsDisponibles;
        this.specialisations = specialisations;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public String getVille() { return ville; }
    public String getCodePostal() { return codePostal; }
    public int getLitsDisponibles() { return litsDisponibles; }
    public List<Specialisation> getSpecialisations() { return specialisations; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setVille(String ville) { this.ville = ville; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public void setLitsDisponibles(int litsDisponibles) { this.litsDisponibles = litsDisponibles; }
    public void setSpecialisations(List<Specialisation> specialisations) { this.specialisations = specialisations; }
}

