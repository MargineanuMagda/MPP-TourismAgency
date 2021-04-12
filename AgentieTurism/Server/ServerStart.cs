using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using services;
using persistence;
using networking;
using System.Net.Sockets;

using System.Threading;

namespace Server
{
    class ServerStart
    {
        static void Main(string[] args)
        {
            TripRepository repoTrips = new TripDBRepository();

            AgentRepository repoAgents = new AgentDBRepository();

            ReservationRepository repoReservation = new ReservationDBRepository();

            IAgencyService serviceImpl = new AgencyServiceImpl(repoTrips, repoAgents, repoReservation);

            SerialAgencyServer server = new SerialAgencyServer("127.0.0.1", 55555, serviceImpl);
            server.Start();
            Console.WriteLine("Server started ...");
            //Console.WriteLine("Press <enter> to exit...");
            Console.ReadLine();
        }
    }
    public class SerialAgencyServer : ConcurrentServer
    {
        private IAgencyService server;
        private ClientWorker worker;
        public SerialAgencyServer(string host, int port, IAgencyService server) : base(host, port)
        {
            this.server = server;
            Console.WriteLine("SerialChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
}
