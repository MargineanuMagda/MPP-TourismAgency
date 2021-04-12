using System;
using System.Collections.Generic;
using model.domain;
namespace services
{
    public interface IAgencyService
    {
        void Login(TravelAgent user, IAgencyObserver client);
        IEnumerable<Trip> GetAllTrips();
        IEnumerable<Trip> FindTripsByNameAndHours(String name, int minHout, int maxHour);
        void AddUser(TravelAgent agent);
        void AddReservation(Reservation reservation);
        void Logout(TravelAgent user, IAgencyObserver client);
    }
}
