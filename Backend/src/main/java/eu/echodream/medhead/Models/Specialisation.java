package eu.echodream.medhead.Models;

public class Specialisation {
    private int id;
    private String libelle;
    private String categorie;

    public Specialisation() { }
    public Specialisation(int id, String libelle, String categorie) {
        setId(id);
        setLibelle(libelle);
        setCategorie(categorie);
    }

    // Getters
    public int getId() { return id; }
    public String getLibelle() { return libelle; }
    public String getCategorie() { return categorie; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
}

