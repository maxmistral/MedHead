using Dapper;
using EmergencyResponseSystem.Models;
using EmergencyResponseSystem_DAL.Database;
using EmergencyResponseSystem_Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmergencyResponseSystem_DAL.Services
{
    public class HopitalService
    {

        private readonly SpecialisationService _specialisationService;
        private readonly HopitalSpecialisationService _hopitalSpecialisationService;

        public HopitalService(SpecialisationService specialisationService, HopitalSpecialisationService hopitalSpecialisationService)
        {
            _specialisationService = specialisationService;
            _hopitalSpecialisationService = hopitalSpecialisationService;
        }

        public async Task<List<Hopital>> GetAllAsync()
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Nom, Adresse, Ville, CodePostal, LitsDisponibles FROM Hopital";
                var result = await connection.QueryAsync<Hopital>(query);
                foreach (var hopital in result)
                {
                    hopital.Specialisations = await _specialisationService.GetByHopitalAsync(hopital.Id);
                }
                return result.ToList();
            }
        }

        public async Task<Hopital> GetAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Nom, Adresse, Ville, CodePostal, LitsDisponibles FROM Hopital WHERE Id = @Id";
                var result = await connection.QuerySingleOrDefaultAsync<Hopital>(query, new { Id = id });
                if (result != null)
                {
                    result.Specialisations = await _specialisationService.GetByHopitalAsync(id);
                }
                return result;
            }
        }

        public async Task<int> InsertAsync(Hopital hopital)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "INSERT INTO Hopital (Nom, Adresse, Ville, CodePostal, LitsDisponibles) VALUES (@Nom, @Adresse, @Ville, @CodePostal, @LitsDisponibles); SELECT last_insert_rowid();";
                var id = await connection.ExecuteScalarAsync<int>(query, hopital);

                // On insère les associations avec les spécialisations
                foreach (var specialisation in hopital.Specialisations)
                {
                    await _hopitalSpecialisationService.InsertAsync(hopital.Id, specialisation.Id);
                }

                return id;
            }
        }

        public async Task UpdateAsync(Hopital hopital)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "UPDATE Hopital SET Nom = @Nom, Adresse = @Adresse, Ville = @Ville, CodePostal = @CodePostal, LitsDisponibles = @LitsDisponibles WHERE Id = @Id";
                await connection.ExecuteAsync(query, hopital);

                // On supprime d'abord toutes les associations existantes
                await _hopitalSpecialisationService.DeleteByHopitalAsync(hopital.Id);

                // On réinsère les nouvelles associations
                foreach (var specialisation in hopital.Specialisations)
                {
                    await _hopitalSpecialisationService.InsertAsync(hopital.Id, specialisation.Id);
                }
            }
        }
        public async Task DeleteAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                // On supprime d'abord toutes les associations existantes
                await _hopitalSpecialisationService.DeleteByHopitalAsync(id);

                // On supprime l'hôpital
                var query = "DELETE FROM Hopital WHERE Id = @Id";
                await connection.ExecuteAsync(query, new { Id = id });
            }
        }

    }
}
