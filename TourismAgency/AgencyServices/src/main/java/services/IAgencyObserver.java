package services;

import domain.Reservation;
import domain.Trip;

import java.util.List;

public interface IAgencyObserver {

    void reservationAdded(Reservation newReservation) throws ServiceException;
}
