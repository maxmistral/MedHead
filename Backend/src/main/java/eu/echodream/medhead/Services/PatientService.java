package eu.echodream.medhead.Services;

import eu.echodream.medhead.Models.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientService {
    private final JdbcTemplate jdbcTemplate;

    public PatientService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Patient> getAll() {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, HopitalActuel, SpecialiteRequise FROM Patient";
        return jdbcTemplate.query(query, rowMapper);
    }

    public Patient getByNumINSEE(String numINSEE) {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, HopitalActuel, SpecialiteRequise FROM Patient WHERE NumINSEE = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{numINSEE}, rowMapper);
    }

    public Patient get(int id) {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, HopitalActuel, SpecialiteRequise FROM Patient WHERE Id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, rowMapper);
    }

    public int insert(Patient patient) {
        String query = "INSERT INTO Patient (Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, HopitalActuel, SpecialiteRequise) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(query, patient.getNom(), patient.getPrenom(), patient.getNomJF(), patient.getDateNaissance().toString(), patient.getAdresse(), patient.getVille(), patient.getCodePostal(), patient.getNumINSEE(), patient.getHopitalActuel(), patient.getSpecialiteRequise());
        return jdbcTemplate.queryForObject("SELECT MAX(Id) FROM Patient", Integer.class);
    }


    public void update(Patient patient) {
        String query = "UPDATE Patient SET Nom = ?, Prenom = ?, NomJF = ?, DateNaissance = ?, Adresse = ?, Ville = ?, CodePostal = ?, HopitalActuel = ?, SpecialiteRequise = ? WHERE NumINSEE = ?";
        jdbcTemplate.update(query, patient.getNom(), patient.getPrenom(), patient.getNomJF(),
                patient.getDateNaissance(), patient.getAdresse(), patient.getVille(),
                patient.getCodePostal(), patient.getHopitalActuel(), patient.getSpecialiteRequise(), patient.getNumINSEE());
    }

    public void deleteByNumINSEE(String numINSEE) {
        String query = "DELETE FROM Patient WHERE NumINSEE = ?";
        jdbcTemplate.update(query, numINSEE);
    }

    public void delete(int id) {
        String query = "DELETE FROM Patient WHERE Id = ?";
        jdbcTemplate.update(query, id);
    }

    private static final RowMapper<Patient> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("Id");
        String nom = rs.getString("Nom");
        String prenom = rs.getString("Prenom");
        String nomJF = rs.getString("NomJF");
        String adresse = rs.getString("Adresse");
        String ville = rs.getString("Ville");
        String codePostal = rs.getString("CodePostal");
        String numINSEE = rs.getString("NumINSEE");
        int hopitalActuel = rs.getInt("HopitalActuel");
        int specialiteRequise = rs.getInt("SpecialiteRequise");

        // Supposons que la date est stockée en tant que String dans le format "yyyy-MM-dd"
        String dateString = rs.getString("DateNaissance");
        LocalDate dateNaissance = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        return new Patient(id, nom, prenom, nomJF, Date.valueOf(dateNaissance), adresse, ville, codePostal, numINSEE, hopitalActuel, specialiteRequise);
        };
    }