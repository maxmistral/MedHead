package eu.echodream.medhead.Controllers;

import eu.echodream.medhead.Models.Coordinate;
import eu.echodream.medhead.Models.Patient;
import eu.echodream.medhead.Services.HopitalService;
import eu.echodream.medhead.Services.PatientService;
import eu.echodream.medhead.Services.SpecialisationService;
import eu.echodream.medhead.Models.Hopital;
import eu.echodream.medhead.Models.Specialisation;
import eu.echodream.medhead.Utils.MapEngine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Emergency", description = "Endpoint de gestion des urgences, récupération des disponibilités, etc...")
@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    @Autowired
    private HopitalService hopitalService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SpecialisationService specialisationService;

    @Operation(summary = "Récupère la liste des hopitaux disponibles par ID de spécialisation",
            tags = { "emergency", "get" })
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Hopital.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping("/getdisponibility/{idSpecialisation}")
    public ResponseEntity<List<Hopital>> getDisponibility(@PathVariable int idSpecialisation) {
        try {
        Specialisation specialisation = specialisationService.get(idSpecialisation);

        if (specialisation == null) {
            return ResponseEntity.notFound().build();
        }

        List<Hopital> hopitaux = hopitalService.getAll();
        List<Hopital> hopitauxAvecSpecialisation = hopitaux.stream()
                .filter(hop -> hop.getSpecialisations().stream()
                        .anyMatch(spe -> spe.getId() == idSpecialisation))
                .collect(Collectors.toList());

        return ResponseEntity.ok(hopitauxAvecSpecialisation);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/matchHospital/{idPatient}")
    @Operation(summary = "Associe un patient à l'hôpital le plus proche avec la spécialisation requise et des lits disponibles",
            tags = { "emergency", "match" })
    @ApiResponse(responseCode = "200", description = "Hôpital correspondant trouvé",
            content = {@Content(schema = @Schema(implementation = Hopital.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", description = "Patient ou hôpital correspondant non trouvé",
            content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur",
            content = {@Content(schema = @Schema())})
    public ResponseEntity<Hopital> matchHospital(@PathVariable int idPatient) {
        try {
            Patient patient = patientService.get(idPatient);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }

            Coordinate patientCoordinates = MapEngine.getCoordinates(
                    patient.getAdresse(), patient.getVille(), patient.getCodePostal());
            if (patientCoordinates == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            List<Hopital> hopitaux = hopitalService.getAll();
            Hopital hopitalCorrespondant = null;
            double closestDistance = Double.MAX_VALUE;

            for (Hopital hopital : hopitaux) {
                boolean hasRequiredSpecialisation = hopital.getSpecialisations().stream()
                        .anyMatch(spe -> spe.getId() == patient.getSpecialiteRequise());
                if (hasRequiredSpecialisation && hopital.getLitsDisponibles() > 0) {
                    Coordinate hopitalCoordinates = MapEngine.getCoordinates(
                            hopital.getAdresse(), hopital.getVille(), hopital.getCodePostal());
                    if (hopitalCoordinates != null) {
                        double distance = MapEngine.getDistance(
                                patientCoordinates.getLat(), patientCoordinates.getLng(),
                                hopitalCoordinates.getLat(), hopitalCoordinates.getLng());
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            hopitalCorrespondant = hopital;
                        }
                    }
                }
            }

            if (hopitalCorrespondant == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(hopitalCorrespondant);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
