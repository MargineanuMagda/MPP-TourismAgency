package service;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import repository.AgentRepository;
import repository.RepoException;
import repository.ReservationRepository;
import repository.TripRepository;

import java.time.LocalTime;

public class ServiceAgency {
    private TripRepository tripRepository;
    private AgentRepository agentRepository;
    private ReservationRepository reservationRepository;
    private TravelAgent mainUser;

    public ServiceAgency(TripRepository tripRepository, AgentRepository agentRepository, ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.agentRepository = agentRepository;
        this.reservationRepository = reservationRepository;
    }
    public void login(String username, String passwd) throws ServiceException {
        TravelAgent user=agentRepository.findAgentByUserAndPassw(username,passwd);
        System.out.println(user);
        if(user!=null){
            mainUser=user;
        }else{
            throw new ServiceException("Invalid Username/Password!");
        }
    }
    public TravelAgent getMainUser(){
        return mainUser;
    }
    public Iterable<Trip> getAllTrips(){
        return tripRepository.findAll();
    }
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minHour, LocalTime maxHour){
        return tripRepository.findTripsByNameAndHours(name,minHour,maxHour);
    }

    public void addUser(TravelAgent newUser) throws ValidationException, RepoException {
        agentRepository.save(newUser);
    }

    public void addReservation(String client, String tel, Trip toReserve, int nrTick) throws ValidationException, RepoException, ServiceException {
        int nrAvaible=toReserve.getFreeTickets();
        if(nrAvaible<nrTick)
            throw new ServiceException("There are no tickets avaible!");
        else{
            toReserve.setFreeTickets(nrAvaible-nrTick);
            tripRepository.update(toReserve);
            Reservation r = new Reservation(client,tel,toReserve,nrTick);
            reservationRepository.save(r);
        }
    }

    public void logout() {
        mainUser=null;
    }
}
