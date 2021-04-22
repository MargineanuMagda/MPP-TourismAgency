using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using model.domain;
using services;
using persistence;
namespace Server
{
    class AgencyServiceImpl : IAgencyService
    {
        //MarshalByRefObject
        private TripRepository tripRepository;
        private AgentRepository agentRepository;
        private ReservationRepository reservationRepository;
        private readonly IDictionary<string, IAgencyObserver> loggedClients;

        public AgencyServiceImpl(TripRepository tripRepository, AgentRepository agentRepository, ReservationRepository reservationRepository)
        {
            this.tripRepository = tripRepository;
            this.agentRepository = agentRepository;
            this.reservationRepository = reservationRepository;
            loggedClients = new Dictionary<string, IAgencyObserver>();
        }

        public void AddReservation(Reservation reservation)
        {
            int nrAvaible = reservation.Trip.FreeTickets;
            if (nrAvaible < reservation.NrTickets)
            {
                throw new ServiceException("There are no tickets!");
            }
            else
            {
                reservation.Trip.FreeTickets = nrAvaible - reservation.NrTickets;
                tripRepository.update(reservation.Trip);
                reservationRepository.save(reservation);

                NotifyUsersLoggedIn(reservation);
            }
        }

        private void NotifyUsersLoggedIn(Reservation reservation)
        {
            
            Console.WriteLine("notify logged users " + loggedClients.Count());
            foreach (KeyValuePair<string, IAgencyObserver> entry in loggedClients)
            {
                // do something with entry.Value or entry.Key
                Task.Run(() => entry.Value.ReservationAdded(reservation));
            }
            
        }

        public void AddUser(TravelAgent agent)
        {
            agentRepository.save(agent);
        }

        public IEnumerable<Trip> FindTripsByNameAndHours(string name, int minHout, int maxHour)
        {
            return tripRepository.findTripsByNameAndHours(name, minHout, maxHour);
        }

        public IEnumerable<Trip> GetAllTrips()
        {
            return tripRepository.findAll();
        }

        public void Login(TravelAgent user, IAgencyObserver client)
        {
            
            TravelAgent user1 = agentRepository.findAgentsByUser(user.Username, user.Passwd);
            if (user1 != null)
            {
                if (loggedClients.ContainsKey(user1.Username))
                    throw new ServiceException("User already logged in.");
                user.ID = user1.ID;
                loggedClients[user.Username] = client;
            }
            else
            {
                throw new ServiceException("Invalid username/password!");
            }
        }

        public void Logout(TravelAgent user, IAgencyObserver client)
        {
            IAgencyObserver localClient = loggedClients[user.Username];
            if (localClient == null)
                throw new ServiceException("User " + user.ID + " is not logged in.");
            loggedClients.Remove(user.Username);
            

        }
        /*
        public override object InitializeLifetimeService()
        {
            return null;
        }
        */
    
    }
}
