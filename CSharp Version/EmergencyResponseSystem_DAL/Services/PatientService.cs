using Dapper;
using EmergencyResponseSystem.Models;
using EmergencyResponseSystem_DAL.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmergencyResponseSystem_DAL.Services
{
    public class PatientService
    {
        public async Task<List<Patient>> GetAllAsync()
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient";
                var result = await connection.QueryAsync<Patient>(query);
                return result.ToList();
            }
        }

        public async Task<Patient> GetAsyncByNumINSEE(string numINSEE)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient WHERE NumINSEE = @NumINSEE";
                var result = await connection.QuerySingleOrDefaultAsync<Patient>(query, new { NumINSEE = numINSEE });
                return result;
            }
        }
        public async Task<Patient> GetAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "SELECT Id, Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise FROM Patient WHERE Id = @Id";
                var result = await connection.QuerySingleOrDefaultAsync<Patient>(query, new { Id = id });
                return result;
            }
        }

        public async Task<string> InsertAsync(Patient patient)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "INSERT INTO Patient (Nom, Prenom, NomJF, DateNaissance, Adresse, Ville, CodePostal, NumINSEE, Location, SpecialiteRequise) VALUES (@Nom, @Prenom, @NomJF, @DateNaissance, @Adresse, @Ville, @CodePostal, @NumINSEE, @Location, @SpecialiteRequise)";
                await connection.ExecuteAsync(query, patient);
                return patient.NumINSEE;
            }
        }

        public async Task UpdateAsync(Patient patient)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "UPDATE Patient SET Nom = @Nom, Prenom = @Prenom, NomJF = @NomJF, DateNaissance = @DateNaissance, Adresse = @Adresse, Ville = @Ville, CodePostal = @CodePostal, Location = @Location, SpecialiteRequise = @SpecialiteRequise WHERE NumINSEE = @NumINSEE";
                await connection.ExecuteAsync(query, patient);
            }
        }

        public async Task DeleteAsyncByNumINSEE(string numINSEE)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "DELETE FROM Patient WHERE NumINSEE = @NumINSEE";
                await connection.ExecuteAsync(query, new { NumINSEE = numINSEE });
            }
        }
        public async Task DeleteAsync(int id)
        {
            using (var connection = DBManager.GetDBConnexion(true))
            {
                var query = "DELETE FROM Patient WHERE Id = @Id";
                await connection.ExecuteAsync(query, new { Id = id });
            }
        }
    }
}
