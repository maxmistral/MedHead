package eu.echodream.medhead.Controllers;

import eu.echodream.medhead.Models.Coordinate;
import eu.echodream.medhead.Models.Hopital;
import eu.echodream.medhead.Services.HopitalService;
import eu.echodream.medhead.Utils.MapEngine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Hospital", description = "Endpoint de gestion des hopitaux.")
@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    @Autowired
    private HopitalService hopitalService;

    @Operation(summary = "Récupère la liste des hôpitaux", tags = { "hospital", "get" })
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Hopital.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping
    public ResponseEntity<List<Hopital>> getHospitals() {
        List<Hopital> hopitaux = hopitalService.getAll();
        return ResponseEntity.ok(hopitaux);
    }

    @Operation(summary = "Récupère un hôpital par ID", tags = { "hospital", "get" })
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Hopital.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping("/{id}")
    public ResponseEntity<Hopital> getHospitalById(@PathVariable int id) {
        try {
            Hopital hopital = hopitalService.get(id);
            if (hopital == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(hopital);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Récupère les hôpitaux dans un rayon spécifique", tags = { "hospital", "get" })
    @ApiResponse(responseCode = "200", description = "Liste des hôpitaux trouvés",
            content = {@Content(schema = @Schema(implementation = Hopital.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @GetMapping("/getHospitalByDistance/{distance}")
    public ResponseEntity<List<Hopital>> getHospitalByDistance(@PathVariable double distance, @RequestParam double lat, @RequestParam double lon) {
        try {
            List<Hopital> hopitaux = hopitalService.getAll();
            List<Hopital> hopitauxDansRayon = new ArrayList<>();
            for (Hopital hopital : hopitaux) {
                Coordinate coordinates = MapEngine.getCoordinates(hopital.getAdresse(), hopital.getVille(), hopital.getCodePostal());
                if (coordinates != null) {
                    double hopitalDistance = MapEngine.getDistance(lat, lon, coordinates.getLat(), coordinates.getLng());
                    if (hopitalDistance <= distance) {
                        hopitauxDansRayon.add(hopital);
                    }
                }
            }
            return ResponseEntity.ok(hopitauxDansRayon);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Ajoute un nouvel hôpital", tags = { "hospital", "post" })
    @ApiResponse(responseCode = "200", description = "Hôpital ajouté, ID renvoyé",
            content = {@Content(schema = @Schema(implementation = Integer.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @PostMapping
    public ResponseEntity<?> addHospital(@RequestBody Hopital hopital) {
        int id = hopitalService.insert(hopital);
        return ResponseEntity.ok(id); // On retourne directement l'ID du nouveau hopital
    }

    @Operation(summary = "Mise à jour d'un hôpital par ID", tags = { "hospital", "put" })
    @ApiResponse(responseCode = "200", description = "Hôpital mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Hôpital non trouvé")
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHospital(@PathVariable int id, @RequestBody Hopital hopital) {
        try {
            Hopital existingHopital = hopitalService.get(id);
            if (existingHopital == null) {
                return ResponseEntity.notFound().build();
            }
            hopital.setId(id);
            hopitalService.update(hopital);
            return ResponseEntity.ok("Hôpital mis à jour avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprime un hôpital par ID", tags = { "hospital", "delete" })
    @ApiResponse(responseCode = "200", description = "Hôpital supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Hôpital non trouvé")
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHospital(@PathVariable int id) {
        try {
            Hopital existingHopital = hopitalService.get(id);
            if (existingHopital == null) {
                return ResponseEntity.notFound().build();
            }
            hopitalService.delete(id);
            return ResponseEntity.ok("Hôpital supprimé avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
