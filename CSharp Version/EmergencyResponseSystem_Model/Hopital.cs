using EmergencyResponseSystem_Models;
using System.Collections.Generic;

namespace EmergencyResponseSystem.Models
{
    public class Hopital
    {
        public int Id { get; set; } // 
        public string Nom { get; set; }
        public string Adresse { get; set; }
        public string Ville { get; set; }
        public string CodePostal { get; set; }
        public int LitsDisponibles { get; set; }
        public List<Specialisation> Specialisations { get; set; } = new List<Specialisation>();
    }
}
