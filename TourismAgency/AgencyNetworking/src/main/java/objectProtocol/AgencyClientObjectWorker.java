package objectProtocol;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import dto.AgentDTO;
import dto.DTOUtils;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AgencyClientObjectWorker implements Runnable, IAgencyObserver {
    private IAgencyService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public AgencyClientObjectWorker(IAgencyService agencyServer, Socket connection) {
        this.server=agencyServer;
        this.connection=connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {

    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void reservationAdded(List<Trip> list) {

    }
}
