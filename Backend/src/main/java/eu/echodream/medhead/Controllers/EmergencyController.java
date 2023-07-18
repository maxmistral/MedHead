package eu.echodream.medhead.Controllers;

import eu.echodream.medhead.Services.HopitalService;
import eu.echodream.medhead.Services.PatientService;
import eu.echodream.medhead.Services.SpecialisationService;
import eu.echodream.medhead.Models.Hopital;
import eu.echodream.medhead.Models.Specialisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    @Autowired
    private HopitalService hopitalService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private SpecialisationService specialisationService;

    @GetMapping
    public ResponseEntity<List<Hopital>> getHospitals() {
        List<Hopital> hopitaux = hopitalService.getAll();
        return ResponseEntity.ok(hopitaux);
    }

    @GetMapping("/getdisponibility/{idSpecialisation}")
    public ResponseEntity<List<Hopital>> getDisponibility(@PathVariable int idSpecialisation) {
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
    }
}
