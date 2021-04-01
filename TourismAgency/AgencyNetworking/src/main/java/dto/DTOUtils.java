package dto;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DTOUtils {
    public static TravelAgent getFromDTO(AgentDTO usdto){
        String username=usdto.getUsername();
        String pass=usdto.getPasswd();
        TravelAgent user= new TravelAgent(username, pass);
        user.setId(usdto.getUserId());
        return user;

    }
    public static AgentDTO getDTO(TravelAgent agent){
        String username = agent.getUsername();
        String pass = agent.getPasswd();
        return new AgentDTO(agent.getId(), username,pass);
    }
    public static Trip getFromDTO(TripDTO tripDTO){
        String place = tripDTO.getPlace();
        String transport= tripDTO.getTransport();
        LocalDateTime date=tripDTO.getDate();
        Double price= tripDTO.getPrice();
        Integer nrTickets= tripDTO.getNrTickets();
        Integer freeTickets= tripDTO.getFreeTickets();
        Trip t= new Trip(place,transport,date,price,nrTickets,freeTickets);
        t.setId(tripDTO.getTripId());
        return t;
    }
    public static TripDTO getDTO(Trip trip){
        Long tripId = trip.getId();
        String place = trip.getPlace();
        String transport= trip.getTransport();
        LocalDateTime date=trip.getDate();
        Double price= trip.getPrice();
        Integer nrTickets= trip.getNrTickets();
        Integer freeTickets= trip.getFreeTickets();
        return new TripDTO(tripId,place,transport,date,price,nrTickets,freeTickets);
    }
    public static Iterable<TripDTO> getDTO(Iterable<Trip> trips){
        List<TripDTO> dtoList = new ArrayList<>();
        trips.forEach(trip -> {
            dtoList.add(getDTO(trip));
        });
        return dtoList;
    }
    public static Iterable<Trip> getFromDTO(Iterable<TripDTO> tripsDTO){
        List<Trip> trips = new ArrayList<>();
        tripsDTO.forEach(tripDTO -> {
            trips.add(getFromDTO(tripDTO));
        });
        return trips;
    }
    public static Reservation getFromDTO(ReservationDTO reservationDTO){
        String client=reservationDTO.getClient();
        String telefon= reservationDTO.getTelefon();
        Trip trip=new Trip(reservationDTO.getTripID(), reservationDTO.getPlace(), reservationDTO.getAvTickets());
        Integer nrTick= reservationDTO.getNrTick();
        return new Reservation(client,telefon,trip,nrTick);
    }
    public static ReservationDTO getDTO(Reservation reservation){
        String client=reservation.getClient();
        String telefon= reservation.getTelefon();
        Long trip=reservation.getTripId().getId();
        String place = reservation.getTripId().getPlace();
        Integer avTick = reservation.getTripId().getFreeTickets();
        Integer nrTick= reservation.getNrTick();
        return new ReservationDTO(client,telefon,trip,place,avTick,nrTick);
    }
    public static FilterDTO getDTO(String place, LocalTime minHour, LocalTime maxHour){
        return new FilterDTO(place, minHour, maxHour);
    }



}
