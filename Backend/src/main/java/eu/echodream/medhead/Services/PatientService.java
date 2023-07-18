package eu.echodream.medhead.Services;

import eu.echodream.medhead.Models.Patient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class PatientService {
    private final JdbcTemplate jdbcTemplate;

    public PatientService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Patient> getAll() {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient";
        return jdbcTemplate.query(query, rowMapper);
    }

    public Patient getByNumINSEE(String numINSEE) {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient WHERE NumINSEE = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{numINSEE}, rowMapper);
    }

    public Patient get(int id) {
        String query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient WHERE Id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, rowMapper);
    }

    public void insert(Patient patient) {
        String query = "INSERT INTO Patient (Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, patient.getNom(), patient.getPrenom(), patient.getNomJF(),
                patient.getDateNaissance(), patient.getAdresse(), patient.getVille(),
                patient.getCodePostal(), patient.getNumINSEE(), patient.getLocation(), patient.getSpecialiteRequise());
    }

    public void update(Patient patient) {
        String query = "UPDATE Patient SET Nom = ?, Prenom = ?, NomJF = ?, DateNaissance = ?, Adresse = ?, Ville = ?, CodePostal = ?, Location = ?, SpecialiteRequise = ? WHERE NumINSEE = ?";
        jdbcTemplate.update(query, patient.getNom(), patient.getPrenom(), patient.getNomJF(),
                patient.getDateNaissance(), patient.getAdresse(), patient.getVille(),
                patient.getCodePostal(), patient.getLocation(), patient.getSpecialiteRequise(), patient.getNumINSEE());
    }

    public void deleteByNumINSEE(String numINSEE) {
        String query = "DELETE FROM Patient WHERE NumINSEE = ?";
        jdbcTemplate.update(query, numINSEE);
    }

    public void delete(int id) {
        String query = "DELETE FROM Patient WHERE Id = ?";
        jdbcTemplate.update(query, id);
    }

    private static final RowMapper<Patient> rowMapper = (rs, rowNum) ->
            new Patient(rs.getInt("Id"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("NomJF"),
                    rs.getDate("DateNaissance"), rs.getString("Adresse"), rs.getString("Ville"),
                    rs.getString("CodePostal"), rs.getString("NumINSEE"), rs.getString("Location"),
                    rs.getString("SpecialiteRequise"));

}