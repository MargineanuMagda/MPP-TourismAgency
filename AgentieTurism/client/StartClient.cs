using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using services;
using networking;
namespace client
{
    static class StartClient
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            AttachConsole(-1);
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            IAgencyService server = new ServerProxy("127.0.0.1", 55555);
            ClientController ctrl = new ClientController(server);
            Login win = new Login(ctrl);
            Application.Run(win);
        }
        [DllImport("kernel32.dll")]
        static extern bool AttachConsole(int dwProcessId);
    }
}
