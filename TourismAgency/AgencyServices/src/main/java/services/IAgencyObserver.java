package services;

import domain.Reservation;
import domain.Trip;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAgencyObserver  extends Remote {

    void reservationAdded(Reservation newReservation) throws ServiceException, RemoteException;
}
