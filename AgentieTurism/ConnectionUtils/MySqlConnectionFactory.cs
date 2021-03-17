using System;
using System.Data;
using MySql.Data.MySqlClient;
using System.Configuration;
namespace ConnectionUtils
{

	public class MySqlConnectionFactory : ConnectionFactory
	{
		string GetAppSetting(Configuration config, string key)
		{
			KeyValueConfigurationElement element = config.AppSettings.Settings[key];
			if (element != null)
			{
				string value = element.Value;
				if (!string.IsNullOrEmpty(value))
					return value;
			}
			return string.Empty;
		}
		public override IDbConnection createConnection()
		{
			//MySql Connection
			
			Configuration config = null;
			string exeConfigPath = this.GetType().Assembly.Location;
			try
			{
				config = ConfigurationManager.OpenExeConfiguration(exeConfigPath);
			}
			catch (Exception ex)
			{
                Console.WriteLine("DLL couldn t real the app.config\n"+ex.Message);
			}

			if (config != null)
			{
				string connectionString = GetAppSetting(config, "dbConnection");
			
				return new MySqlConnection(connectionString);
			}

			return null;
           


		}
	}
}
