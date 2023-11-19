package eu.echodream.medhead.Services;

import eu.echodream.medhead.Models.Specialisation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialisationService {
    private final JdbcTemplate jdbcTemplate;

    public SpecialisationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Specialisation> getAll() {
        String query = "SELECT Id, Libelle, Categorie FROM Specialisation";
        return jdbcTemplate.query(query, specialisationRowMapper());
    }

    public Specialisation get(int id) {
        String query = "SELECT Id, Libelle, Categorie FROM Specialisation WHERE Id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, specialisationRowMapper());
    }

    public List<Specialisation> getByHopital(int hopitalId) {
        String query = "SELECT s.Id, s.Libelle, s.Categorie FROM Specialisation s INNER JOIN HopitalSpecialisation hs ON s.Id = hs.SpecialisationId WHERE hs.HopitalId = ?";
        return jdbcTemplate.query(query, new Object[]{hopitalId}, specialisationRowMapper());
    }

    public int insert(Specialisation specialisation) {
        String query = "INSERT INTO Specialisation (Libelle, Categorie) VALUES (?, ?);";
        jdbcTemplate.update(query, specialisation.getLibelle(), specialisation.getCategorie());
        return jdbcTemplate.queryForObject("SELECT MAX(Id) FROM Specialisation", Integer.class);
    }

    public int update(Specialisation specialisation) {
        String query = "UPDATE Specialisation SET Libelle = ?, Categorie = ? WHERE Id = ?";
        return jdbcTemplate.update(query, specialisation.getLibelle(), specialisation.getId());
    }

    public int delete(int id) {
        String query = "DELETE FROM Specialisation WHERE Id = ?";
        return jdbcTemplate.update(query, id);
    }

    private RowMapper<Specialisation> specialisationRowMapper() {
        return (rs, rowNum) -> {
            Specialisation specialisation = new Specialisation();
            specialisation.setId(rs.getInt("Id"));
            specialisation.setLibelle(rs.getString("Libelle"));
            specialisation.setCategorie(rs.getString("Categorie"));
            return specialisation;
        };
    }
}
