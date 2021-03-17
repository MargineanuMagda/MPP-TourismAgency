using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AgentieTurism.domain;

namespace AgentieTurism.repository
{
    interface ReservationRepository:Repository<long,Reservation>
    {
        IEnumerable<Reservation> findReservationByClient(String client);
        IEnumerable<Reservation> findReservationByTripID(long tripID);
    }
}
