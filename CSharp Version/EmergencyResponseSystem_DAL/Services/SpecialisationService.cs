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
    public class SpecialisationService
    {
        public async Task<List<Specialisation>> GetAllAsync()
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Libelle FROM Specialisation";
                var result = await connection.QueryAsync<Specialisation>(query);
                return result.ToList();
            }
        }

        /// <summary>
        /// Récupère une spécialisation par son Id
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        public async Task<Specialisation> GetAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Libelle FROM Specialisation WHERE Id = @Id";
                var result = await connection.QuerySingleOrDefaultAsync<Specialisation>(query, new { Id = id });
                return result;
            }
        }
        public async Task<List<Specialisation>> GetByHopitalAsync(int hopitalId)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT s.Id, s.Libelle FROM Specialisation s INNER JOIN HopitalSpecialisation hs ON s.Id = hs.SpecialisationId WHERE hs.HopitalId = @HopitalId";
                var result = await connection.QueryAsync<Specialisation>(query, new { HopitalId = hopitalId });
                return result.ToList();
            }
        }

        public async Task<int> InsertAsync(Specialisation specialisation)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "INSERT INTO Specialisation (Libelle) VALUES (@Libelle); SELECT last_insert_rowid();";
                var id = await connection.ExecuteScalarAsync<int>(query, specialisation);
                return id;
            }
        }

        public async Task UpdateAsync(Specialisation specialisation)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "UPDATE Specialisation SET Libelle = @Libelle WHERE Id = @Id";
                await connection.ExecuteAsync(query, specialisation);
            }
        }

        public async Task DeleteAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "DELETE FROM Specialisation WHERE Id = @Id";
                await connection.ExecuteAsync(query, new { Id = id });
            }
        }
    }
}