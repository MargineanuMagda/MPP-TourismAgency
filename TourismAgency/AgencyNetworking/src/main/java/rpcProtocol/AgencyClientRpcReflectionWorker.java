package rpcProtocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import dto.*;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

public class AgencyClientRpcReflectionWorker implements Runnable, IAgencyObserver {
    private IAgencyService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AgencyClientRpcReflectionWorker(IAgencyService agencyServer, Socket connection) {
        this.server = agencyServer;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
    public void reservationAdded(List<Trip> tripList) throws ServiceException {


        Iterable<TripDTO> dtoTrips = DTOUtils.getDTO(tripList);
        Response resp=new Response.Builder().type(ResponseType.NEW_RESERVATION).data(dtoTrips).build();
        System.out.println("Reservation received.Update!!");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServiceException("Sending error: "+e);
        }
        System.out.println("Am notificat update ul");
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        AgentDTO udto=(AgentDTO)request.data();
        TravelAgent user=DTOUtils.getFromDTO(udto);
        try {
            server.login(user, this);
            return okResponse;
        } catch (ServiceException e) {
            //connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleLOGOUT(Request request){
        System.out.println("Logout request...");
        AgentDTO udto=(AgentDTO)request.data();
        TravelAgent user=DTOUtils.getFromDTO(udto);
        try {
            server.logout(user, this);
            connected=false;
            return okResponse;

        } catch (ServiceException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleADD_USER(Request request){
        System.out.println("ADD_USER Request ...");
        AgentDTO udto=(AgentDTO)request.data();
        TravelAgent user=DTOUtils.getFromDTO(udto);
        try {
            server.addUser(user);
            return okResponse;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }


    }
    private Response handleADD_RESERVATION(Request request){
        System.out.println("ADD_Reservation Request ...");
        ReservationDTO rdto=(ReservationDTO)request.data();
        Reservation reservation=DTOUtils.getFromDTO(rdto);
        try {
            server.addReservation(reservation);
            return okResponse;
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

    }
    private Response handleGET_ALL_TRIPS(Request request){
        System.out.println("Get all trips Request ...");

        try {
            Iterable<Trip> trips=server.getAllTrips();
            Iterable<TripDTO> tripsDTO=DTOUtils.getDTO(trips);
            return new Response.Builder().type(ResponseType.GET_ALL_TRIPS).data(tripsDTO).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleTRIPS_BY_NAME_HOUR(Request request){
        System.out.println("Get  trips by name and hour Request ...");

        FilterDTO dto=(FilterDTO)request.data();

        try {
            Iterable<Trip> trips=server.findTripsByNameAndHours(dto.getPlace(), dto.getMinHour(),dto.getMaxHour());
            Iterable<TripDTO> tripsDTO=DTOUtils.getDTO(trips);
            return new Response.Builder().type(ResponseType.TRIPS_FILTERED).data(tripsDTO).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

}
