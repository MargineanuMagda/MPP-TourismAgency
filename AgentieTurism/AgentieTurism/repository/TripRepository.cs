using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AgentieTurism.domain;

namespace AgentieTurism.repository
{
    interface TripRepository:Repository<long,Trip>
    {
        IEnumerable<Trip> findTripsByDate(DateTime minDate, DateTime maxDate);
        IEnumerable<Trip> findTripsByName(String name);
        IEnumerable<Trip> findTripsByNameAndHours(String name, int minTime, int maxTime);
    }
}
