using Dapper;
using EmergencyResponseSystem_DAL.Database;
using EmergencyResponseSystem_Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmergencyResponseSystem_DAL.Services
{
    public class HopitalSpecialisationService
    {
        public async Task InsertAsync(int hopitalId, int specialisationId)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var checkQuery = "SELECT 1 FROM HopitalSpecialisation WHERE HopitalId = @HopitalId AND SpecialisationId = @SpecialisationId";
                var exists = await connection.QueryFirstOrDefaultAsync<int?>(checkQuery, new { HopitalId = hopitalId, SpecialisationId = specialisationId });
                if (exists == null)
                {
                    var insertQuery = "INSERT INTO HopitalSpecialisation (HopitalId, SpecialisationId) VALUES (@HopitalId, @SpecialisationId)";
                    await connection.ExecuteAsync(insertQuery, new { HopitalId = hopitalId, SpecialisationId = specialisationId });
                }
                else
                {
                    // Handle the case where the record already exists
                }
            }
        }

        public async Task DeleteAsync(int hopitalId, int specialisationId)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "DELETE FROM HopitalSpecialisation WHERE HopitalId = @HopitalId AND SpecialisationId = @SpecialisationId";
                await connection.ExecuteAsync(query, new { HopitalId = hopitalId, SpecialisationId = specialisationId });
            }
        }
        public async Task DeleteByHopitalAsync(int hopitalId)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "DELETE FROM HopitalSpecialisation WHERE HopitalId = @HopitalId";
                await connection.ExecuteAsync(query, new { HopitalId = hopitalId });
            }
        }

    }
}
