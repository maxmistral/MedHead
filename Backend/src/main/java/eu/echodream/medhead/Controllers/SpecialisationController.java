package eu.echodream.medhead.Controllers;

import eu.echodream.medhead.Models.Specialisation;
import eu.echodream.medhead.Services.SpecialisationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Specialisation", description = "Endpoint de gestion des spécialisations.")
@RestController
@RequestMapping("/api/specialisation")
public class SpecialisationController {

    @Autowired
    private SpecialisationService specialisationService;

    @Operation(summary = "Récupère toutes les spécialisations", tags = { "specialisation", "get" })
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Specialisation.class), mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<List<Specialisation>> getAllSpecialisations() {
        List<Specialisation> specialisations = specialisationService.getAll();
        return ResponseEntity.ok(specialisations);
    }

    @Operation(summary = "Récupère une spécialisation par son ID", tags = { "specialisation", "get" })
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Specialisation.class), mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Spécialisation non trouvée")
    @GetMapping("/{id}")
    public ResponseEntity<Specialisation> getSpecialisationById(@PathVariable int id) {
        try {
            Specialisation specialisation = specialisationService.get(id);
            if (specialisation == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(specialisation);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ajoute une nouvelle spécialisation", tags = { "specialisation", "post" })
    @ApiResponse(responseCode = "200", description = "Spécialisation ajoutée avec succès")
    @PostMapping
    public ResponseEntity<?> addSpecialisation(@RequestBody Specialisation specialisation) {
        int id = specialisationService.insert(specialisation);
        return ResponseEntity.ok(id); // On retourne directement l'ID de la nouvelle spécialité
    }

    @Operation(summary = "Met à jour une spécialisation", tags = { "specialisation", "put" })
    @ApiResponse(responseCode = "200", description = "Spécialisation mise à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Spécialisation non trouvée")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpecialisation(@PathVariable int id, @RequestBody Specialisation specialisation) {
        try {
            Specialisation existingSpecialisation = specialisationService.get(id);
            if (existingSpecialisation == null) {
                return ResponseEntity.notFound().build();
            }
            specialisation.setId(id);
            specialisationService.update(specialisation);
            return ResponseEntity.ok("Spécialisation mise à jour avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprime une spécialisation", tags = { "specialisation", "delete" })
    @ApiResponse(responseCode = "200", description = "Spécialisation supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Spécialisation non trouvée")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpecialisation(@PathVariable int id) {
        try {
            Specialisation existingSpecialisation = specialisationService.get(id);
            if (existingSpecialisation == null) {
                return ResponseEntity.notFound().build();
            }
            specialisationService.delete(id);
            return ResponseEntity.ok("Spécialisation supprimée avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
