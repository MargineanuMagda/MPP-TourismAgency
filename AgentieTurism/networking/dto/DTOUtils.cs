using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using model.domain;
namespace networking.dto
{
    public class DTOUtils
    {
        public static TravelAgent getFromDTO(AgentDTO usdto)
        {
            String username = usdto.Username;
            String pass = usdto.Passwd;
            TravelAgent user = new TravelAgent(username, pass);
            user.ID=usdto.UserId;
            return user;

        }
        public static AgentDTO getDTO(TravelAgent agent)
        {
            String username = agent.Username;
            String pass = agent.Passwd;
            return new AgentDTO(agent.ID, username, pass);
        }

        public static Trip getFromDTO(TripDTO tripDTO)
        {
            String place = tripDTO.Place;
            String transport = tripDTO.Transport;
            DateTime date = tripDTO.Data;
            Double price = tripDTO.Price;
            int nrTickets = tripDTO.Tickets;
            int freeTickets = tripDTO.FreeTickets;
            Trip t = new Trip(place, transport, date, price, nrTickets, freeTickets);
            t.ID=tripDTO.Id;
            return t;
        }
        public static TripDTO getDTO(Trip trip)
        {
            long tripId = trip.ID;
            String place = trip.Place;
            String transport = trip.Transport;
            DateTime date = trip.Data;
            Double price = trip.Price;
            int nrTickets = trip.Tickets;
            int freeTickets = trip.FreeTickets;
            return new TripDTO(tripId, place, transport, date, price, nrTickets, freeTickets);
        }
        public static IEnumerable<TripDTO> getDTO(IEnumerable<Trip> trips)
        {
            List<TripDTO> dtoList = new List<TripDTO>();
            trips.ToList().ForEach(trip=> {
                dtoList.Add(getDTO(trip));
            });
            return dtoList;
        }
        public static IEnumerable<Trip> getFromDTO(IEnumerable<TripDTO> tripsDTO)
        {
            List<Trip> trips = new List<Trip>();
            tripsDTO.ToList().ForEach(tripDTO=> {
                trips.Add(getFromDTO(tripDTO));
            });
            return trips;
        }
        public static Reservation getFromDTO(ReservationDTO reservationDTO)
        {
            String client = reservationDTO.Client;
            String telefon = reservationDTO.Telefon;
            Trip trip = new Trip(reservationDTO.TripID, reservationDTO.Place, reservationDTO.AvTickets);
            int nrTick = reservationDTO.NrTick;
            return new Reservation(client, telefon, trip, nrTick);
        }
        public static ReservationDTO getDTO(Reservation reservation)
        {
            String client = reservation.Client;
            String telefon = reservation.Telefon;
            long trip = reservation.Trip.ID;
            String place = reservation.Trip.Place;
            int avTick = reservation.Trip.FreeTickets;
            int nrTick = reservation.NrTickets;
            return new ReservationDTO(client, telefon, trip, place, avTick, nrTick);
        }
        public static FilterDTO getDTO(String place, int minHour, int maxHour)
        {
            return new FilterDTO(place, minHour, maxHour);
        }

    }
}
