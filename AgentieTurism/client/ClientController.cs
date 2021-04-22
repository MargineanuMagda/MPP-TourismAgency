using System;
using System.Collections.Generic;
using model.domain;
using services;

namespace client
{
    public class ClientController : IAgencyObserver
    {
        //MarshalByRefObject
        /*[field: NonSerializedAttribute()]*/
        public event EventHandler<AgencyUserEventArgs> updateEvent; //ctrl calls it when it has received an update
        private readonly IAgencyService server;
        private TravelAgent currentUser;
        public ClientController(IAgencyService server)
        {
            this.server = server;
            currentUser = null;
        }

        
        public void ReservationAdded(Reservation reservation)
        {
            //String mess = "[" + message.Sender.Id + "]: " + message.Text;
            AgencyUserEventArgs userArgs = new AgencyUserEventArgs(AgencyUserEvent.ReservationAdded, reservation);
            Console.WriteLine("Rezervarea {} a fost primita iar tabelul updatat");
            OnUserEvent(userArgs);
        }

        protected virtual void OnUserEvent(AgencyUserEventArgs e)
        {
            if (updateEvent == null) return;
            updateEvent(this, e);
            Console.WriteLine("Update Event called");
        }

        public IEnumerable<Trip> GetAllTrips()
        {
            return server.GetAllTrips();
        }

        public void Logout()
        {
            Console.WriteLine("Ctrl logout");
            server.Logout(currentUser, this);
            currentUser = null;
        }

        public void AddUser(string user, string pass1)
        {
            TravelAgent agent = new TravelAgent(user, pass1);
            server.AddUser(agent);
        }

        public void Login(string user, string pass1)
        {
            TravelAgent agent = new TravelAgent(user, pass1);
            server.Login(agent, this);
            Console.WriteLine("Setare user curent: {0}",agent);

            currentUser = agent;

        }

        public IEnumerable<Trip> FindTripsByNameAndHours(string name, int minHour, int maxHour)
        {
            return server.FindTripsByNameAndHours(name, minHour, maxHour);
        }

        

        internal void AddReservation(string client, string tel, Trip t, int nrTick)
        {
            Reservation r = new Reservation(client, tel, t, nrTick);
            server.AddReservation(r);
        }
    }
}
