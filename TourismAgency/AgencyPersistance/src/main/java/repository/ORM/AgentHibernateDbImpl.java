package repository.ORM;

import domain.TravelAgent;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.AgentRepository;
import repository.RepoException;

import java.util.ArrayList;
import java.util.List;

public class AgentHibernateDbImpl implements AgentRepository {

    public static SessionFactory sessionFactory;

    private static final Logger logger= LogManager.getLogger();

    private final Validator<TravelAgent> agentValidator;

    public AgentHibernateDbImpl(Validator<TravelAgent> agentValidator) {
        this.agentValidator = agentValidator;
    }

    @Override
    public TravelAgent findAgentByUserAndPassw(String username, String passwd) {

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();

                TravelAgent user = session.createQuery("SELECT user FROM TravelAgent user WHERE user.username = :username and user.passwd = :parola",TravelAgent.class)
                        .setParameter("username", username)
                        .setParameter("parola",passwd)
                        .setMaxResults(1).uniqueResult();


                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Deleted All Records From The Database Table!\n");
                return user;
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public TravelAgent findOne(Long aLong) {
        TravelAgent findUser = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                // Getting Transaction Object From Session Object
                tx=session.beginTransaction();

                findUser = session.createQuery("from TravelAgent where id =:cod", TravelAgent.class)
                        .setParameter("cod",aLong)
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
        return findUser;
    }

    @Override
    public Iterable<TravelAgent> findAll() {
        List<TravelAgent> userEntities=new ArrayList<>();

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                userEntities = session.createQuery("FROM TravelAgent",TravelAgent.class).list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return userEntities;
    }

    @Override
    public TravelAgent save(TravelAgent entity) throws ValidationException, RepoException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();


                session.save(entity);
                tx.commit();

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
    public TravelAgent delete(Long aLong) throws RepoException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();


                TravelAgent user = session.createQuery("from TravelAgent where id=:cod ",TravelAgent.class).setParameter("cod",aLong).setMaxResults(1).uniqueResult();
                System.out.println("Stergem userul: "+user);

                if(user!=null){
                    session.delete(user);
                }else{
                    throw new RepoException("User with this id does not exist!");
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
    public TravelAgent update(TravelAgent entity) throws RepoException, ValidationException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();

                //TravelAgent user = session.createQuery("from TravelAgent where id=:cod ",TravelAgent.class).setParameter("cod",aLong).setMaxResults(1).uniqueResult();
                System.out.println("Update entity: "+entity);

                session.update(entity);

                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Updated All Records From The Database Table!\n");
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
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
}
