package rpcProtocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import dto.*;
import repository.RepoException;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AgencyServicesRpcProxy implements IAgencyService {
    private String host;
    private int port;

    private IAgencyObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public AgencyServicesRpcProxy(String serverIP, int serverPort) {
        System.out.println("RPC PROXY SERVER host: "+serverIP+" port: "+serverPort);
        this.host = serverIP;
        this.port = serverPort;
        qresponses=new LinkedBlockingQueue<Response>();
        try {
            initializeConnection();
        } catch (ServiceException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void login(TravelAgent user, IAgencyObserver client) throws ServiceException {
        //initializeConnection();
        AgentDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServiceException(err);
        }
    }


    @Override
    public Iterable<Trip> getAllTrips() throws ServiceException{


        Request req=new Request.Builder().type(RequestType.GET_ALL_TRIPS).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
        Iterable<TripDTO> tripDTO=(Iterable<TripDTO>)response.data();
        Iterable<Trip> trips= DTOUtils.getFromDTO(tripDTO);
        return trips;
    }

    @Override
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minHour, LocalTime maxHour) throws ServiceException {
        FilterDTO udto= DTOUtils.getDTO(name, minHour, maxHour);
        Request req=new Request.Builder().type(RequestType.TRIPS_BY_NAME_HOUR).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
        Iterable<TripDTO> tripDTO=(Iterable<TripDTO>)response.data();
        Iterable<Trip> trips= DTOUtils.getFromDTO(tripDTO);
        return trips;
    }

    @Override
    public void addUser(TravelAgent newUser) throws ValidationException, RepoException, ServiceException {

        AgentDTO adto = DTOUtils.getDTO(newUser);
        Request req=new Request.Builder().type(RequestType.ADD_USER).data(adto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }
    }

    @Override
    public void addReservation(Reservation r) throws ValidationException, RepoException, ServiceException {

        ReservationDTO adto = DTOUtils.getDTO(r);
        Request req=new Request.Builder().type(RequestType.ADD_RESERVATION).data(adto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
        }

    }

    @Override
    public void logout(TravelAgent user, IAgencyObserver client) throws ServiceException {

        AgentDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServiceException(err);
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
    private void sendRequest(Request request)throws ServiceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ServiceException {
        try {
            connection=new Socket(host,port);
            System.out.println(host+" port"+port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
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

    private void handleUpdate(Response response){


        Reservation reservation = DTOUtils.getFromDTO((ReservationDTO) response.data());

        System.out.println("Update; Reservarea  e: "+reservation);
        try {
                client.reservationAdded(reservation);
            } catch (ServiceException | RemoteException e) {
                e.printStackTrace();
            }

    }

    private boolean isUpdate(Response response){

        return response.type()== ResponseType.NEW_RESERVATION;

    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        System.out.println("ReaderThread: response need update");
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
