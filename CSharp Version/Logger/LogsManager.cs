using System.ComponentModel;

namespace Logger
{
    public static class LogsManager
    {
        public static void WriteLogs(string message, Verbose level)
        {
            if ((int)level <= (int)Verbose)
            {
                string verbose = $"[{Enum.GetName(typeof(Verbose), level).ToUpper()}]";
                string dateTime = $"[{DateTime.Now.ToString("dd-MM-yyyy HH:mm:ss")}] ";
                Console.ForegroundColor = ConsoleColor.DarkGreen;
                Console.Write(dateTime);
                Console.ForegroundColor = ConsoleColor.Blue;
                if (level == Verbose.Warning)
                {
                    Console.ForegroundColor = ConsoleColor.Yellow;
                }
                if (level == Verbose.Error)
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                }
                if (level == Verbose.Trace)
                {
                    Console.ForegroundColor = ConsoleColor.Magenta;
                }
                Console.Write($"{verbose}");
                Console.ResetColor();
                Console.Write($" : {message}");
                Console.WriteLine();
                WriteLogsInFile(dateTime + verbose + " : " + message);
            }
        }
        private static void WriteLogsInFile(string logs)
        {
            if (!Directory.Exists("Logs"))
            {
                Directory.CreateDirectory("Logs");
            }
            if (!File.Exists("Logs" + Path.DirectorySeparatorChar + DateTime.Now.ToString("dd-MM-yyyy") + ".log"))
            {
                File.Create($"Logs" + Path.DirectorySeparatorChar + DateTime.Now.ToString("dd-MM-yyyy") + ".log").Close();

            }
            using (StreamWriter sw = File.AppendText("Logs" + Path.DirectorySeparatorChar + DateTime.Now.ToString("dd-MM-yyyy") + ".log"))
            {
                sw.WriteLine(logs);
                sw.Close();
            }
        }
        public static Verbose Verbose { get; set; }
    }

    public enum Verbose
    {
        [Description("Renvoi uniquement les messages systèmes indispensable.")]
        System = 0,
        [Description("Renvoi uniquement les erreurs.")]
        Error = 1,
        [Description("Renvoi les erreurs ainsi que les informations nécessitant une attention particulière.")]
        Warning = 2,
        [Description("Renvoi les erreurs, les informations nécessitant une attention particulière ainsi que les messages informatifs.")]
        Info = 3,
        [Description("Renvoi le maximum d'informations pour permettre un meilleur débogague.")]
        Trace = 4,
    }
}