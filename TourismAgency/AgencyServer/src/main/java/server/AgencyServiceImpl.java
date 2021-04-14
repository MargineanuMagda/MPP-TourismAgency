package server;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import repository.AgentRepository;
import repository.RepoException;
import repository.ReservationRepository;
import repository.TripRepository;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AgencyServiceImpl implements IAgencyService {

    private TripRepository tripRepository;
    private AgentRepository agentRepository;
    private ReservationRepository reservationRepository;

    private Map<String, IAgencyObserver> loggedClients;
    private final int defaultThreadsNo=5;

    public AgencyServiceImpl(TripRepository tripRepository, AgentRepository agentRepository, ReservationRepository reservationRepository) {
        this.tripRepository = tripRepository;
        this.agentRepository = agentRepository;
        this.reservationRepository = reservationRepository;
        loggedClients=new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(TravelAgent user, IAgencyObserver client) throws ServiceException {
        TravelAgent userLogat=agentRepository.findAgentByUserAndPassw(user.getUsername(),user.getPasswd());


        if(userLogat!=null){
            if(loggedClients.get(userLogat.getUsername())!=null)
                throw new ServiceException("User already logged in.");
            loggedClients.put(userLogat.getUsername(), client);
            user.setId(userLogat.getId());
        }else{
            throw new ServiceException("Invalid Username/Password!");
        }
    }

    @Override
    public synchronized Iterable<Trip> getAllTrips() throws ServiceException {
        Iterable<Trip> trips=tripRepository.findAll();


        return trips;
    }

    @Override
    public synchronized  Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minHour, LocalTime maxHour) throws ServiceException {
        return tripRepository.findTripsByNameAndHours(name,minHour,maxHour);
    }

    @Override
    public synchronized void addUser(TravelAgent newUser) throws ValidationException, RepoException, ServiceException {

        agentRepository.save(newUser);
    }

    @Override
    public synchronized void addReservation(Reservation reservation) throws ValidationException, RepoException, ServiceException {
        int nrAvaible=reservation.getTripId().getFreeTickets();
        if(nrAvaible<reservation.getNrTick())
            throw new ServiceException("There are no tickets avaible!");
        else{
            reservation.getTripId().setFreeTickets(nrAvaible-reservation.getNrTick());
            tripRepository.update(reservation.getTripId());

            reservationRepository.save(reservation);
        }

        /*for( IAgencyObserver client: loggedClients.values()){
            client.reservationAdded(reservation);
        }*/
        notifyUsersLoggedIn(reservation);

    }

    private void notifyUsersLoggedIn(Reservation reservation) {
        Iterable<String> friends= loggedClients.keySet();
        System.out.println("Logged "+friends);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(String us :friends){
            IAgencyObserver client=loggedClients.get(us);
            if (client!=null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + us+ "] a reservation has been added.");
                        //List<Trip> tripList = StreamSupport.stream(tripRepository.findAll().spliterator(), false).collect(Collectors.toList());

                        client.reservationAdded(reservation);
                    } catch (ServiceException e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }

    @Override
    public synchronized void logout(TravelAgent user, IAgencyObserver client) throws ServiceException {

        IAgencyObserver localClient=loggedClients.remove(user.getUsername());
        if (localClient==null)
            throw new ServiceException("User "+user.getId()+" is not logged in.");

    }
}
