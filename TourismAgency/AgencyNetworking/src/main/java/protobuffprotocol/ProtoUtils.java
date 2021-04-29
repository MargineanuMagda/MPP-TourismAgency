package protobuffprotocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import org.apache.logging.log4j.core.pattern.TextRenderer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static AgencyProtobufs.AgencyRequest createLoginRequest(TravelAgent user){
        AgencyProtobufs.TravelAgent userDTO = AgencyProtobufs.TravelAgent.newBuilder().setUserId(user.getId()).setUsername(user.getUsername()).setPasswd(user.getPasswd()).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.LOGIN)
                .setUser(userDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createLogoutRequest(TravelAgent user){
        AgencyProtobufs.TravelAgent userDTO = AgencyProtobufs.TravelAgent.newBuilder().setUserId(user.getId()).setUsername(user.getUsername()).setPasswd(user.getPasswd()).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.LOGOUT)
                .setUser(userDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createGetAllTripsRequest(){
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.GET_ALL_TRIPS).build();
        return request;
    }

    public static AgencyProtobufs.AgencyRequest createFilteredTripsRequest(String name, LocalTime minHour, LocalTime maxHour){
        AgencyProtobufs.Filter filterDTO = AgencyProtobufs.Filter.newBuilder().setPlace(name).setMaxHour(maxHour.getHour()).setMinHour(minHour.getHour()).build();

        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.TRIPS_BY_NAME_HOUR)
                .setFilterTrips(filterDTO).build();
        return request;
    }
    public static AgencyProtobufs.AgencyRequest createNewUserRequest(TravelAgent user){
        AgencyProtobufs.TravelAgent userDTO = AgencyProtobufs.TravelAgent.newBuilder().setUserId(user.getId()).setUsername(user.getUsername()).setPasswd(user.getPasswd()).build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.ADD_USER)
                .setUser(userDTO).build();
        return request;
    }
    public static AgencyProtobufs.AgencyRequest createNewReservationRequest(Reservation reservation){
        AgencyProtobufs.Reservation resDTO = AgencyProtobufs.Reservation.newBuilder()
                .setClient(reservation.getClient())
                .setTelefon(reservation.getTelefon())
                .setTripID(reservation.getTripId().getId())
                .setPlace(reservation.getTripId().getPlace())
                .setAvTickets(reservation.getTripId().getFreeTickets())
                .setNrTick(reservation.getNrTick())
                .build();
        AgencyProtobufs.AgencyRequest request = AgencyProtobufs.AgencyRequest.newBuilder()
                .setType(AgencyProtobufs.AgencyRequest.Type.ADD_RESERVATION)
                .setReservation(resDTO).build();
        return request;
    }

    public static AgencyProtobufs.AgencyResponse createOkResponse(){
        AgencyProtobufs.AgencyResponse response=AgencyProtobufs.AgencyResponse.newBuilder()
                .setType(AgencyProtobufs.AgencyResponse.Type.OK).build();
        return response;
    }



    public static AgencyProtobufs.AgencyResponse createErrorResponse(String text){
        AgencyProtobufs.AgencyResponse response=AgencyProtobufs.AgencyResponse.newBuilder()
                .setType(AgencyProtobufs.AgencyResponse.Type.ERROR)
                .setError(text).build();
        return response;
    }

    public static AgencyProtobufs.AgencyResponse createGetAllTripsResponse(Iterable<Trip> trips){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        AgencyProtobufs.AgencyResponse.Builder response = AgencyProtobufs.AgencyResponse.newBuilder()
                .setType(AgencyProtobufs.AgencyResponse.Type.GET_ALL_TRIPS);
        for (Trip t : trips){
            AgencyProtobufs.Trip tripDTO = AgencyProtobufs.Trip.newBuilder()
                    .setTripId(t.getId())
                    .setPlace(t.getPlace())
                    .setTransport(t.getTransport())
                    .setDate(t.getDate().format(formatter))
                    .setPrice(t.getPrice())
                    .setNrTickets(t.getNrTickets())
                    .setFreeTickets(t.getFreeTickets())
                    .build();
            response.addTrips(tripDTO);
        }
        return response.build();

    }

    public static AgencyProtobufs.AgencyResponse createFilteredTripsResponse(Iterable<Trip> trips){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        AgencyProtobufs.AgencyResponse.Builder response = AgencyProtobufs.AgencyResponse.newBuilder()
                .setType(AgencyProtobufs.AgencyResponse.Type.TRIPS_FILTERED);
        for (Trip t : trips){
            AgencyProtobufs.Trip tripDTO = AgencyProtobufs.Trip.newBuilder()
                    .setTripId(t.getId())
                    .setPlace(t.getPlace())
                    .setTransport(t.getTransport())
                    .setDate(t.getDate().format(formatter))
                    .setPrice(t.getPrice())
                    .setNrTickets(t.getNrTickets())
                    .setFreeTickets(t.getFreeTickets())
                    .build();
            response.addTrips(tripDTO);
        }
        return response.build();

    }

    public static AgencyProtobufs.AgencyResponse createNewReservationResponse(Reservation reservation){
        AgencyProtobufs.Reservation resDTO = AgencyProtobufs.Reservation.newBuilder()
                .setClient(reservation.getClient())
                .setTelefon(reservation.getTelefon())
                .setTripID(reservation.getTripId().getId())
                .setPlace(reservation.getTripId().getPlace())
                .setAvTickets(reservation.getTripId().getFreeTickets())
                .setNrTick(reservation.getNrTick())
                .build();
        AgencyProtobufs.AgencyResponse response = AgencyProtobufs.AgencyResponse.newBuilder()
                .setType(AgencyProtobufs.AgencyResponse.Type.NEW_RESERVATION)
                .setReservation(resDTO).build();
        return response;
    }
   public static String getError(AgencyProtobufs.AgencyResponse response){
       String errorMessage=response.getError();
       return errorMessage;
   }
    public static TravelAgent getUser(AgencyProtobufs.AgencyResponse response){
        TravelAgent sender = new TravelAgent(response.getUser().getUsername(),response.getUser().getPasswd());
        sender.setId(response.getUser().getUserId());
        return sender;
    }

    public static Iterable<Trip> getTrips(AgencyProtobufs.AgencyResponse response){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Trip> trips = new ArrayList<>();
        for(int i=0;i<response.getTripsCount();i++){
            AgencyProtobufs.Trip tripDTO = response.getTrips(i);
            Trip trip = new Trip(tripDTO.getPlace(),tripDTO.getTransport(),LocalDateTime.parse(tripDTO.getDate(), formatter),tripDTO.getPrice(), tripDTO.getNrTickets(), tripDTO.getFreeTickets());
            trip.setId(tripDTO.getTripId());
            trips.add(trip);
        }
        return trips;
    }
    public static Reservation getReservation(AgencyProtobufs.AgencyResponse response){
        AgencyProtobufs.Reservation reservationDTO = response.getReservation();
        String client=reservationDTO.getClient();
        String telefon= reservationDTO.getTelefon();
        Trip trip=new Trip(reservationDTO.getTripID(), reservationDTO.getPlace(), reservationDTO.getAvTickets());
        Integer nrTick= reservationDTO.getNrTick();
        return new Reservation(client,telefon,trip,nrTick);
    }

    public static TravelAgent getUser(AgencyProtobufs.AgencyRequest request){
        TravelAgent sender = new TravelAgent(request.getUser().getUsername(),request.getUser().getPasswd());
        sender.setId(request.getUser().getUserId());
        return sender;
    }
    public static Reservation getReservation(AgencyProtobufs.AgencyRequest request){
        AgencyProtobufs.Reservation reservationDTO = request.getReservation();
        String client=reservationDTO.getClient();
        String telefon= reservationDTO.getTelefon();
        Trip trip=new Trip(reservationDTO.getTripID(), reservationDTO.getPlace(), reservationDTO.getAvTickets());
        Integer nrTick= reservationDTO.getNrTick();
        return new Reservation(client,telefon,trip,nrTick);
    }

}
