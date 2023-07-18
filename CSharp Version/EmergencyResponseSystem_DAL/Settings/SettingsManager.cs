using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using YamlDotNet.Serialization.NamingConventions;
using YamlDotNet.Serialization;

namespace EmergencyResponseSystem_DAL.Settings
{
    public class SettingsManager
    {
        private static string _ConfigFile = "config.yml";
        public static SettingsManager Instance { get; } = new SettingsManager();
        public SettingsManager()
        {
            // On créer le fichier de configuration s'il n'existe pas et le remplit avec les valeurs par défaut.
            if (!File.Exists(_ConfigFile))
            {
                File.Create(_ConfigFile).Close();
            }
        }

        public string GetDatasource()
        {
            return GetConfig("Datasource", "MedHead");
        }
        public void SetDatasource(string datasource)
        {
            SetConfig("Datasource", datasource);
        }

        public string GetDatabasePassword()
        {
            return GetConfig("DatabasePassword", null);
        }

        public void SetDatabasePassword(string password)
        {
            SetConfig("DatabasePassword", password);
        }

        /// <summary>
        /// Défini la valeur d'une clé dans le fichier de configuration
        /// </summary>
        /// <param name="key"></param>
        /// <param name="value"></param>
        private void SetConfig(string key, string value)
        {
            // On charge le contenu du fichier
            string content = File.ReadAllText(_ConfigFile);

            // On désérialise le contenu du fichier YAML
            var deserializer = new DeserializerBuilder().WithNamingConvention(CamelCaseNamingConvention.Instance).Build();
            var config = deserializer.Deserialize<Dictionary<string, string>>(content) ?? new Dictionary<string, string>();

            // On met à jour la valeur de la clé
            config[key] = value;

            // On resérialise le contenu mis à jour
            var serializer = new SerializerBuilder().WithNamingConvention(CamelCaseNamingConvention.Instance).Build();
            var updatedContent = serializer.Serialize(config);

            // On écrit le contenu mis à jour dans le fichier config.yml
            File.WriteAllText(_ConfigFile, updatedContent);
        }

        /// <summary>
        /// Récupère la valeur d'une clé dans le fichier de configuration
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        private string GetConfig(string key, string? defaultValue = null)
        {
            // On lit le contenu du fichier config.yml
            var content = File.ReadAllText(_ConfigFile);

            // On désérialise le contenu YAML
            var deserializer = new DeserializerBuilder().WithNamingConvention(CamelCaseNamingConvention.Instance).Build();
            var config = deserializer.Deserialize<Dictionary<string, string>>(content) ?? new Dictionary<string, string>();

            // On renvoi la valeur de la clé si elle existe, sinon, on renvoi null
            return config.ContainsKey(key) ? config[key] : defaultValue;
        }
    }
}
