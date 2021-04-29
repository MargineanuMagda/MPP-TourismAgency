package repository.ORM;

import domain.TravelAgent;
import domain.Trip;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.RepoException;
import repository.TripRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TripHibernateDBImpl implements TripRepository {
    public static SessionFactory sessionFactory;

    private static final Logger logger= LogManager.getLogger();

    private final Validator<Trip> tripValidator;

    public TripHibernateDBImpl(Validator<Trip> tripValidator) {
        this.tripValidator = tripValidator;
    }

    @Override
    public Trip findOne(Long id) {
        Trip findTrip = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                // Getting Transaction Object From Session Object
                tx=session.beginTransaction();

                findTrip = session.createQuery("from Trip where id =:cod", Trip.class)
                        .setParameter("cod",id)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return findTrip;
    }

    @Override
    public Iterable<Trip> findAll() {
        List<Trip> tripEntities=new ArrayList<>();

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                tripEntities = session.createQuery("FROM Trip",Trip.class).list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return tripEntities;
    }

    @Override
    public Trip save(Trip entity) throws ValidationException, RepoException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();


                session.save(entity);
                 tx.commit();

                System.out.println(" id entity"+entity.getId());


                return entity;
            } catch (RuntimeException ex) {
                if (tx != null) {
                    ex.printStackTrace();
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");

                    tx.rollback();
                }

            }
        }return null;
    }

    @Override
    public Trip delete(Long id) throws RepoException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();


                Trip tripToDelete = session.createQuery("from Trip where id=:cod ",Trip.class).setParameter("cod",id).setMaxResults(1).uniqueResult();
                System.out.println("Stergem trip: "+tripToDelete);

                if(tripToDelete!=null){
                    session.delete(tripToDelete);
                }else{
                    throw new RepoException("Trip with this id does not exist!");
                }


                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Deleted All Records From The Database Table!\n");
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    System.out.println("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }

                sqlException.printStackTrace();
            }

        }
        return null;
    }


    @Override
    public Trip update(Trip entity) throws RepoException, ValidationException {
        tripValidator.validate(entity);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();


                Trip tripFound = session.createQuery("from Trip where id=:cod ",Trip.class).setParameter("cod",entity.getId()).setMaxResults(1).uniqueResult();
                System.out.println("Updatam trip: "+tripFound);


                if(tripFound!=null){

                    tripFound.setPlace(entity.getPlace());
                    tripFound.setFreeTickets(entity.getFreeTickets());
                    session.update(tripFound);

                }else{
                    throw new RepoException("Trip with this id does not exist!");
                }


                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Updated All Records From The Database Table!\n");
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    System.out.println("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }

                sqlException.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterable<Trip> findTripsByDate(LocalDate minDate, LocalDate maxDate) {
        return null;
    }

    @Override
    public Iterable<Trip> findTripsByName(String name) {
        return null;
    }

    @Override
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minTime, LocalTime maxTime) {
        List<Trip> tripEntities=new ArrayList<>();

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                tripEntities = session.createQuery("from Trip  where HOUR(date) between HOUR(:minHour) and HOUR(:maxHour) and place like :place",Trip.class)
                        .setParameter("minHour",minTime)
                        .setParameter("maxHour",maxTime)
                        .setParameter("place","%"+name+"%")
                        .list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return tripEntities;
    }
}
