using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;

using Hashtable=System.Collections.Hashtable;
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
             //lab7
             IAgencyService server = new ServerProxy("127.0.0.1", 55555);
            ClientController ctrl = new ClientController(server);
            Login win = new Login(ctrl);
            Application.Run(win);
            /*BinaryServerFormatterSinkProvider serverProv = new BinaryServerFormatterSinkProvider();
            serverProv.TypeFilterLevel = System.Runtime.Serialization.Formatters.TypeFilterLevel.Full;
            BinaryClientFormatterSinkProvider clientProv = new BinaryClientFormatterSinkProvider();
            IDictionary props = new Hashtable();

            props["port"] = 0;
            TcpChannel channel = new TcpChannel(props, clientProv, serverProv);
            ChannelServices.RegisterChannel(channel, false);
            IAgencyService services =
                (IAgencyService)Activator.GetObject(typeof(IAgencyService), "tcp://localhost:55555/Agency");
          
            ClientController ctrl = new ClientController(services);
            Login win = new Login(ctrl);
            Application.Run(win);*/
            
        }
        [DllImport("kernel32.dll")]
        static extern bool AttachConsole(int dwProcessId);
    }
}
