package eu.echodream.medhead.Services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HopitalSpecialisationService {
    private final JdbcTemplate jdbcTemplate;

    public HopitalSpecialisationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(int hopitalId, int specialisationId) {
        String checkQuery = "SELECT 1 FROM HopitalSpecialisation WHERE HopitalId = ? AND SpecialisationId = ?";
        try {
            Integer exists = jdbcTemplate.queryForObject(checkQuery, new Object[]{hopitalId, specialisationId}, Integer.class);
            // Si on arrive ici, la ligne existe déjà
        } catch (EmptyResultDataAccessException e) {
            // On insère la nouvelle ligne car elle n'existe pas
            String insertQuery = "INSERT INTO HopitalSpecialisation (HopitalId, SpecialisationId) VALUES (?, ?)";
            jdbcTemplate.update(insertQuery, hopitalId, specialisationId);
        }
    }

    public void delete(int hopitalId, int specialisationId) {
        String query = "DELETE FROM HopitalSpecialisation WHERE HopitalId = ? AND SpecialisationId = ?";
        jdbcTemplate.update(query, hopitalId, specialisationId);
    }

    public void deleteByHopital(int hopitalId) {
        String query = "DELETE FROM HopitalSpecialisation WHERE HopitalId = ?";
        jdbcTemplate.update(query, hopitalId);
    }
}
