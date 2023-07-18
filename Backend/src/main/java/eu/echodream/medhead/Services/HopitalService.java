package eu.echodream.medhead.Services;

import eu.echodream.medhead.Models.Hopital;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HopitalService {
    private final JdbcTemplate jdbcTemplate;
    private final SpecialisationService specialisationService;
    private final HopitalSpecialisationService hopitalSpecialisationService;

    public HopitalService(JdbcTemplate jdbcTemplate, SpecialisationService specialisationService,
                          HopitalSpecialisationService hopitalSpecialisationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.specialisationService = specialisationService;
        this.hopitalSpecialisationService = hopitalSpecialisationService;
    }

    public List<Hopital> getAll() {
        String query = "SELECT Id, Nom, Adresse, Ville, CodePostal, LitsDisponibles FROM Hopital";
        List<Hopital> result = jdbcTemplate.query(query, rowMapper);
        result.forEach(hopital -> hopital.setSpecialisations(specialisationService.getByHopital(hopital.getId())));
        return result;
    }

    public Hopital get(int id) {
        String query = "SELECT Id, Nom, Adresse, Ville, CodePostal, LitsDisponibles FROM Hopital WHERE Id = ?";
        Hopital result = jdbcTemplate.queryForObject(query, new Object[]{id}, rowMapper);
        if (result != null) {
            result.setSpecialisations(specialisationService.getByHopital(id));
        }
        return result;
    }

    public int insert(Hopital hopital) {
        String query = "INSERT INTO Hopital (Nom, Adresse, Ville, CodePostal, LitsDisponibles) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, hopital.getNom(), hopital.getAdresse(), hopital.getVille(),
                hopital.getCodePostal(), hopital.getLitsDisponibles());

        // get the last inserted id
        int id = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Integer.class);

        // On insère les associations avec les spécialisations
        hopital.getSpecialisations().forEach(specialisation -> hopitalSpecialisationService.insert(id, specialisation.getId()));

        return id;
    }

    public void update(Hopital hopital) {
        String query = "UPDATE Hopital SET Nom = ?, Adresse = ?, Ville = ?, CodePostal = ?, LitsDisponibles = ? WHERE Id = ?";
        jdbcTemplate.update(query, hopital.getNom(), hopital.getAdresse(), hopital.getVille(),
                hopital.getCodePostal(), hopital.getLitsDisponibles(), hopital.getId());

        // On supprime d'abord toutes les associations existantes
        hopitalSpecialisationService.deleteByHopital(hopital.getId());

        // On réinsère les nouvelles associations
        hopital.getSpecialisations().forEach(specialisation -> hopitalSpecialisationService.insert(hopital.getId(), specialisation.getId()));
    }

    public void delete(int id) {
        // On supprime d'abord toutes les associations existantes
        hopitalSpecialisationService.deleteByHopital(id);

        // On supprime l'hôpital
        String query = "DELETE FROM Hopital WHERE Id = ?";
        jdbcTemplate.update(query, id);
    }

    private static final RowMapper<Hopital> rowMapper = (rs, rowNum) ->
            new Hopital(rs.getInt("Id"), rs.getString("Nom"), rs.getString("Adresse"), rs.getString("Ville"),
                    rs.getString("CodePostal"), rs.getInt("LitsDisponibles"));
}
