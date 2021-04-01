using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AgentieTurism.repository;
using AgentieTurism.domain;
namespace AgentieTurism.service
{
    
    class ServiceAgency
    {
        private TripRepository tripRepository;
        private AgentRepository agentRepository;
        private ReservationRepository reservationRepository;
        public TravelAgent mainUser { get; set; }

        public ServiceAgency(TripRepository tripRepository, AgentRepository agentRepository, ReservationRepository reservationRepository)
        {
            this.tripRepository = tripRepository;
            this.agentRepository = agentRepository;
            this.reservationRepository = reservationRepository;
        }
        public void Login(String username, String passw)
        {
            TravelAgent user = agentRepository.findAgentsByUser(username, passw);
            if (user != null)
            {
                mainUser = user;
            }
            else
            {
                throw new ServiceException("Invalid username/password!");
            }
        }

        public IEnumerable<Trip> GetAllTrips()
        {
            return tripRepository.findAll();
        }
        public IEnumerable<Trip> FindTripsByNameAndHours(String name, int minHout, int maxHour)
        {
            return tripRepository.findTripsByNameAndHours(name, minHout, maxHour);
        }
        public void AddUser(TravelAgent agent)
        {
            agentRepository.save(agent);
        }
        public void AddReservation(String client, String tel, Trip tripToReserve,int nrTick)
        {
            int nrAvaible = tripToReserve.FreeTickets;
            if (nrAvaible < nrTick)
            {
                throw new ServiceException("There are no tickets!");
            }
            else
            {
                tripToReserve.FreeTickets = nrAvaible - nrTick;
                tripRepository.update(tripToReserve);
                Reservation reservation = new Reservation(client, tel, tripToReserve, nrTick);
                reservationRepository.save(reservation);
            }
        }
        public void Logout()
        {
            mainUser = null;   
        }

        internal void UpdateTrip(Trip t)
        {
            tripRepository.update(t);
        }
    }
}
