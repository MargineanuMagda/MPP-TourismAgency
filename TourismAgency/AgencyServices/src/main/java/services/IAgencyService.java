package services;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import repository.RepoException;

import java.time.LocalTime;

public interface IAgencyService {
     void login(TravelAgent user,IAgencyObserver client) throws ServiceException;
     Iterable<Trip> getAllTrips()throws ServiceException;
     Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minHour, LocalTime maxHour)throws ServiceException;
     void addUser(TravelAgent newUser) throws ValidationException, RepoException,ServiceException;
     void addReservation(Reservation reservation) throws ValidationException, RepoException, ServiceException;
     void logout(TravelAgent user,IAgencyObserver client)throws ServiceException;
}
