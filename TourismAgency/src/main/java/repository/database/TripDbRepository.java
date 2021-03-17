package repository.database;

import domain.Trip;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.RepoException;
import repository.TripRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TripDbRepository implements TripRepository {

    private JdbcUtils dbUtils;
    private Validator<Trip> tripValidator;

    private static final Logger logger= LogManager.getLogger();

    public TripDbRepository(Properties props, Validator<Trip> tripValidator){
        this.tripValidator = tripValidator;
        logger.info("Initializing TripRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Trip findOne(Long aLong) {
        logger.traceEntry("finding trip with id {} ",aLong);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from trip where idt=?")){
            preStmt.setLong(1,aLong);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int tickets = result.getInt("tickets");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,tickets,freeTickets);
                    trip.setId(id);
                    logger.traceExit(trip);
                    return trip;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No task found with id {}", aLong);

        return null;
    }

    @Override
    public Iterable<Trip> findAll() {
        Connection con=dbUtils.getConnection();
        List<Trip> trips=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from trip")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int tickets = result.getInt("tickets");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,tickets,freeTickets);
                    trip.setId(id);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(trips);
        return trips;
    }

    @Override
    public Trip save(Trip entity) throws ValidationException, RepoException {

        tripValidator.validate(entity);
        if (findOne(entity.getId()) != null) {
            return entity;
        } else{
            logger.traceEntry("saving trip {} ",entity);
            Connection con=dbUtils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("insert into trip(place,transport,datat,price,tickets,freetickets) values (?,?,?,?,?,?) returning idT ")){
                //preStmt.setDouble(1,entity.getId());
                preStmt.setString(1,entity.getPlace());
                preStmt.setString(2,entity.getTransport());
                preStmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.of(entity.getDate(),entity.getDepartureTime())));

                preStmt.setDouble(4,entity.getPrice());
                preStmt.setInt(5,entity.getNrTickets());
                preStmt.setInt(6,entity.getFreeTickets());
                try(ResultSet result=preStmt.executeQuery()) {
                    if (result.next()) {
                        long id = result.getLong("idt");
                        entity.setId(id);

                    }}
            }catch (SQLException ex){
                logger.error(ex);
                System.out.println("Error DB "+ex);
            }
            logger.traceExit();
        }
        return null;
    }

    @Override
    public Trip delete(Long aLong) throws RepoException {
        if (aLong == null)
            throw new RepoException("Repository exception: id must be not null!\n");

        logger.traceEntry("deleting trip with {}",aLong);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from trip where idt=?")){
            preStmt.setLong(1,aLong);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Trip update(Trip entity) throws RepoException, ValidationException {

        tripValidator.validate(entity);
        if (findOne(entity.getId()) == null) {
            return entity;
        } else{
            logger.traceEntry("updating trip {} ",entity);
            Connection con=dbUtils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("update trip set place=?,transport=?,datat=?,price=?,tickets=?,freetickets=? where idt=?")){
                //preStmt.setDouble(1,entity.getId());
                preStmt.setString(1,entity.getPlace());
                preStmt.setString(2,entity.getTransport());
                preStmt.setTimestamp(3,Timestamp.valueOf(LocalDateTime.of(entity.getDate(),entity.getDepartureTime())));

                preStmt.setDouble(4,entity.getPrice());
                preStmt.setInt(5,entity.getNrTickets());
                preStmt.setInt(6,entity.getFreeTickets());
                preStmt.setLong(7,entity.getId());
                int result=preStmt.executeUpdate();
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
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from trip")) {
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

    @Override
    public Iterable<Trip> findTripsByDate(LocalDate minDate, LocalDate maxDate) {
        Connection con=dbUtils.getConnection();
        List<Trip> trips=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from trip where datat between ? and ?")) {
            preStmt.setTimestamp(1,Timestamp.valueOf(LocalDateTime.of(minDate,LocalTime.MIN)));
            preStmt.setTimestamp(2,Timestamp.valueOf(LocalDateTime.of(maxDate,LocalTime.MIN)));
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int tickets = result.getInt("tickets");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,tickets,freeTickets);
                    trip.setId(id);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(trips);
        return trips;
    }

    @Override
    public Iterable<Trip> findTripsByName(String name) {
        Connection con=dbUtils.getConnection();
        List<Trip> trips=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from trip where place like ?")) {
            preStmt.setString(1,"%"+name+"%");
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int tickets = result.getInt("tickets");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,tickets,freeTickets);
                    trip.setId(id);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(trips);
        return trips;
    }

    @Override
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minTime, LocalTime maxTime) {
        Connection con=dbUtils.getConnection();
        List<Trip> trips=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from trip where date_part('hour',datat) between ? and ? and place like ?")) {
            preStmt.setInt(1,minTime.getHour());
            preStmt.setInt(2,maxTime.getHour());
            preStmt.setString(3,"%"+name+"%");
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int tickets = result.getInt("tickets");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,tickets,freeTickets);
                    trip.setId(id);
                    trips.add(trip);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(trips);
        return trips;
    }
}
