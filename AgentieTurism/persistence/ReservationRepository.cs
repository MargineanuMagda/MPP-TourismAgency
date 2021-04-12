using System;
using System.Collections.Generic;
using model.domain;

namespace persistence

{
    public interface ReservationRepository:Repository<long,Reservation>
    {
        IEnumerable<Reservation> findReservationByClient(String client);
        IEnumerable<Reservation> findReservationByTripID(long tripID);
    }
}
