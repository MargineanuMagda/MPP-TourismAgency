using System;
using System.Collections.Generic;

using model.domain;

namespace persistence
{
    public interface TripRepository:Repository<long,Trip>
    {
        IEnumerable<Trip> findTripsByDate(DateTime minDate, DateTime maxDate);
        IEnumerable<Trip> findTripsByName(String name);
        IEnumerable<Trip> findTripsByNameAndHours(String name, int minTime, int maxTime);

       
    }
}
