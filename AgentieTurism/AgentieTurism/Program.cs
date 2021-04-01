using System;

using AgentieTurism.repository;
using System.Windows.Forms;
using System.IO;
using System.Configuration;
using log4net;
using log4net.Config;
using AgentieTurism.service;


namespace AgentieTurism
{
    static class Program
    {
        [STAThread]
        static void InitWin()
        {
            if (Environment.OSVersion.Version.Major >= 6)
            {
                SetProcessDPIAware();
                Application.EnableVisualStyles();
            
                Application.SetCompatibleTextRenderingDefault(false);
            }
        }

        [System.Runtime.InteropServices.DllImport("user32.dll")]
        private static extern bool SetProcessDPIAware();
        static void Main(string[] args)
        {

            

            XmlConfigurator.Configure(new FileInfo(ConfigurationManager.AppSettings["log4net-config-file"]));

            TripRepository repoTrips = new TripDBRepository();
           
            AgentRepository repoAgents = new AgentDBRepository();
           
            ReservationRepository repoReservation = new ReservationDBRepository();
          
            ServiceAgency serv = new ServiceAgency(repoTrips, repoAgents, repoReservation);
            
            Login log = new Login(serv);
            
            Application.Run(log);
        }
    }
}
