package protobuffprotocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import dto.AgentDTO;
import dto.DTOUtils;
import dto.FilterDTO;
import dto.TripDTO;
import repository.RepoException;
import rpcProtocol.Request;
import rpcProtocol.RequestType;
import rpcProtocol.Response;
import rpcProtocol.ResponseType;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AgencyProtoProxi implements IAgencyService {
    private String host;
    private int port;

    private IAgencyObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<AgencyProtobufs.AgencyResponse> qresponses;
    private volatile boolean finished;

    public AgencyProtoProxi(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<AgencyProtobufs.AgencyResponse>();
        try {
            initializeConnection();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(TravelAgent user, IAgencyObserver client) throws ServiceException {

        sendRequest(ProtoUtils.createLoginRequest(user));
        AgencyProtobufs.AgencyResponse response=readResponse();
        if (response.getType()== AgencyProtobufs.AgencyResponse.Type.OK){
            this.client=client;
            return;
        }
        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            //closeConnection();
            throw new ServiceException(errorText);
        }
    }

    @Override
    public Iterable<Trip> getAllTrips() throws ServiceException {

        sendRequest(ProtoUtils.createGetAllTripsRequest());
        AgencyProtobufs.AgencyResponse response=readResponse();
        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }

        Iterable<Trip> trips= ProtoUtils.getTrips(response);
        return trips;
    }

    @Override
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minHour, LocalTime maxHour) throws ServiceException {

        sendRequest(ProtoUtils.createFilteredTripsRequest(name,minHour,maxHour));
        AgencyProtobufs.AgencyResponse response=readResponse();
        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
        Iterable<Trip> trips= ProtoUtils.getTrips(response);
        for(Trip t :trips){
            System.out.println("Excursiile filtrate sunt: "+t);
        }

        return trips;
    }

    @Override
    public void addUser(TravelAgent newUser) throws ValidationException, RepoException, ServiceException {

        sendRequest(ProtoUtils.createNewUserRequest(newUser));
        AgencyProtobufs.AgencyResponse response=readResponse();

        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            throw new ServiceException(errorText);
        }
    }

    @Override
    public void addReservation(Reservation reservation) throws ValidationException, RepoException, ServiceException {

        sendRequest(ProtoUtils.createNewReservationRequest(reservation));
        AgencyProtobufs.AgencyResponse response=readResponse();

        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            //closeConnection();
            throw new ServiceException(errorText);
        }
    }

    @Override
    public void logout(TravelAgent user, IAgencyObserver client) throws ServiceException {

        sendRequest(ProtoUtils.createLogoutRequest(user));
        AgencyProtobufs.AgencyResponse response=readResponse();
        closeConnection();
        if (response.getType()==AgencyProtobufs.AgencyResponse.Type.ERROR){
            String errorText=ProtoUtils.getError(response);
            //closeConnection();
            throw new ServiceException(errorText);
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendRequest(AgencyProtobufs.AgencyRequest request)throws ServiceException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }
    private AgencyProtobufs.AgencyResponse readResponse() throws ServiceException{
        AgencyProtobufs.AgencyResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ServiceException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    AgencyProtobufs.AgencyResponse response=AgencyProtobufs.AgencyResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
    private void handleUpdate(AgencyProtobufs.AgencyResponse updateResponse){
        if(updateResponse.getType()== AgencyProtobufs.AgencyResponse.Type.NEW_RESERVATION){
            Reservation reservation = ProtoUtils.getReservation(updateResponse);
            System.out.println("Reservarea ce urmeaza a fi updatata: "+reservation);
            try {
                client.reservationAdded(reservation);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isUpdateResponse(AgencyProtobufs.AgencyResponse.Type type){
        switch (type){
            case NEW_RESERVATION -> {return true;}
        }
        return false;
    }
}
