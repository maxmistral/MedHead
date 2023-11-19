using Dapper;
using EmergencyResponseSystem.Models;
using EmergencyResponseSystem_DAL.Services;
using EmergencyResponseSystem_DAL.Settings;
using EmergencyResponseSystem_Models;
using Logger;
using MedHead_DAL;
using System.Data;
using System.Data.SQLite;

namespace EmergencyResponseSystem_DAL.Database
{
    public class DBManager
    {
        private static SQLiteConnection _CurrentConnection = null;

        /// <summary>
        /// Retourne la connexion à la base de données courante ou en crée une nouvelle si celle-ci n'existe pas ou est fermée.
        /// </summary>
        /// <param name="bNewConnection"></param>
        /// <returns></returns>
        public static SQLiteConnection GetDBConnexion(bool bNewConnection = false)
        {
            if (bNewConnection)
            {
                return GetNewDBConnection();
            }
            else
            {
                if (_CurrentConnection == null || _CurrentConnection.State == ConnectionState.Closed)
                {
                    _CurrentConnection = GetNewDBConnection();
                }
                return _CurrentConnection;
            }

        }
        private static SQLiteConnection GetNewDBConnection()
        {
            SQLiteConnection sqlite_conn;
            // On crée la connexion à la base de données
            sqlite_conn = new SQLiteConnection($"Data Source={SettingsManager.Instance.GetDatasource()}.db;Cache=Shared;");
            sqlite_conn.SetPassword(SettingsManager.Instance.GetDatabasePassword());
            // On ouvre la connexion
            try
            {
                sqlite_conn.Open();
            }
            catch (Exception ex)
            {
                LogsManager.WriteLogs(ex.ToString(), Verbose.Error);
                return null;
            }
            return sqlite_conn;
        }

        /// <summary>
        /// Initialise la base de données en créant les tables si celles-ci n'existent pas.
        /// </summary>
        public static async void InitDB()
        {
            // Création des tables
            await CreerTablePatient();
            await CreerTableHopital();
            await CreerTableSpecialisation();
            await CreerTableHopitalSpecialisation();
        }


        /// <summary>
        /// Fonction gérant la cration de la table Patient si elle n'existe pas.
        /// </summary>
        /// <returns></returns>
        private static async Task<bool> CreerTablePatient()
        {
            using (var connection = GetDBConnexion(true))
            {
                try
                {
                    var query = @"
                    CREATE TABLE IF NOT EXISTS Patient
                    (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Nom TEXT NOT NULL,
                        Prenom TEXT NOT NULL,
                        NomJF TEXT,
                        DateNaissance TEXT NOT NULL,
                        Adresse TEXT NOT NULL,
                        Ville TEXT NOT NULL,
                        CodePostal TEXT NOT NULL,
                        NumINSEE TEXT NOT NULL,
                        HopitalActuel INTEGER NOT NULL DEFAULT -1,
                        SpecialiteRequise INTEGER NOT NULL
                    )";

                    await connection.ExecuteAsync(query);
                    return true;
                }
                catch (Exception ex)
                {
                    LogsManager.WriteLogs(ex.ToString(), Verbose.Error);
                    return false;
                }
            }
        }
        /// <summary>
        /// Fonction gérant la cration de la table Hopital si elle n'existe pas.
        /// </summary>
        /// <returns></returns>
        private static async Task<bool> CreerTableHopital()
        {
            using (var connection = GetDBConnexion(true))
            {
                try
                {
                    var query = @"
                    CREATE TABLE IF NOT EXISTS Hopital
                    (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Nom TEXT NOT NULL,
                        Adresse TEXT NOT NULL,
                        Ville TEXT NOT NULL,
                        CodePostal TEXT NOT NULL,
                        LitsDisponibles INTEGER NOT NULL
                    )";

                    await connection.ExecuteAsync(query);
                    return true;
                }
                catch (Exception ex)
                {
                    LogsManager.WriteLogs(ex.ToString(), Verbose.Error);
                    return false;
                }
            }
        }

        /// <summary>
        /// Fonction gérant la création de la table Specialisation si elle n'existe pas.
        /// </summary>
        /// <returns></returns>
        private static async Task<bool> CreerTableSpecialisation()
        {
            using (var connection = GetDBConnexion(true))
            {
                try
                {
                    var query = @"
                    CREATE TABLE IF NOT EXISTS Specialisation
                    (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Libelle TEXT NOT NULL,
                        Categorie TEXT NOT NULL
                    )";

                    await connection.ExecuteAsync(query);
                    return true;
                }
                catch (Exception ex)
                {
                    LogsManager.WriteLogs(ex.ToString(), Verbose.Error);
                    return false;
                }
            }
        }

        /// <summary>
        /// Fonction gérant la création de la table d'association HopitalSpecialisation si elle n'existe pas.
        /// </summary>
        /// <returns></returns>
        private static async Task<bool> CreerTableHopitalSpecialisation()
        {
            using (var connection = GetDBConnexion(true))
            {
                try
                {
                    var query = @"
                    CREATE TABLE IF NOT EXISTS HopitalSpecialisation
                    (
                        HopitalId INTEGER,
                        SpecialisationId INTEGER,
                        PRIMARY KEY (HopitalId, SpecialisationId),
                        FOREIGN KEY (HopitalId) REFERENCES Hopital (Id) ON DELETE CASCADE,
                        FOREIGN KEY (SpecialisationId) REFERENCES Specialisation (Id) ON DELETE CASCADE
                    )";

                    await connection.ExecuteAsync(query);
                    return true;
                }
                catch (Exception ex)
                {
                    LogsManager.WriteLogs(ex.ToString(), Verbose.Error);
                    return false;
                }
            }
        }



        public static async Task CreerDataset()
        {
            using (var connection = GetDBConnexion(true))
            {
                var patientService = new PatientService();
                var specialisationService = new SpecialisationService();
                var hopitalSpecialisationService = new HopitalSpecialisationService();
                var hopitalService = new HopitalService(specialisationService, hopitalSpecialisationService);

                var specialisations = new List<Specialisation>();

                // Fichier ressources incorporés à l'assembly.
                var csvString = Ressources.specialites;

                using (var reader = new StringReader(csvString))
                {
                    // On ignore la ligne d'en-ytête
                    reader.ReadLine();

                    while (true)
                    {
                        var line = reader.ReadLine();
                        if (line == null)
                            break;

                        var values = line.Split(';');

                        if (values.Length >= 2)
                        {
                            var libelle = values[1].Trim();
                            var categorie = values[0].Trim();

                            var specialisation = new Specialisation
                            {
                                Libelle = libelle,
                                Categorie = categorie
                            };

                            specialisations.Add(specialisation);
                        }
                    }
                }
                foreach (var specialisation in specialisations)
                {
                    await specialisationService.InsertAsync(specialisation);
                }

                int count = specialisations.Count;
                int chunkSize = count / 4;

                var sousListe1 = specialisations.GetRange(0, chunkSize);
                var sousListe2 = specialisations.GetRange(chunkSize, chunkSize);
                var sousListe3 = specialisations.GetRange(2 * chunkSize, chunkSize);
                var sousListe4 = specialisations.GetRange(3 * chunkSize, count - 3 * chunkSize);
                // Création des hôpitaux
                var hopitaux = new List<Hopital>
            {
                new Hopital { Nom = "Hôpital A", Adresse = "Adresse A", Ville = "Ville A", CodePostal = "Code A", LitsDisponibles = 100, Specialisations = sousListe1},
                new Hopital { Nom = "Hôpital B", Adresse = "Adresse B", Ville = "Ville B", CodePostal = "Code B", LitsDisponibles = 200, Specialisations = sousListe2 },
                new Hopital { Nom = "Hôpital C", Adresse = "Adresse C", Ville = "Ville C", CodePostal = "Code C", LitsDisponibles = 300, Specialisations = sousListe3 },
                new Hopital { Nom = "Hôpital D", Adresse = "Adresse D", Ville = "Ville D", CodePostal = "Code D", LitsDisponibles = 400, Specialisations = sousListe4 }
            };
                foreach (var hopital in hopitaux)
                {
                    await hopitalService.InsertAsync(hopital);
                }
                // Créations des associations hôpital / spécialisation
                foreach (var hopital in hopitaux)
                {
                    var hopitalId = await hopitalService.InsertAsync(hopital);

                    // On part du principe que chaque hôpital a toutes les spécialisations
                    foreach (var specialisation in specialisations)
                    {
                        await hopitalSpecialisationService.InsertAsync(hopitalId, specialisation.Id);
                    }
                }
                // Création des patients
                var patients = new List<Patient>
            {
                new Patient { Nom = "Patient A", Prenom = "Prenom A", NomJF = "", DateNaissance = DateTime.Now.AddYears(-30), Adresse = "Adresse A", Ville = "Ville A", CodePostal = "CodePostal A", NumINSEE = "NumINSEE A", HopitalActuel = -1, SpecialiteRequise = 1 },
                new Patient { Nom = "Patient B", Prenom = "Prenom B", NomJF = "", DateNaissance = DateTime.Now.AddYears(-40), Adresse = "Adresse B", Ville = "Ville B", CodePostal = "CodePostal B", NumINSEE = "NumINSEE B", HopitalActuel = -1, SpecialiteRequise = 2 },
                new Patient { Nom = "Patient C", Prenom = "Prenom C", NomJF = "", DateNaissance = DateTime.Now.AddYears(-50), Adresse = "Adresse C", Ville = "Ville C", CodePostal = "CodePostal C", NumINSEE = "NumINSEE C", HopitalActuel = -1, SpecialiteRequise = 3 },
                new Patient { Nom = "Patient D", Prenom = "Prenom D", NomJF = "", DateNaissance = DateTime.Now.AddYears(-60), Adresse = "Adresse D", Ville = "Ville D", CodePostal = "CodePostal D", NumINSEE = "NumINSEE D", HopitalActuel = -1, SpecialiteRequise = 4 }
            };
                foreach (var patient in patients)
                {
                    await patientService.InsertAsync(patient);
                }
            }
        }
    }
}