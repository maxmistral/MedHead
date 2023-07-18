namespace EmergencyResponseSystem.Models
{
    public class Patient
    {
        public int Id { get; set; } // Identifiant unique du patient
        public string Nom { get; set; } // Nom de famille du patient
        public string Prenom { get; set; } // Prénom du patient
        public string NomJF { get; set; } // Nom de jeune fille du patient
        public DateTime DateNaissance { get; set; } // Date de naissance du patient
        public string Adresse { get; set; } // Adresse résidentielle du patient
        public string Ville { get; set; } // Ville de résidence du patient
        public string CodePostal { get; set; } // Code postal de la ville de résidence du patient
        public string NumINSEE { get; set; } // Numéro de sécurité sociale du patient
        public string Location { get; set; } // Emplacement actuel du patient
        public int SpecialiteRequise { get; set; } // Spécialité médicale dont le patient a besoin
    }

}
