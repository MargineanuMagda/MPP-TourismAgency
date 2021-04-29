import domain.TravelAgent;
import domain.Trip;
import domain.validators.AgentValidator;
import domain.validators.TripValidator;
import domain.validators.ValidationException;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.AgentRepository;
import repository.ORM.AgentHibernateDbImpl;
import repository.ORM.TripHibernateDBImpl;
import repository.RepoException;
import repository.TripRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestORM {
    public static void main(String[] args) {


        initialize();
        AgentRepository repoUser = new AgentHibernateDbImpl(new AgentValidator());

        TravelAgent agent = new TravelAgent("maria@travel","1234567");
        agent.setId(0l);
        /*try {
            repoUser.save(agent);
            System.out.println("User adaugat: "+agent);
            System.out.println("User gasit: "+repoUser.findOne(2L));
            repoUser.findAll().forEach(System.out::println);
            System.out.println("Find by username and passwd: "+repoUser.findAgentByUserAndPassw("magdalena@travel","1234567"));
            //repoUser.delete(4L);
            System.out.println("User gasit: "+repoUser.findOne(2L));
            System.out.println("UPDATE");
            agent.setId(3L);
            agent.setUsername("test@travel");
            repoUser.update(agent);
            System.out.println("User gasit: "+repoUser.findOne(2L));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //REPO TRIP
        TripRepository tripRepository = new TripHibernateDBImpl(new TripValidator());
        Trip trip = new Trip("Cluj","ToraTrans", LocalDateTime.of(2020,7,24,8,0),400d,20,17);
        trip.setId(3L);
        try {
            tripRepository.update(trip);
            tripRepository.findAll().forEach(System.out::println);
            tripRepository.findTripsByNameAndHours("ucu", LocalTime.of(7,0),LocalTime.of(20,0)).forEach(System.out::println);

        } catch (RepoException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        close();
    }
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
            TripHibernateDBImpl.sessionFactory=metaData.getSessionFactoryBuilder().build();
            System.out.println("Initializat");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }

    static void close(){
        if ( AgentHibernateDbImpl.sessionFactory != null ) {
            AgentHibernateDbImpl.sessionFactory.close();
        }
        if ( TripHibernateDBImpl.sessionFactory != null ) {
            TripHibernateDBImpl.sessionFactory.close();
        }


    }
}
