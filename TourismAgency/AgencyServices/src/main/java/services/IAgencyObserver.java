package services;

import domain.Reservation;
import domain.Trip;

import java.util.List;

public interface IAgencyObserver {

    void reservationAdded(List<Trip> tripsRefreshed) throws ServiceException;
}
