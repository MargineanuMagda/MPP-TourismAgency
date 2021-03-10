package repository.database;

import domain.TravelAgent;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.AgentRepository;
import repository.RepoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AgentDbRepository implements AgentRepository {

    private final JdbcUtils dbUtils;
    private final Validator<TravelAgent> agentValidator;

    private static final Logger logger= LogManager.getLogger();

    public AgentDbRepository(Properties props, Validator<TravelAgent> agentValidator){
        this.agentValidator = agentValidator;
        logger.info("Initializing AgentRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public TravelAgent findOne(Long aLong) {
        logger.traceEntry("finding agent with id {} ",aLong);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from login where idf=?")){
            preStmt.setLong(1,aLong);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("idt");
                    String username = result.getString("username");
                    String passwd = result.getString("passw");
                    TravelAgent travelAgent=new TravelAgent(username,passwd);
                    travelAgent.setId(id);
                    logger.traceExit(travelAgent);
                    return travelAgent;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No travel agent found with id {}", aLong);

        return null;
    }

    @Override
    public Iterable<TravelAgent> findAll() {
        Connection con=dbUtils.getConnection();
        List<TravelAgent> agents=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from login")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idf");
                    String username = result.getString("username");
                    String passwd = result.getString("passw");
                    TravelAgent travelAgent=new TravelAgent(username,passwd);
                    travelAgent.setId(id);
                    agents.add(travelAgent);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(agents);
        return agents;
    }

    @Override
    public TravelAgent save(TravelAgent entity) throws ValidationException, RepoException {
        if (entity == null)
            throw new RepoException("Repository exception: id must be not null!\n");
        agentValidator.validate(entity);
        if (findOne(entity.getId()) != null) {
            return entity;
        } else{
            logger.traceEntry("saving agent {} ",entity);
            Connection con=dbUtils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("insert into login(username,passw) values (?,?)")){
                //preStmt.setDouble(1,entity.getId());
                preStmt.setString(1,entity.getUsername());
                preStmt.setString(2,entity.getPasswd());

                int result=preStmt.executeUpdate();
                logger.traceEntry("rows affected {}",result);
            }catch (SQLException ex){
                logger.error(ex);
                System.out.println("Error DB "+ex);
            }
            logger.traceExit();
        }
        return null;
    }

    @Override
    public TravelAgent delete(Long aLong) throws RepoException {
        if (aLong == null)
            throw new RepoException("Repository exception: id must be not null!\n");

        logger.traceEntry("deleting travel agent with {}",aLong);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from login where idf=?")){
            preStmt.setLong(1,aLong);
            int result=preStmt.executeUpdate();
            logger.traceEntry("rows affected {}",result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public TravelAgent update(TravelAgent entity) throws RepoException, ValidationException {
        if (entity == null)
            throw new RepoException("Repository exception: id must be not null!\n");
        agentValidator.validate(entity);
        if (findOne(entity.getId()) == null) {
            return entity;
        } else{
            logger.traceEntry("updating agent {} ",entity);
            Connection con=dbUtils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("update login set username=?,passw=? where idf=?")){
                //preStmt.setDouble(1,entity.getId());
                preStmt.setString(1,entity.getUsername());
                preStmt.setString(2,entity.getPasswd());
                preStmt.setDouble(3,entity.getId());

                int result=preStmt.executeUpdate();
                logger.traceEntry("rows affected {}",result);
            }catch (SQLException ex){
                logger.error(ex);
                System.out.println("Error DB "+ex);
            }
            logger.traceExit();
        }
        return null;
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from login")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }
}
