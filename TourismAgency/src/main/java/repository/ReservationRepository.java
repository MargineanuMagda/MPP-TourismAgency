package repository;

import domain.Reservation;

public interface ReservationRepository extends Repository<Long, Reservation> {
    Iterable<Reservation> findReservationByClient(String client);
    Iterable<Reservation> findReservationByTripID(Long tripID);
}
