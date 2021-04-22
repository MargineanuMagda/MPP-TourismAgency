using System;
using System.Collections;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Tcp;
using services;
using persistence;
using networking;
using System.Net.Sockets;
using Hashtable=System.Collections.Hashtable;
using System.Threading;
using protobuf_3;

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

            //SerialAgencyServer server = new SerialAgencyServer("127.0.0.1", 55555, serviceImpl);
            Proto3AgencyServer server = new Proto3AgencyServer("127.0.1", 55557, serviceImpl);
            server.Start();
            Console.WriteLine("Server started ...");
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

    public class Proto3AgencyServer : ConcurrentServer
    {
        private IAgencyService server;
        private AgencyProtoWorker worker;

        public Proto3AgencyServer(string host, int port, IAgencyService server) : base(host, port)
        {
            this.server = server;
            Console.WriteLine("ProtoChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new AgencyProtoWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
}
