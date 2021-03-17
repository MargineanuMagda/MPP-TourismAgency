package repository.database;

import domain.Reservation;
import domain.Trip;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.RepoException;
import repository.ReservationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReservationDbRepository implements ReservationRepository {
    private final JdbcUtils dbUtils;
    private final Validator<Reservation> reservationValidator;

    private static final Logger logger= LogManager.getLogger();

    public ReservationDbRepository(Properties props, Validator<Reservation> reservationValidator){
        this.reservationValidator=reservationValidator;
        logger.info("Initializing ReservationRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }
    @Override
    public Reservation findOne(Long aLong) {
        logger.traceEntry("finding reservation with id {} ",aLong);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets as tick,b.freetickets,b.datat " +
                "from reservation a inner join trip b " +
                "on a.tripid=b.idT where idr=?")){
            preStmt.setLong(1,aLong);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("idr");
                    String client = result.getString("client");
                    String tel = result.getString("telefon");
                    int tickets = result.getInt("tickets");
                    long idT = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int ticketsTrip = result.getInt("tick");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,ticketsTrip,freeTickets);
                    trip.setId(idT);

                    Reservation reservation = new Reservation(client,tel,trip,tickets);
                    reservation.setId(id);
                    logger.traceExit(reservation);
                    return reservation;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No reservation found with id {}", aLong);

        return null;
    }

    @Override
    public Iterable<Reservation> findAll() {
        Connection con=dbUtils.getConnection();
        List<Reservation> reservationList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets as tick,b.freetickets,b.datat " +
                "from reservation a inner join trip b " +
                "on a.tripid=b.idT")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idr");
                    String client = result.getString("client");
                    String tel = result.getString("telefon");
                    int tickets = result.getInt("tickets");
                    long idT = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int ticketsTrip = result.getInt("tick");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,ticketsTrip,freeTickets);
                    trip.setId(idT);

                    Reservation reservation = new Reservation(client,tel,trip,tickets);
                    reservation.setId(id);
                    reservationList.add(reservation);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(reservationList);
        return reservationList;
    }

    @Override
    public Reservation save(Reservation entity) throws ValidationException, RepoException {

        reservationValidator.validate(entity);

        logger.traceEntry("saving reservation {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into reservation(client,telefon,tripid,tickets) values (?,?,?,?) returning idr")){
                //preStmt.setDouble(1,entity.getId());
            preStmt.setString(1,entity.getClient());
            preStmt.setString(2,entity.getTelefon());
            preStmt.setLong(3,entity.getTripId().getId());
            preStmt.setInt(4,entity.getNrTick());

            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    long id = result.getLong("idr");
                    entity.setId(id);

                }}
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Reservation delete(Long aLong) throws RepoException {
        if (aLong == null)
            throw new RepoException("Repository exception: id must be not null!\n");

        logger.traceEntry("deleting reservation agent with {}",aLong);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from reservation where idr=?")){
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
    public Reservation update(Reservation entity) throws RepoException, ValidationException {
        if (entity == null)
            throw new RepoException("Repository exception: id must be not null!\n");
        reservationValidator.validate(entity);
        if (findOne(entity.getId()) == null) {
            return entity;
        } else{
            logger.traceEntry("updating reservation {} ",entity);
            Connection con=dbUtils.getConnection();
            try(PreparedStatement preStmt=con.prepareStatement("update reservation set client=?,telefon=?,tripid=?,tickets=? where idr=?")){
                //preStmt.setDouble(1,entity.getId());
                preStmt.setString(1,entity.getClient());
                preStmt.setString(2,entity.getTelefon());
                preStmt.setLong(3,entity.getTripId().getId());
                preStmt.setInt(4,entity.getNrTick());
                preStmt.setLong(5,entity.getId());

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
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from reservation")) {
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
    public Iterable<Reservation> findReservationByClient(String client1) {
        Connection con=dbUtils.getConnection();
        List<Reservation> reservationList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets as tick,b.freetickets,b.datat from reservation a inner join trip b on a.tripid=b.idT where client like ?")) {
            preStmt.setString(1,"%"+client1+"%");
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idr");
                    String client = result.getString("client");
                    String tel = result.getString("telefon");
                    int tickets = result.getInt("tickets");
                    long idT = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int ticketsTrip = result.getInt("tick");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,ticketsTrip,freeTickets);
                    trip.setId(idT);

                    Reservation reservation = new Reservation(client,tel,trip,tickets);
                    reservation.setId(id);
                    reservationList.add(reservation);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(reservationList);
        return reservationList;
    }

    @Override
    public Iterable<Reservation> findReservationByTripID(Long tripID) {
        Connection con=dbUtils.getConnection();
        List<Reservation> reservationList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets as tick,b.freetickets,b.datat from reservation a inner join trip b on a.tripid=b.idT where tripid=?")) {
            preStmt.setLong(1,tripID);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong("idr");
                    String client = result.getString("client");
                    String tel = result.getString("telefon");
                    int tickets = result.getInt("tickets");
                    long idT = result.getLong("idt");
                    String place = result.getString("place");
                    String transport = result.getString("transport");
                    LocalDateTime dataOra = result.getTimestamp("datat").toLocalDateTime();
                    LocalDate data = dataOra.toLocalDate();
                    LocalTime hour = dataOra.toLocalTime();
                    double price = result.getDouble("price");
                    int ticketsTrip = result.getInt("tick");
                    int freeTickets = result.getInt("freetickets");
                    Trip trip = new Trip(place,transport,data,hour,price,ticketsTrip,freeTickets);
                    trip.setId(idT);

                    Reservation reservation = new Reservation(client,tel,trip,tickets);
                    reservation.setId(id);
                    reservationList.add(reservation);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(reservationList);
        return reservationList;
    }
}
