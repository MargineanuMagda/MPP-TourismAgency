import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.AgentValidator;
import domain.validators.ReservationValidator;
import domain.validators.TripValidator;
import domain.validators.Validator;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.AgentRepository;
import repository.ORM.AgentHibernateDbImpl;
import repository.ORM.TripHibernateDBImpl;
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
    private static int defaultPort = 55555;

    public static void main(String[] args) {


        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }


        //HIBERNATE
        try {

            Validator<Trip> tripValidator = new TripValidator();
            //TripRepository repoTrip = new TripDbRepository(serverProps, tripValidator);
            TripRepository repoTrip = new TripHibernateDBImpl(tripValidator);
            Validator<TravelAgent> agentValidator = new AgentValidator();
            //AgentRepository repoAgent= new AgentDbRepository(serverProps,agentValidator);
            initialize();
            AgentRepository repoAgent = new AgentHibernateDbImpl(agentValidator);
            Validator<Reservation> reservationValidator = new ReservationValidator();

            ReservationRepository repoReservation = new ReservationDbRepository(serverProps, reservationValidator);


            IAgencyService serverImplementation = new AgencyServiceImpl(repoTrip, repoAgent, repoReservation);
            int chatServerPort = defaultPort;
            try {
                chatServerPort = Integer.parseInt(serverProps.getProperty("agency.server.port"));
            } catch (NumberFormatException nef) {
                System.err.println("Wrong  Port Number" + nef.getMessage());
                System.err.println("Using default port " + defaultPort);
            }
            System.out.println("Starting server on port: " + chatServerPort);
            AbstractServer server = new AgencyProtobuffConcurrentServer(chatServerPort, serverImplementation);
            try {
                server.start();
            } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }


    }
    //HIBERNATE INITIALIZE

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
                .build();
        try {
            System.out.println("Incerc");
            //AgentHibernateDbImpl.sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

            Metadata metaData = new MetadataSources(registry).getMetadataBuilder().build();
            AgentHibernateDbImpl.sessionFactory = metaData.getSessionFactoryBuilder().build();
            TripHibernateDBImpl.sessionFactory= metaData.getSessionFactoryBuilder().build();
            System.out.println("Initializat");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            StandardServiceRegistryBuilder.destroy(registry);
        }

    }

    static void close() {
        if (AgentHibernateDbImpl.sessionFactory != null) {
            AgentHibernateDbImpl.sessionFactory.close();
        }
        if ( TripHibernateDBImpl.sessionFactory != null ) {
            TripHibernateDBImpl.sessionFactory.close();
        }
    }
}
