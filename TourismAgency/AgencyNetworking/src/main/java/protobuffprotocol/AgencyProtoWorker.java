package protobuffprotocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import repository.RepoException;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;

public class AgencyProtoWorker implements Runnable, IAgencyObserver {

    private IAgencyService server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public AgencyProtoWorker(IAgencyService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                AgencyProtobufs.AgencyRequest request=AgencyProtobufs.AgencyRequest.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                AgencyProtobufs.AgencyResponse response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }
    @Override
    public void reservationAdded(Reservation newReservation) throws ServiceException {

        System.out.println("Update new reservation...");
        try {
            sendResponse(ProtoUtils.createNewReservationResponse(newReservation));
        } catch (IOException e) {
            throw new ServiceException("Sending  new reservation response error... "+e);
        }
    }

    private AgencyProtobufs.AgencyResponse handleRequest(AgencyProtobufs.AgencyRequest request) {
        AgencyProtobufs.AgencyResponse response = null;
        switch (request.getType()){
            case LOGIN -> {
                System.out.println("LOGIN REQUEST...");
                TravelAgent user = ProtoUtils.getUser(request);
                try{
                    server.login(user,this);
                    return ProtoUtils.createOkResponse();
                } catch (ServiceException e) {
                    //connected=false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case LOGOUT -> {
                System.out.println("LOGOUT REQUEST...");
                TravelAgent user = ProtoUtils.getUser(request);
                try{
                    server.logout(user,this);
                    connected=false;
                    return ProtoUtils.createOkResponse();
                } catch (ServiceException e) {

                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case ADD_USER -> {
                System.out.println("Add user request..");
                TravelAgent user = ProtoUtils.getUser(request);
                try{
                    server.addUser(user);
                    return ProtoUtils.createOkResponse();
                } catch (ServiceException | ValidationException | RepoException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GET_ALL_TRIPS -> {
                System.out.println("Get all trips request..");
                try {
                    Iterable<Trip> trips = server.getAllTrips();
                    return ProtoUtils.createGetAllTripsResponse(trips);
                } catch (ServiceException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case TRIPS_BY_NAME_HOUR -> {
                System.out.println("Get filtered trips request..");
                try {
                    String s = request.getFilterTrips().getPlace();
                    LocalTime min = LocalTime.of(request.getFilterTrips().getMinHour(),0);
                    LocalTime max = LocalTime.of(request.getFilterTrips().getMaxHour(),0);
                    System.out.println("Se cauta excursiile in locul: "+s+" incepand cu ora: "+min+"si max ora: "+max);
                    Iterable<Trip> ftrips = server.findTripsByNameAndHours(s, min,max);
                    for(Trip t :ftrips){
                        System.out.println("Excursiile filtrate sunt: "+t);
                    }
                    return ProtoUtils.createFilteredTripsResponse(ftrips);
                } catch (ServiceException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case ADD_RESERVATION -> {
                System.out.println("New reservation request..");
                Reservation r = ProtoUtils.getReservation(request);
                try{
                    server.addReservation(r);
                    return ProtoUtils.createOkResponse();
                } catch (ServiceException | RepoException | ValidationException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }

        }
        return response;
    }




    private void sendResponse(AgencyProtobufs.AgencyResponse response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        //output.writeObject(response);
        output.flush();
    }
}
