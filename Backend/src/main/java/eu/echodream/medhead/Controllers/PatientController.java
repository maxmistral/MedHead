package eu.echodream.medhead.Controllers;

import eu.echodream.medhead.Services.PatientService;
import eu.echodream.medhead.Models.Patient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patient", description = "Endpoint de gestion des patients")
@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Operation(summary = "Récupère la liste des patients", tags = { "patient", "get" })
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Patient.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping
    public ResponseEntity<List<Patient>> getPatients() {
        List<Patient> patients = patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Récupère un patient à l'aide de son id", tags = { "patient", "get" })
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = Patient.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable int id) {
        try {
            Patient patient = patientService.get(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(patient);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Ajoute un nouveau patient", tags = { "patient", "post" })
    @ApiResponse(responseCode = "200", description = "Patient ajouté avec succès")
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @PostMapping
    public ResponseEntity<?> addPatient(@RequestBody Patient patient) {
        int id = patientService.insert(patient);
        return ResponseEntity.ok(id); // On retourne directement l'ID du nouveau patient
    }

    @Operation(summary = "Met à jour un patient", tags = { "patient", "put" })
    @ApiResponse(responseCode = "200", description = "Patient mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable int id, @RequestBody Patient patient) {
        try {
            Patient existingPatient = patientService.get(id);
            if (existingPatient == null) {
                return ResponseEntity.notFound().build();
            }
            patient.setId(id);
            patientService.update(patient);
            return ResponseEntity.ok("Patient mis à jour avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprime un patient", tags = { "patient", "delete" })
    @ApiResponse(responseCode = "200", description = "Patient supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable int id) {
        try {
            Patient existingPatient = patientService.get(id);
            if (existingPatient == null) {
                return ResponseEntity.notFound().build();
            }
            patientService.delete(id);
            return ResponseEntity.ok("Patient supprimé avec succès");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
