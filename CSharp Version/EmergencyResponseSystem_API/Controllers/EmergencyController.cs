using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using EmergencyResponseSystem.Models;
using EmergencyResponseSystem_DAL.Services;
using EmergencyResponseSystem_Models;
using Microsoft.AspNetCore.Mvc;

namespace EmergencyResponseSystem.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class EmergencyController : ControllerBase
    {
        private readonly HopitalService _hopitalService;
        private readonly PatientService _patientService;
        private readonly SpecialisationService _specialisationService;

        public EmergencyController(HopitalService hopitalService, PatientService patientService, SpecialisationService specialisationService)
        {
            _hopitalService = hopitalService;
            _patientService = patientService;
            _specialisationService = specialisationService;
        }

        // GET: api/Emergency
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Hopital>>> GetHospitals()
        {
            var hopitaux = await _hopitalService.GetAllAsync();
            return Ok(hopitaux);
        }

        // GET: api/Emergency
        [HttpGet, Route("getdisponibility/{idSpecialisation}")]
        public async Task<ActionResult<Hopital>> GetDisponibility(int idSpecialisation/*Patient patient*/)
        {

            Specialisation specialisation = await _specialisationService.GetAsync(idSpecialisation);
            if (specialisation == null)
            {
                return NotFound("La spécialisation n'existe pas.");
            }
            var hopitaux = await _hopitalService.GetAllAsync();
            List<Hopital> hopitauxAvecSpecialisation = hopitaux.Where(hop => hop.Specialisations.Where(spe => spe.Id == idSpecialisation).Count() > 0).ToList();
            //// On vérifie que le patient existe

            //// On choisi l'hôpital qui a le plus de lits disponibles
            //var hopitaux = await _hopitalService.GetAllAsync();
            //var hopitalChoisi = hopitaux.OrderByDescending(h => h.LitsDisponibles).FirstOrDefault();

            //if (hopitalChoisi == null)
            //{
            //    return NotFound("Aucun hôpital n'est disponible pour accueillir le patient.");
            //}

            // TODO: Mettre à jour le nombre de lits disponibles dans la base de données pour l'hôpital choisi

            return Ok(hopitauxAvecSpecialisation);
        }
    }
}
