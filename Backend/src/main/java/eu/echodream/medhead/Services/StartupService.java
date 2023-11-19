package eu.echodream.medhead.Services;

import eu.echodream.medhead.Models.Hopital;
import eu.echodream.medhead.Models.Patient;
import eu.echodream.medhead.Models.Specialisation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StartupService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SpecialisationService specialisationService;

    @Autowired
    private HopitalService hopitalService;

    @Autowired
    private PatientService patientService;

    //
    @PostConstruct
    public void initDb() {
        createTables();
        if (isDatabaseEmpty()) {
            List<Specialisation> specialisations = fillSpecialisations();
            System.out.println(specialisations.toString());
            // Jeu de données test.
            fillHospitalsAndPatients(specialisations);
        }
    }

    private boolean isDatabaseEmpty() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Specialisation", Integer.class);
        return (count != null && count == 0);
    }

    private void createTables() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Patient (Id INTEGER PRIMARY KEY AUTOINCREMENT, Nom TEXT NOT NULL, Prenom TEXT NOT NULL, NomJF TEXT, DateNaissance TEXT NOT NULL, Adresse TEXT NOT NULL, Ville TEXT NOT NULL, CodePostal TEXT NOT NULL, NumINSEE TEXT NOT NULL, HopitalActuel INTEGER NOT NULL DEFAULT -1, SpecialiteRequise INTEGER NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Hopital (Id INTEGER PRIMARY KEY AUTOINCREMENT, Nom TEXT NOT NULL, Adresse TEXT NOT NULL, Ville TEXT NOT NULL, CodePostal TEXT NOT NULL, LitsDisponibles INTEGER NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Specialisation (Id INTEGER PRIMARY KEY AUTOINCREMENT, Libelle TEXT NOT NULL, Categorie TEXT NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS HopitalSpecialisation (HopitalId INTEGER, SpecialisationId INTEGER, PRIMARY KEY (HopitalId, SpecialisationId), FOREIGN KEY (HopitalId) REFERENCES Hopital (Id) ON DELETE CASCADE, FOREIGN KEY (SpecialisationId) REFERENCES Specialisation (Id) ON DELETE CASCADE)");
    }

    private List<Specialisation> fillSpecialisations() {
        List<Specialisation> specialisations = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource("specialites.csv");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                reader.lines().skip(1).forEach(line -> {
                    String[] parts = line.split(";");
                    String libelle = parts[0].trim();
                    String categorie = parts[1].trim();
                    Specialisation specialisation = new Specialisation(0, libelle, categorie);
                    specialisation.setId(specialisationService.insert(specialisation));
                    specialisations.add(specialisation);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specialisations;
    }

    private void fillHospitalsAndPatients(List<Specialisation> specialisations) {

        // On split la liste de specialisations en 4 sous-liste pour assigner des specialisations à nos hopitaux tests.
        int size = specialisations.size();
        ArrayList<Specialisation> groupe1 = new ArrayList<>(specialisations.subList(0, size / 4));
        ArrayList<Specialisation> groupe2 = new ArrayList<>(specialisations.subList(size / 4, size / 2));
        ArrayList<Specialisation> groupe3 = new ArrayList<>(specialisations.subList(size / 2, 3 * size / 4));
        ArrayList<Specialisation> groupe4 = new ArrayList<>(specialisations.subList(3 * size / 4, size));

        // On rempli la table avec un jeu de données d'hopitaux piochés aléatoirement. Les spécialités associées ne sont absolument pas représentatives des spécialités réellement proposées par ces centres hospitaliers.
        List<Hopital> hopitaux = new ArrayList<>();
        hopitaux.add(new Hopital(0, "Hôpital de la Croix-Rousse", "103 Gd Rue de la Croix-Rousse", "Lyon", "69004", 100, groupe1));
        hopitaux.add(new Hopital(1, "Hôpital Pellegrin Bordeaux", "Pl. Amélie Raba Léon", "Bordeaux", "33000", 200, groupe2));
        hopitaux.add(new Hopital(2, "Hôpital Paris Saint-Joseph", "185 Rue Raymond Losserand", "Paris", "75014", 200, groupe3));
        hopitaux.add(new Hopital(3, "Hôpital privé de la Loire - Ramsay Santé", "A39 Bd de la Palle", "Saint-Étienne", "42100", 200, groupe4));

        for (Hopital hopital : hopitaux) {
            hopitalService.insert(hopital);
        }

        // On rempli la table avec un jeu de données de patients fictifs. Les adresses correspondent en réalité à des adresses de restaurants piochées aléatoirement sur Google Maps, ce afin de construire un jeu de données cohérent à des fins de tests.
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient(0, "Patient A", "Prenom A", "", Date.valueOf(LocalDate.of(1990, 1, 1)), "1 Cloître Notre Dame", "Chartres", "28000", "NumINSEE A", -1, 1));
        patients.add(new Patient(0, "Patient B", "Prenom B", "", Date.valueOf(LocalDate.of(1990, 1, 1)), "200 Rue de Bourgogne", "Orléans", "45000", "NumINSEE B", -1, 2));
        patients.add(new Patient(0, "Patient C", "Prenom C", "", Date.valueOf(LocalDate.of(1990, 1, 1)), "34 Rue Palais Grillet", "Lyon", "69002", "NumINSEE C", -1, 2));
        patients.add(new Patient(0, "Patient D", "Prenom D", "", Date.valueOf(LocalDate.of(1990, 1, 1)), "10 Rue Labeda", "Toulouse", "31000", "NumINSEE D", -1, 2));

        for (Patient patient : patients) {
            patientService.insert(patient);
        }
    }
}
