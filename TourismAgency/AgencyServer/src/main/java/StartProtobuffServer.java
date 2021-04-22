import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.AgentValidator;
import domain.validators.ReservationValidator;
import domain.validators.TripValidator;
import domain.validators.Validator;
import repository.AgentRepository;
import repository.ReservationRepository;
import repository.TripRepository;
import repository.database.AgentDbRepository;
import repository.database.ReservationDbRepository;
import repository.database.TripDbRepository;
import server.AgencyServiceImpl;
import services.IAgencyService;
import utils.AbstractServer;
import utils.AgencyProtobuffConcurrentServer;
import utils.AgencyRpcConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties "+e);
            return;
        }
        Validator<Trip> tripValidator=new TripValidator();
        TripRepository repoTrip= new TripDbRepository(serverProps,tripValidator);
        Validator<TravelAgent> agentValidator=new AgentValidator();
        AgentRepository repoAgent= new AgentDbRepository(serverProps,agentValidator);
        Validator<Reservation> reservationValidator=new ReservationValidator();
        ReservationRepository repoReservation = new ReservationDbRepository(serverProps,reservationValidator);


        IAgencyService serverImplementation = new AgencyServiceImpl(repoTrip,repoAgent,repoReservation);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("agency.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new AgencyProtobuffConcurrentServer(chatServerPort, serverImplementation);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
